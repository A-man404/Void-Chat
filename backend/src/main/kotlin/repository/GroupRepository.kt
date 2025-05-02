package repository

import com.mongodb.client.model.Filters
import com.mongodb.client.model.Updates
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.toList
import model.Group
import model.GroupMessage
import service.DatabaseService

object GroupRepository {

    private val groupCollection = DatabaseService.getGroupCollection()
    private val groupMessageCollection = DatabaseService.getGroupMessageCollection()

    suspend fun createGroup(group: Group) {
        groupCollection.insertOne(group).wasAcknowledged()
    }

    suspend fun viewGroupMembers(groupId: String): List<String> {
        val group = groupCollection.find(Filters.eq("groupId", groupId)).firstOrNull()
        return group?.members?.toList() ?: emptyList()
    }

    suspend fun deleteGroup(groupId: String) {
        groupCollection.deleteOne(Filters.eq("groupId", groupId))
    }

    suspend fun addGroupMember(groupId: String, memberEmail: String) {
        groupCollection.findOneAndUpdate(Filters.eq("groupId", groupId), Updates.addToSet("members", memberEmail))
    }

    suspend fun removeGroupMember(groupId: String, memberEmail: String) {
        groupCollection.findOneAndUpdate(
            Filters.eq("groupId", groupId),
            Updates.pull("members", memberEmail)
        )
    }

    suspend fun insertMessage(groupMessage: GroupMessage) {
        groupMessageCollection.insertOne(groupMessage)
    }

    suspend fun viewGroupMessages(roomId: String): List<GroupMessage> {
        return groupMessageCollection.find(Filters.eq("groupId", roomId)).toList()
    }


}