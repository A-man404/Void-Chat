package model

import kotlinx.serialization.Serializable


@Serializable
data class ChatMessage(
    val roomId: String,
    val sender: String,
    val recipient: String,
    val message: String,
    val timeStamp: Long
)

@Serializable
data class Group(
    val groupId: String,
    val adminEmail: String,
    val members: List<GroupMember>,
    val createdAt: Long = System.currentTimeMillis(),
    val profileImage: String,
    val description: String
)

@Serializable
data class GroupMember(
    val name:String,
    val email:String,
    val profileImage:String
)

@Serializable
data class GroupMessage(
    val groupId: String,
    val sender: String,
    val message: String,
    val timeStamp: Long
)