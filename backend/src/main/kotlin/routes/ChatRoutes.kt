package routes

import com.example.database.MongoDatabaseFactory
import com.mongodb.client.model.Filters
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import kotlinx.coroutines.flow.firstOrNull
import model.User
import repository.ChatRepository
import java.util.concurrent.ConcurrentHashMap


data class ChatUser(
    val email: String,
    val session: DefaultWebSocketServerSession
)

val activeUsers = ConcurrentHashMap<String, ChatUser>()
private val database = MongoDatabaseFactory.client
private val userCollection = database.getCollection<User>("users")


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

        val user1 = userCollection.find(Filters.eq("email", senderEmail)).firstOrNull()
        val user2 = userCollection.find(Filters.eq("email", targetEmail)).firstOrNull()

        if (user1 == null || user2 == null) {
            send("Users not found")
            close(CloseReason(CloseReason.Codes.VIOLATED_POLICY, "Users not registered"))
            return@webSocket
        }


        activeUsers[senderEmail] = ChatUser(senderEmail, this)
        ChatRepository.changeStatus("ONLINE", senderEmail)
        send("Connected as $senderEmail. Chatting with $targetEmail")

        try {
            for (frame in incoming) {
                if (frame is Frame.Text) {
                    val message = frame.readText()

                    val targetSession = activeUsers[targetEmail]
                    if (targetSession != null) {
                        targetSession.session.send("From $senderEmail: $message")
                    } else {
                        send("User $targetEmail is not connected.")
                    }
                }
            }
        } catch (e: Exception) {
            println("Error with $senderEmail: ${e.message}")
        } finally {
            ChatRepository.changeStatus("OFFLINE", senderEmail)
            activeUsers.remove(senderEmail)
            println("$senderEmail disconnected.")
        }
    }
}
