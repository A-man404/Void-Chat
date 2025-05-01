package routes

import io.ktor.http.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import kotlinx.serialization.Serializable
import model.Group
import model.GroupMessage
import service.GroupService

fun Route.groupRoutes() {
    route("/group") {

        authenticate("jwt-auth") {

            post("/create") {
                val group = call.receive<Group>()
                val res = GroupService.createGroup(group)
                call.respond(HttpStatusCode.fromValue(res.statusCode), res)
            }

            post("/delete") {
                val groupId = call.receive<String>()
                val res = GroupService.deleteGroup(groupId)
                call.respond(HttpStatusCode.fromValue(res.statusCode), res)
            }

            get("/view") {
                val groupId = call.receive<String>()
                val res = GroupService.viewGroupMembers(groupId)
                call.respond(HttpStatusCode.fromValue(res.statusCode), res)
            }

            post("/add-member") {
                val addMemberRequest = call.receive<AddMemberRequest>()
                val res = GroupService.addGroupMember(addMemberRequest.groupId, addMemberRequest.memberEmail)
                call.respond(HttpStatusCode.fromValue(res.statusCode), res)
            }

            post("/remove-member") {
                val addMemberRequest = call.receive<AddMemberRequest>()
                val res = GroupService.removeGroupMember(addMemberRequest.groupId, addMemberRequest.memberEmail)
                call.respond(HttpStatusCode.fromValue(res.statusCode), res)
            }

            get("/group-messages") {
                val roomId = call.request.queryParameters["roomId"] ?: call.respond(
                    HttpStatusCode.BadRequest, "Please give a room id"
                )
                val list = GroupService.viewGroupMessages(roomId.toString())
                call.respond(list)
            }
        }
    }

    val rooms = mutableMapOf<String, MutableSet<WebSocketSession>>()

    webSocket("group-chat") {

        val roomId = call.request.queryParameters["room"]
        val userEmail = call.request.queryParameters["email"]

        if (roomId == null || userEmail == null) {
            return@webSocket close(CloseReason(CloseReason.Codes.VIOLATED_POLICY, "Missing params"))
        }

        val room = rooms.getOrPut(roomId) { mutableSetOf() }
        room.add(this)

        try {
            for (frame in incoming) {
                if (frame is Frame.Text) {
                    val text = frame.readText()
                    val message = "From: $userEmail: $text"
                    room.forEach {
                        it.send(message)
                    }
                    GroupService.saveMessage(
                        GroupMessage(
                            groupId = roomId, sender = userEmail, message = text, timeStamp = System.currentTimeMillis()
                        )
                    )
                }
            }
        } catch (e: Exception) {
            room.remove(this)
            println(e.localizedMessage)
        } finally {
            room.remove(this)
        }
    }
}

@Serializable
data class AddMemberRequest(
    val groupId: String, val memberEmail: String
)