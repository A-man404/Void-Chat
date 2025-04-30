package routes

import io.ktor.http.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable
import model.Group
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
        }

    }
}

@Serializable
data class AddMemberRequest(
    val groupId: String, val memberEmail: String
)