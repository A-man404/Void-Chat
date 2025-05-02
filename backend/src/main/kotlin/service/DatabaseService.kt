package service

import com.example.database.MongoDatabaseFactory
import com.mongodb.kotlin.client.coroutine.MongoCollection
import model.ChatMessage
import model.Group
import model.GroupMessage
import model.User

object DatabaseService {

    private val database = MongoDatabaseFactory.client

    fun getUserCollection(): MongoCollection<User> {
        return database.getCollection("users")
    }

    fun getChatCollection(): MongoCollection<ChatMessage> {
        return database.getCollection("chats")
    }

    fun getGroupCollection(): MongoCollection<Group> {
        return database.getCollection("chat-groups")
    }

    fun getGroupMessageCollection(): MongoCollection<GroupMessage> {
        return database.getCollection("group-messages")
    }


}
