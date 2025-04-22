package repository

import com.example.database.MongoDatabaseFactory
import com.mongodb.client.model.Filters
import com.mongodb.client.model.Updates
import io.ktor.http.*
import kotlinx.coroutines.flow.firstOrNull
import model.RepositoryResponse
import model.User

object ChatRepository {
    private val database = MongoDatabaseFactory.client
    private val userCollection = database.getCollection<User>("users")


    suspend fun changeStatus(status: String, email: String): RepositoryResponse<Boolean> {

        return try {

            userCollection.find(Filters.eq("email", email)).firstOrNull() ?:  RepositoryResponse(
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
}