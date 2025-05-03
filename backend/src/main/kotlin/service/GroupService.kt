package service

import com.mongodb.client.model.Filters
import com.mongodb.client.model.Updates
import io.ktor.http.*
import kotlinx.coroutines.flow.firstOrNull
import model.Group
import model.GroupMessage
import model.RepositoryResponse
import repository.GroupRepository

object GroupService {

    val userCollection = DatabaseService.getUserCollection()

    suspend fun createGroup(group: Group): RepositoryResponse<Boolean> {
        return try {

            if (userCollection.find(Filters.eq("email", group.adminEmail)).firstOrNull() == null) {
                return RepositoryResponse(false, "Email is not registered", HttpStatusCode.BadRequest.value)
            }
            for (email in group.members) {
                if (userCollection.find(Filters.eq("email", email)).firstOrNull() == null) {
                    return RepositoryResponse(false, "Email $email is not registered", HttpStatusCode.BadRequest.value)
                }
                if (group.adminEmail == email) {
                    return RepositoryResponse(false, "Add at least one member", HttpStatusCode.BadRequest.value)
                }
            }

            if (group.groupId.isBlank()) {
                return RepositoryResponse(false, "Please enter a group id", HttpStatusCode.BadRequest.value)
            }

            if (group.adminEmail.isBlank()) {
                return RepositoryResponse(false, "Admin email is required", HttpStatusCode.BadRequest.value)
            }

            if (group.members.isEmpty()) {
                return RepositoryResponse(false, "Group must have at least one member", HttpStatusCode.BadRequest.value)
            }

            val existing = GroupRepository.viewGroupMembers(group.groupId)
            if (existing.isNotEmpty()) {
                return RepositoryResponse(false, "Group with this ID already exists", HttpStatusCode.Conflict.value)
            }

            val newGroup =
                group.copy(profileImage = "https://avatar.iran.liara.run/public/boy?username=${group.groupId}")

            GroupRepository.createGroup(newGroup)
            userCollection.findOneAndUpdate(
                Filters.eq("email", group.adminEmail),
                Updates.addToSet("groups", group.groupId)
            )

            for (email in group.members) {
                userCollection.findOneAndUpdate(
                    Filters.eq("email", email),
                    Updates.addToSet("groups", group.groupId)
                )
            }
            RepositoryResponse(true, "Group Created Successfully", HttpStatusCode.OK.value)

        } catch (e: Exception) {
            RepositoryResponse(false, "Error occurred: ${e.localizedMessage}", HttpStatusCode.InternalServerError.value)
        }
    }

    suspend fun deleteGroup(groupId: String): RepositoryResponse<Boolean> {
        return try {
            if (groupId.isBlank()) {
                return RepositoryResponse(false, "Please enter a group id", HttpStatusCode.BadRequest.value)
            }
            val existing = GroupRepository.viewGroupMembers(groupId)
            if (existing.isNotEmpty()) {
                GroupRepository.deleteGroup(groupId)
                return RepositoryResponse(true, "Group deleted successfully", HttpStatusCode.OK.value)
            }

            RepositoryResponse(false, "Group with this ID doesn't exist", HttpStatusCode.NotFound.value)

        } catch (e: Exception) {
            RepositoryResponse(false, "Error occurred: ${e.localizedMessage}", HttpStatusCode.InternalServerError.value)
        }
    }

    suspend fun viewGroupMembers(groupId: String): RepositoryResponse<List<String>> {
        return try {
            if (groupId.isBlank()) {
                return RepositoryResponse(emptyList(), "Please enter a group id", HttpStatusCode.BadRequest.value)
            }

            val existing = GroupRepository.viewGroupMembers(groupId)
            if (existing.isEmpty()) {
                return RepositoryResponse(emptyList(), "Group doesn't exist", HttpStatusCode.BadRequest.value)
            }


            RepositoryResponse(existing, "Data fetched successfully", HttpStatusCode.OK.value)

        } catch (e: Exception) {
            RepositoryResponse(
                emptyList(), "Error occurred: ${e.localizedMessage}", HttpStatusCode.InternalServerError.value
            )
        }
    }

    suspend fun addGroupMember(groupId: String, memberEmail: String): RepositoryResponse<Boolean> {
        return try {
            if (groupId.isBlank()) {
                return RepositoryResponse(false, "Please enter a group id", HttpStatusCode.BadRequest.value)
            }

            if (memberEmail.isBlank()) {
                return RepositoryResponse(false, "Member email is required", HttpStatusCode.BadRequest.value)
            }

            val existing = GroupRepository.viewGroupMembers(groupId)
            if (existing.isEmpty()) {
                return RepositoryResponse(false, "Group doesn't exist", HttpStatusCode.Conflict.value)
            }

            if (existing.contains(memberEmail)) {
                return RepositoryResponse(false, "User is already added", HttpStatusCode.Conflict.value)
            }

            GroupRepository.addGroupMember(groupId, memberEmail)
            RepositoryResponse(true, "Member added successfully", HttpStatusCode.OK.value)

        } catch (e: Exception) {
            RepositoryResponse(false, "Error occurred: ${e.localizedMessage}", HttpStatusCode.InternalServerError.value)
        }
    }

    suspend fun removeGroupMember(groupId: String, memberEmail: String): RepositoryResponse<Boolean> {
        return try {
            if (groupId.isBlank()) {
                return RepositoryResponse(false, "Please enter a group id", HttpStatusCode.BadRequest.value)
            }

            if (memberEmail.isBlank()) {
                return RepositoryResponse(false, "Member email is required", HttpStatusCode.BadRequest.value)
            }

            val existing = GroupRepository.viewGroupMembers(groupId)
            if (existing.isEmpty()) {
                return RepositoryResponse(false, "Group doesn't exist", HttpStatusCode.Conflict.value)
            }

            if (existing.contains(memberEmail)) {
                GroupRepository.removeGroupMember(groupId, memberEmail)
                return RepositoryResponse(true, "Member deleted successfully", HttpStatusCode.OK.value)
            } else {
                return RepositoryResponse(true, "Member doesn't exist", HttpStatusCode.BadRequest.value)
            }
        } catch (e: Exception) {
            RepositoryResponse(false, "Error occurred: ${e.localizedMessage}", HttpStatusCode.InternalServerError.value)
        }
    }

    suspend fun saveMessage(message: GroupMessage) {
        GroupRepository.insertMessage(message)
    }

    suspend fun viewGroupMessages(roomId: String): RepositoryResponse<List<GroupMessage>> {
        val res = GroupRepository.viewGroupMessages(roomId)
        return RepositoryResponse(data = res, message = "list", HttpStatusCode.OK.value)
    }

}
