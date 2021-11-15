package no.samfundet.sitbooking.user

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import kotlinx.serialization.SerializationException
import org.jetbrains.exposed.exceptions.ExposedSQLException

fun Route.userRouting() {
    route("/user") {
        get {
            call.respond(UserRepository.getAllUsers())
        }

        post {
            try {
                val newUser = call.receive<User>()
                call.respondText(UserRepository.create(newUser), status = HttpStatusCode.Created)
            } catch (e: SerializationException) {
                call.respondText("Malformed user object", status = HttpStatusCode.BadRequest)
            } catch (e: ExposedSQLException) {
                call.respondText("Database error", status = HttpStatusCode.BadRequest)
            }
        }

        get("{username}") {
            val username = call.parameters["username"] ?: return@get call.respond(
                status = HttpStatusCode.BadRequest,
                "Malformed username"
            )
            val user =
                UserRepository.getByUsername(username) ?: return@get call.respond(HttpStatusCode.NotFound)
            call.respond(user)
        }
    }
}
