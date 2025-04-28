package repository

import com.example.database.MongoDatabaseFactory
import com.mongodb.client.model.Filters
import com.mongodb.client.model.Updates
import io.ktor.http.*
import kotlinx.coroutines.flow.firstOrNull
import model.FriendUser
import model.RepositoryResponse
import model.User
import service.DatabaseService

object FriendRepository {

    val userCollection = DatabaseService.getUserCollection()


    suspend fun addFriend(email: String, friendEmail: String): RepositoryResponse<Boolean> {
        return try {
            val friend = userCollection.find(Filters.eq("email", friendEmail)).firstOrNull()
            if (friend == null) {
                return RepositoryResponse(
                    data = false,
                    message = "User Doesn't exist",
                    statusCode = HttpStatusCode.NotFound.value
                )
            }
            val alreadyExists = userCollection.find(Filters.eq("friends", friendEmail)).firstOrNull()
            if (alreadyExists != null) {
                return RepositoryResponse(
                    data = false,
                    message = "Already friends with them",
                    statusCode = HttpStatusCode.Conflict.value
                )
            }
            userCollection.findOneAndUpdate(Filters.eq("email", email), Updates.addToSet("friends", friendEmail))
            RepositoryResponse(
                data = true,
                message = "Friend Added Successfully",
                statusCode = HttpStatusCode.OK.value
            )
        } catch (e: Exception) {
            RepositoryResponse(
                data = false,
                message = "An error occurred ${e.message}",
                statusCode = HttpStatusCode.InternalServerError.value
            )
        }
    }

    suspend fun removeFriend(email: String, friendEmail: String): RepositoryResponse<Boolean> {
        return try {
            val friend = userCollection.find(Filters.eq("email", friendEmail)).firstOrNull()
            if (friend == null) {
                return RepositoryResponse(
                    data = false,
                    message = "User Doesn't exist",
                    statusCode = HttpStatusCode.NotFound.value
                )
            }

            val alreadyExists = userCollection.find(Filters.eq("friends", friendEmail)).firstOrNull()
            if (alreadyExists == null) {
                return RepositoryResponse(
                    data = false,
                    message = "You are not friends with them",
                    statusCode = HttpStatusCode.Conflict.value
                )
            }
            userCollection.findOneAndUpdate(Filters.eq("email", email), Updates.pull("friends", friendEmail))
            RepositoryResponse(
                data = true,
                message = "Friend Removed successfully",
                statusCode = HttpStatusCode.OK.value
            )
        } catch (e: Exception) {
            RepositoryResponse(
                data = false,
                message = "An error occurred ${e.message}",
                statusCode = HttpStatusCode.InternalServerError.value
            )
        }
    }


    suspend fun getFriend(email: String, friendEmail: String): RepositoryResponse<FriendUser> {
        return try {

            val friend = userCollection.find(Filters.eq("email", friendEmail)).firstOrNull()
            if (friend == null) {
                return RepositoryResponse(
                    data = null,
                    message = "User Doesn't exist",
                    statusCode = HttpStatusCode.NotFound.value
                )
            }

            val user = userCollection.find(Filters.eq("email", email)).firstOrNull()
            if (user == null) {
                return RepositoryResponse(
                    data = null,
                    message = "Current user doesn't exist",
                    statusCode = HttpStatusCode.NotFound.value
                )
            }

            if (!user.friends.contains(friendEmail)) {
                return RepositoryResponse(
                    data = null,
                    message = "You are not friends with them",
                    statusCode = HttpStatusCode.Conflict.value
                )
            }
            val friendUser = FriendUser.fromUser(friend)

            RepositoryResponse(
                data = friendUser,
                message = "Friend Fetched successfully",
                statusCode = HttpStatusCode.OK.value
            )

        } catch (e: Exception) {
            RepositoryResponse(
                data = null,
                message = "An error occurred ${e.message}",
                statusCode = HttpStatusCode.InternalServerError.value
            )
        }
    }

    suspend fun getAllFriend(email: String): RepositoryResponse<List<FriendUser>> {
        return try {
            val user = userCollection.find(Filters.eq("email", email)).firstOrNull()
            if (user == null) {
                return RepositoryResponse(
                    data = emptyList(),
                    message = "Current user doesn't exist",
                    statusCode = HttpStatusCode.NotFound.value
                )
            }

            val friendEmails = user.friends

            val friends = friendEmails.mapNotNull { friendEmail ->
                val friend = userCollection.find(Filters.eq("email", friendEmail)).firstOrNull()
                friend?.let { FriendUser.fromUser(it) }
            }

            return RepositoryResponse(
                data = friends,
                message = "Friends Fetched Successfully",
                statusCode = HttpStatusCode.OK.value
            )


        } catch (e: Exception) {
            RepositoryResponse(
                data = null,
                message = "An error occurred ${e.message}",
                statusCode = HttpStatusCode.InternalServerError.value
            )
        }
    }

}