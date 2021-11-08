package no.samfundet.sitbooking.user

import io.ktor.application.*
import io.ktor.response.*
import io.ktor.routing.*

fun Route.userRouting() {
    route("/user") {
        get {
            call.respond(User("erikaja", "Erik", "Jakobsen", "erik.a.jakobsen@gmail.com", "92859131"))
        }
    }
}

fun Application.registerUserRoutes() {
    routing {
        userRouting()
    }
}