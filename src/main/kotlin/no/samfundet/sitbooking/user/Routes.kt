package no.samfundet.sitbooking.user

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import kotlinx.serialization.SerializationException
import org.jetbrains.exposed.exceptions.ExposedSQLException

fun Route.userRouting() {
    route("/users") {
        get {
            call.respond(UserRepository.getAllUsers())
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
