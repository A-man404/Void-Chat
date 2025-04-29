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
