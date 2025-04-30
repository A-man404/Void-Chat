package repository

import com.mongodb.client.model.Filters
import com.mongodb.client.model.Sorts
import com.mongodb.client.model.Updates
import io.ktor.http.*
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.toList
import model.ChatMessage
import model.RepositoryResponse
import service.DatabaseService

object ChatRepository {
    val userCollection = DatabaseService.getUserCollection()
    val chatCollection = DatabaseService.getChatCollection()


    suspend fun changeStatus(status: String, email: String): RepositoryResponse<Boolean> {

        return try {

            userCollection.find(Filters.eq("email", email)).firstOrNull() ?: RepositoryResponse(
                data = false,
                message = "User not found",
                statusCode = HttpStatusCode.NotFound.value
            )

            userCollection.findOneAndUpdate(Filters.eq("email", email), Updates.set("status", status))
            RepositoryResponse(
                data = true,
                message = "Status Changed",
                statusCode = HttpStatusCode.OK.value
            )
        } catch (e: Exception) {
            RepositoryResponse(
                data = false,
                message = "Error Found ${e.localizedMessage}",
                statusCode = HttpStatusCode.InternalServerError.value
            )
        }
    }


    suspend fun insertMessage(message: ChatMessage) {
        chatCollection.insertOne(message)
    }

    suspend fun fetchAllMessages(roomId: String): List<ChatMessage> {
        return chatCollection.find(Filters.eq("roomId", roomId))
            .sort(Sorts.ascending("timeStamp"))
            .toList()
    }
}