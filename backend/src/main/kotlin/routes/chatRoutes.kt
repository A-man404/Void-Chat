package routes

import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import model.ChatMessage
import service.ChatService


fun Route.chatRoutes() {

    webSocket("/chat") {

        val sender = call.request.queryParameters["sender"] ?: return@webSocket close(
            CloseReason(
                CloseReason.Codes.VIOLATED_POLICY, message = "Sender Email is not specified"
            ),
        )

        val recipient = call.request.queryParameters["recipient"] ?: return@webSocket close(
            CloseReason(
                CloseReason.Codes.VIOLATED_POLICY, message = "Recipient Email is not specified"
            ),
        )

        val sorted = listOf(sender, recipient).sorted()
        val roomId = "${sorted[0].substringBefore("@")}_${sorted[1].substringBefore("@")}"

        try {
            ChatService.addSession(roomId, this)
            for (frame in incoming) {
                if (frame is Frame.Text) {

                    val incomingMessage = frame.readText()
                    val message = ChatMessage(
                        roomId = roomId,
                        sender = sender,
                        recipient = recipient,
                        message = incomingMessage,
                        timeStamp = System.currentTimeMillis()
                    )
                    ChatService.saveMessage(message)
                    ChatService.sendMessage(roomId, message)

                }
            }
        } catch (e: Exception) {
            println("Error: ${e.localizedMessage}")
        } finally {
            ChatService.removeSession(roomId, this)
        }


    }

}
