package repository

import com.mongodb.client.model.Filters
import com.mongodb.client.model.Updates
import kotlinx.coroutines.flow.firstOrNull
import model.Group
import model.GroupMember
import service.DatabaseService

object GroupRepository {

    private val groupCollection = DatabaseService.getGroupCollection()

    suspend fun addGroup(group: Group) {
        groupCollection.insertOne(group).wasAcknowledged()
    }

    suspend fun viewGroupMembers(groupId: String): List<GroupMember> {
        val group = groupCollection.find(Filters.eq("groupId", groupId)).firstOrNull()
        return group?.members ?: emptyList()
    }

    suspend fun deleteGroup(groupId: String) {
        groupCollection.deleteOne(Filters.eq("groupId", groupId))
    }

    suspend fun addGroupMember(groupId: String, groupMember: GroupMember) {
        groupCollection.findOneAndUpdate(Filters.eq("groupId", groupId), Updates.addToSet("members", groupMember))
    }

    suspend fun removeGroupMember(groupId: String, groupMember: GroupMember) {
        groupCollection.findOneAndUpdate(Filters.eq("groupId", groupId), Updates.pull("members", groupMember))
    }

}