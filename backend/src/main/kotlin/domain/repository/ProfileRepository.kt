package domain.repository

import com.mongodb.client.model.Filters
import io.ktor.http.*
import kotlinx.coroutines.flow.firstOrNull
import model.RepositoryResponse
import model.User
import security.PasswordHasher
import service.DatabaseService

object ProfileRepository {
    val userCollection = DatabaseService.getUserCollection()


    suspend fun getUserProfile(email: String): RepositoryResponse<User> {

        val user = userCollection.find(Filters.eq("email", email)).firstOrNull()

        return if (user == null) {
            RepositoryResponse(data = null, message = "User not found", HttpStatusCode.NotFound.value)
        } else {
            RepositoryResponse(data = user, message = "User Fetched Successfully", HttpStatusCode.Found.value)
        }
    }

    suspend fun deleteUser(email: String, password: String): RepositoryResponse<Boolean> {
        val user = userCollection.find(Filters.eq("email", email)).firstOrNull()

        if (user == null) {
            return RepositoryResponse(data = false, message = "User not found", HttpStatusCode.NotFound.value)
        }

        if (!PasswordHasher.checkPassword(password = password, hashPassword = user.password)) {
            return RepositoryResponse(data = false, message = "Password is incorrect", HttpStatusCode.BadRequest.value)
        }

        val deletedUser = userCollection.findOneAndDelete(Filters.eq("email", email))

        return if (deletedUser != null) {
            RepositoryResponse(data = true, message = "User deleted successfully", HttpStatusCode.OK.value)
        } else {
            RepositoryResponse(data = false, message = "User deletion failed", HttpStatusCode.InternalServerError.value)
        }
    }

    suspend fun searchUser(email: String): RepositoryResponse<User> {

        return try {
            val user = userCollection.find(Filters.eq("email", email)).firstOrNull() ?: return RepositoryResponse(
                data = null, message = "User Doesn't exist", HttpStatusCode.NotFound.value
            )

            val editedUser = user.copy(name = user.name, email = user.email)

            RepositoryResponse(
                data = editedUser,
                message = "User Fetched Successfully",
                statusCode = HttpStatusCode.OK.value
            )
        } catch (e: Exception) {
            RepositoryResponse(
                data = null,
                message = "Unknown error ${e.localizedMessage}",
                statusCode = HttpStatusCode.InternalServerError.value
            )
        }

    }


}