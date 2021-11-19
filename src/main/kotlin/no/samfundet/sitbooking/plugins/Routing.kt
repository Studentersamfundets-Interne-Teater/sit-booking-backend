package no.samfundet.sitbooking.plugins

import io.ktor.routing.*
import io.ktor.application.*
import no.samfundet.sitbooking.booking.bookingRouting
import no.samfundet.sitbooking.user.userRouting

fun Application.configureRouting() {
    routing {
        userRouting()
        bookingRouting()
    }
}
