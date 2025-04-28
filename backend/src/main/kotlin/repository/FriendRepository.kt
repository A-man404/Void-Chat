package repository

import com.mongodb.client.model.Filters
import com.mongodb.client.model.Updates
import io.ktor.http.*
import kotlinx.coroutines.flow.firstOrNull
import model.FriendUser
import model.RepositoryResponse
import service.DatabaseService

object FriendRepository {

    val userCollection = DatabaseService.getUserCollection()


    suspend fun addFriend(email: String, friendEmail: String): RepositoryResponse<Boolean> {
        return try {
            if (email == friendEmail) {
                return RepositoryResponse(
                    data = null,
                    message = "You cant perform this operation on yourself",
                    statusCode = HttpStatusCode.Conflict.value
                )
            }

            val friend = userCollection.find(Filters.eq("email", friendEmail)).firstOrNull()
            if (friend == null) {
                return RepositoryResponse(
                    data = false,
                    message = "User Doesn't exist",
                    statusCode = HttpStatusCode.NotFound.value
                )
            }

            if (friend.blockedUser.contains(email)) {
                return RepositoryResponse(
                    data = false,
                    message = "You are blocked by the user",
                    statusCode = HttpStatusCode.Forbidden.value
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
            if (email == friendEmail) {
                return RepositoryResponse(
                    data = null,
                    message = "You cant perform this operation on yourself",
                    statusCode = HttpStatusCode.Conflict.value
                )
            }
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
            if (email == friendEmail) {
                return RepositoryResponse(
                    data = null,
                    message = "You cant perform this operation on yourself",
                    statusCode = HttpStatusCode.Conflict.value
                )
            }
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

    suspend fun blockUser(email: String, blockEmail: String): RepositoryResponse<Boolean> {
        try {
            val user = userCollection.find(Filters.eq("email", email)).firstOrNull()

            if (email == blockEmail) {
                return RepositoryResponse(
                    data = false,
                    message = "You cant block yourself",
                    statusCode = HttpStatusCode.Conflict.value
                )
            }

            if (user == null) {
                return RepositoryResponse(
                    data = false,
                    message = "Current user doesn't exist",
                    statusCode = HttpStatusCode.NotFound.value
                )
            }

            if (user.blockedUser.contains(blockEmail)) {
                return RepositoryResponse(
                    data = false,
                    message = "This user is already blocked",
                    statusCode = HttpStatusCode.NotFound.value
                )
            }

            val friend = userCollection.find(Filters.eq("email", blockEmail)).firstOrNull()
            if (friend == null) {
                return RepositoryResponse(
                    data = false,
                    message = "The user you are trying to block doesn't exist",
                    statusCode = HttpStatusCode.NotFound.value
                )
            }


            userCollection.findOneAndUpdate(Filters.eq("email", email), Updates.addToSet("blockedUser", blockEmail))
            userCollection.findOneAndUpdate(Filters.eq("email", email), Updates.pull("friends", blockEmail))

            return RepositoryResponse(
                data = true,
                message = "User Blocked Successfully",
                statusCode = HttpStatusCode.OK.value
            )
        } catch (e: java.lang.Exception) {
            return RepositoryResponse(
                data = false,
                message = "Error occurred ${e.message}",
                statusCode = HttpStatusCode.InternalServerError.value
            )
        }
    }

    suspend fun unBlockUser(email: String, unBlockEmail: String): RepositoryResponse<Boolean> {
        try {
            val user = userCollection.find(Filters.eq("email", email)).firstOrNull()

            if (email == unBlockEmail) {
                return RepositoryResponse(
                    data = false,
                    message = "You cant unblock yourself",
                    statusCode = HttpStatusCode.Conflict.value
                )
            }

            if (user == null) {
                return RepositoryResponse(
                    data = false,
                    message = "Current user doesn't exist",
                    statusCode = HttpStatusCode.NotFound.value
                )
            }

            val userBlocked = user.blockedUser.contains(unBlockEmail)

            if (!userBlocked) {
                return RepositoryResponse(
                    data = false,
                    message = "User is not blocked",
                    statusCode = HttpStatusCode.NotFound.value
                )

            }


            val friend = userCollection.find(Filters.eq("email", unBlockEmail)).firstOrNull()
            if (friend == null) {
                return RepositoryResponse(
                    data = false,
                    message = "The user you are trying to unblock doesn't exist",
                    statusCode = HttpStatusCode.NotFound.value
                )
            }


            userCollection.findOneAndUpdate(Filters.eq("email", email), Updates.pull("blockedUser", unBlockEmail))

            return RepositoryResponse(
                data = true,
                message = "User UnBlocked Successfully",
                statusCode = HttpStatusCode.OK.value
            )
        } catch (e: java.lang.Exception) {
            return RepositoryResponse(
                data = false,
                message = "Error occurred ${e.message}",
                statusCode = HttpStatusCode.InternalServerError.value
            )
        }
    }


}