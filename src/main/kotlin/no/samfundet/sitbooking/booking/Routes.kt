package no.samfundet.sitbooking.booking

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*


fun Route.bookingRouting() {
    route("/events") {
        get {
            call.respond(
                BookingRepository.getAllEvents()
            )
        }

        post {
            val newBooking = try {
                call.receive<Booking>()
            } catch (e: Throwable) {
                return@post call.respondText(
                    e?.message ?: "Parsing JSON failed",
                    status = HttpStatusCode.BadRequest
                )
            }



            return@post call.respondText(
                BookingRepository.create(newBooking).toString(),
                status = HttpStatusCode.Created
            )
        }
    }
}

