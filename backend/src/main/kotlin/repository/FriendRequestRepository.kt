package repository

import com.mongodb.client.model.Filters
import com.mongodb.client.model.Updates
import io.ktor.http.*
import kotlinx.coroutines.flow.firstOrNull
import model.FriendUser
import model.RepositoryResponse
import service.DatabaseService


object FriendRequestRepository {

    val userCollection = DatabaseService.getUserCollection()

    suspend fun friendRequests(email: String): RepositoryResponse<List<FriendUser>> {

        return try {
            val user = userCollection.find(Filters.eq("email", email)).firstOrNull()
            if (user == null) {
                return RepositoryResponse(
                    data = emptyList(),
                    message = "Current user doesn't exist",
                    statusCode = HttpStatusCode.NotFound.value
                )
            }

            val friendRequestList = user.friendRequests.mapNotNull { friendEmail ->
                val friend = FriendRepository.userCollection.find(Filters.eq("email", friendEmail)).firstOrNull()
                friend?.let { FriendUser.fromUser(it) }
            }

            RepositoryResponse(
                data = friendRequestList,
                message = "Friend Requests Fetched Successfully",
                statusCode = HttpStatusCode.OK.value
            )
        } catch (e: Exception) {
            RepositoryResponse(
                data = emptyList(),
                message = "Error: ${e.message}",
                statusCode = HttpStatusCode.InternalServerError.value
            )
        }

    }

    suspend fun sendFriendRequest(email: String, toEmail: String): RepositoryResponse<Boolean> {
        return try {
            if (email == toEmail) {
                return RepositoryResponse(
                    data = null,
                    message = "You cant perform this operation on yourself",
                    statusCode = HttpStatusCode.Conflict.value
                )
            }
            val user = userCollection.find(Filters.eq("email", email)).firstOrNull()
            if (user == null) {
                return RepositoryResponse(
                    data = false,
                    message = "Current user doesn't exist",
                    statusCode = HttpStatusCode.NotFound.value
                )
            }

            val toUser = userCollection.find(Filters.eq("email", toEmail)).firstOrNull() ?: return RepositoryResponse(
                data = false,
                message = "To User doesnt exist",
                statusCode = HttpStatusCode.Conflict.value
            )

            if (toUser.blockedUser.contains(email)) {
                return RepositoryResponse(
                    data = false,
                    message = "You are blocked by the user",
                    statusCode = HttpStatusCode.Conflict.value
                )
            }

            val alreadyExists =
                FriendRepository.userCollection.find(Filters.eq("friendRequests", toEmail)).firstOrNull()
            if (alreadyExists != null) {
                return RepositoryResponse(
                    data = false,
                    message = "Friend request already sent",
                    statusCode = HttpStatusCode.Conflict.value
                )
            }
            val friendEmail = userCollection.find(Filters.eq("email", toEmail)).firstOrNull()
            if (friendEmail == null) {
                return RepositoryResponse(
                    data = false,
                    message = "The person you are trying to add does not exist",
                    statusCode = HttpStatusCode.NotFound.value
                )
            }

            userCollection.findOneAndUpdate(Filters.eq("email", email), Updates.addToSet("friendRequests", toEmail))

            RepositoryResponse(
                data = true,
                message = "Friend Request Sent Successfully",
                statusCode = HttpStatusCode.OK.value
            )
        } catch (e: Exception) {
            RepositoryResponse(
                data = false,
                message = "Error: ${e.message}",
                statusCode = HttpStatusCode.InternalServerError.value
            )
        }

    }

    suspend fun acceptFriendRequest(email: String, reqEmail: String): RepositoryResponse<Boolean> {
        return try {
            if (email == reqEmail) {
                return RepositoryResponse(
                    data = null,
                    message = "You cant perform this operation on yourself",
                    statusCode = HttpStatusCode.Conflict.value
                )
            }
            val user = userCollection.find(Filters.eq("email", email)).firstOrNull()
            if (user == null) {
                return RepositoryResponse(
                    data = false,
                    message = "Current user doesn't exist",
                    statusCode = HttpStatusCode.NotFound.value
                )
            }

            val toUser = userCollection.find(Filters.eq("email", reqEmail)).firstOrNull()
            if (toUser == null) {
                return RepositoryResponse(
                    data = false,
                    message = "The User you are trying to add doesn't exist",
                    statusCode = HttpStatusCode.NotFound.value
                )
            }
            val alreadyFriends =
                userCollection.find(Filters.eq("friends", reqEmail)).firstOrNull()
            if (alreadyFriends != null) {
                return RepositoryResponse(
                    data = false,
                    message = "You are already friends with them",
                    statusCode = HttpStatusCode.BadRequest.value
                )
            }
            val alreadyExists =
                userCollection.find(Filters.eq("friendRequests", reqEmail)).firstOrNull()
            if (alreadyExists == null) {
                return RepositoryResponse(
                    data = false,
                    message = "You have not sent the friend request",
                    statusCode = HttpStatusCode.BadRequest.value
                )
            }
            val friendEmail = userCollection.find(Filters.eq("email", reqEmail)).firstOrNull()
            if (friendEmail == null) {
                return RepositoryResponse(
                    data = false,
                    message = "The person you are trying to add does not exist",
                    statusCode = HttpStatusCode.NotFound.value
                )
            }

            userCollection.findOneAndUpdate(Filters.eq("email", email), Updates.addToSet("friends", reqEmail))
            userCollection.findOneAndUpdate(Filters.eq("email", email), Updates.pull("friendRequests", reqEmail))

            RepositoryResponse(
                data = true,
                message = "Friend request accepted",
                statusCode = HttpStatusCode.OK.value
            )
        } catch (e: Exception) {
            RepositoryResponse(
                data = false,
                message = "Error: ${e.message}",
                statusCode = HttpStatusCode.InternalServerError.value
            )
        }

    }

    suspend fun rejectFriendRequest(email: String, reqEmail: String): RepositoryResponse<Boolean> {
        return try {
            if (email == reqEmail) {
                return RepositoryResponse(
                    data = null,
                    message = "You cant perform this operation on yourself",
                    statusCode = HttpStatusCode.Conflict.value
                )
            }
            val user = userCollection.find(Filters.eq("email", email)).firstOrNull()
            if (user == null) {
                return RepositoryResponse(
                    data = false,
                    message = "Current user doesn't exist",
                    statusCode = HttpStatusCode.NotFound.value
                )
            }

            val toUser = userCollection.find(Filters.eq("email", reqEmail)).firstOrNull()
            if (toUser == null) {
                return RepositoryResponse(
                    data = false,
                    message = "The User you are trying to add doesn't exist",
                    statusCode = HttpStatusCode.NotFound.value
                )
            }
            val alreadyFriends =
                userCollection.find(Filters.eq("friends", reqEmail)).firstOrNull()
            if (alreadyFriends != null) {
                return RepositoryResponse(
                    data = false,
                    message = "You are already friends with them",
                    statusCode = HttpStatusCode.BadRequest.value
                )
            }

            val alreadyExists =
                userCollection.find(Filters.eq("friendRequests", reqEmail)).firstOrNull()
            if (alreadyExists == null) {
                return RepositoryResponse(
                    data = false,
                    message = "You have not sent the friend request",
                    statusCode = HttpStatusCode.BadRequest.value
                )
            }
            userCollection.findOneAndUpdate(Filters.eq("email", email), Updates.pull("friendRequests", reqEmail))

            RepositoryResponse(
                data = true,
                message = "Friend request rejected",
                statusCode = HttpStatusCode.OK.value
            )
        } catch (e: Exception) {
            RepositoryResponse(
                data = false,
                message = "Error: ${e.message}",
                statusCode = HttpStatusCode.InternalServerError.value
            )
        }

    }

}