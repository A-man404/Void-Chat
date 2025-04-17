package routes

import com.example.database.MongoDatabaseFactory
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.demoRoutes() {

    val database = MongoDatabaseFactory.client

    get("/") {
        call.respondText(database.name)
    }
}