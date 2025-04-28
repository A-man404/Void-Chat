package service

import com.example.database.MongoDatabaseFactory
import com.mongodb.kotlin.client.coroutine.MongoCollection
import model.User

object DatabaseService {

    private val database = MongoDatabaseFactory.client

    fun getUserCollection(): MongoCollection<User> {
        return database.getCollection("users")
    }

}
