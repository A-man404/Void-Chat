package com.example.database

import com.mongodb.kotlin.client.coroutine.MongoClient

object MongoDatabaseFactory {

    private val connectionString = "mongodb+srv://Aman:2312@cluster.naebv2g.mongodb.net/"
    private val databaseName = "void-chat"

    val client = MongoClient.create(connectionString).getDatabase(databaseName)
}