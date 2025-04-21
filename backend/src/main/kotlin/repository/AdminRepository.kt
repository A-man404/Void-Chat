package repository

import com.example.database.MongoDatabaseFactory
import com.mongodb.client.model.Filters
import com.mongodb.client.model.Updates
import io.ktor.http.*
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.toList
import model.ChangeRoleRequest
import model.RepositoryResponse
import model.User
import model.UserRole

object AdminRepository {

    private val database = MongoDatabaseFactory.client
    private val userCollection = database.getCollection<User>("users")

    suspend fun getAllUsers(page: Int, limit: Int): RepositoryResponse<List<User>> {
        return try {
            val skip = (page - 1) * limit
            val allUsers = userCollection.find().skip(skip).limit(limit).toList()
            RepositoryResponse(
                data = allUsers,
                message = "Data successfully fetched",
                statusCode = HttpStatusCode.Found.value
            )
        } catch (e: Exception) {
            RepositoryResponse(
                data = null,
                message = "Error occured ${e.localizedMessage}",
                statusCode = HttpStatusCode.BadRequest.value
            )
        }

    }

    suspend fun getUserByEmail(email: String): RepositoryResponse<User> {
        return try {
            val user = userCollection.find(Filters.eq("email", email)).firstOrNull() ?: return RepositoryResponse(
                data = null,
                message = "User not found",
                statusCode = HttpStatusCode.NotFound.value
            )


            RepositoryResponse(
                data = user,
                message = "User fetched Successfully",
                statusCode = HttpStatusCode.Found.value
            )
        } catch (e: Exception) {
            RepositoryResponse(
                data = null,
                message = "An Error Occurred ${e.localizedMessage}",
                statusCode = HttpStatusCode.InternalServerError.value
            )
        }
    }

    suspend fun changeRoleRequest(role: UserRole, changeRoleRequest: ChangeRoleRequest): RepositoryResponse<Boolean> {
        userCollection.find(Filters.eq("email", changeRoleRequest.targetEmail)).firstOrNull()
            ?: return RepositoryResponse(
                data = false,
                message = "User Doesn't exist",
                statusCode = HttpStatusCode.NotFound.value
            )


        if (role.name != UserRole.ADMIN.name) {
            return RepositoryResponse(
                data = false,
                message = "You dont have the permission to change the roles",
                statusCode = HttpStatusCode.Unauthorized.value
            )
        }

        val newRole = try {
            UserRole.valueOf(changeRoleRequest.newRole)
        } catch (e: Exception) {
            return RepositoryResponse(
                data = false,
                message = "Invalid role specified",
                statusCode = HttpStatusCode.BadRequest.value
            )
        }

        userCollection.findOneAndUpdate(
            Filters.eq("email", changeRoleRequest.targetEmail),
            Updates.set("role", newRole.name)
        )
        return RepositoryResponse(
            data = true,
            message = "Role Changed Successfully",
            statusCode = HttpStatusCode.OK.value
        )
    }

    suspend fun deactivateUser(email: String): RepositoryResponse<Boolean> {
        userCollection.find(Filters.eq("email", email)).firstOrNull() ?: return RepositoryResponse(
            data = false,
            message = "User not found",
            HttpStatusCode.NotFound.value
        )

        return try {
            userCollection.updateOne(Filters.eq(("email"), email), Updates.set("isActive", false))
            RepositoryResponse(
                data = true,
                message = "User deactivated successfully",
                statusCode = HttpStatusCode.OK.value
            )

        } catch (e: Exception) {
            RepositoryResponse(
                data = false,
                message = "An Error Occurred",
                statusCode = HttpStatusCode.InternalServerError.value
            )
        }
    }

    suspend fun activateUser(email: String): RepositoryResponse<Boolean> {
        userCollection.find(Filters.eq("email", email)).firstOrNull() ?: return RepositoryResponse(
            data = false,
            message = "User not found",
            HttpStatusCode.NotFound.value
        )

        return try {
            userCollection.updateOne(Filters.eq(("email"), email), Updates.set("isActive", true))
            RepositoryResponse(
                data = true,
                message = "User activated successfully",
                statusCode = HttpStatusCode.OK.value
            )

        } catch (e: Exception) {
            RepositoryResponse(
                data = false,
                message = "An Error Occurred",
                statusCode = HttpStatusCode.InternalServerError.value
            )
        }
    }

}