package no.samfundet.sitbooking.plugins

import io.ktor.routing.*
import io.ktor.application.*
import no.samfundet.sitbooking.booking.bookingRouting
import no.samfundet.sitbooking.user.userRouting
import no.samfundet.sitbooking.userAuth.authRouting

fun Application.configureRouting() {
    routing {
        route("api") {
            authRouting()
            userRouting()
            bookingRouting()
        }
    }
}

