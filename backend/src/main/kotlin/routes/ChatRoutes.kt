package routes

import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import java.util.concurrent.ConcurrentHashMap

val activeUsers = ConcurrentHashMap<String, DefaultWebSocketServerSession>()

fun Route.chatRoutes() {

    webSocket("/chat/{senderEmail}/{targetEmail}") {

        val senderEmail = call.parameters["senderEmail"]
        val targetEmail = call.parameters["targetEmail"]

        if (senderEmail == null || targetEmail == null) {
            send("Both sender and target emails are required")
            close(CloseReason(CloseReason.Codes.VIOLATED_POLICY, "Missing emails"))
            return@webSocket
        }

        if (!senderEmail.contains("@") || !targetEmail.contains("@")) {
            send("Invalid email format.")
            close(CloseReason(CloseReason.Codes.VIOLATED_POLICY, "Invalid email format"))
            return@webSocket
        }


        activeUsers[senderEmail] = this
        send("Connected as $senderEmail. Chatting with $targetEmail")

        try {
            for (frame in incoming) {
                if (frame is Frame.Text) {
                    val message = frame.readText()

                    val targetSession = activeUsers[targetEmail]
                    if (targetSession != null) {
                        targetSession.send("From $senderEmail: $message")
                    } else {
                        send("User $targetEmail is not connected.")
                    }
                }
            }
        } catch (e: Exception) {
            println("Error with $senderEmail: ${e.message}")
        } finally {
            activeUsers.remove(senderEmail)
            println("$senderEmail disconnected.")
        }
    }
}
