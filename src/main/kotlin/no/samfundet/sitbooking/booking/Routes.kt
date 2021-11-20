package no.samfundet.sitbooking.booking

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*


fun Route.bookingRouting() {
    route("/bookings") {
        get {
            val parameters = call.request.queryParameters
            when {
                parameters["future"] != null && parameters["username"]?.isNullOrEmpty() == false ->
                    call.respond(BookingRepository.getUserFutureBookings(parameters["user"]!!))

                parameters["future"] != null ->
                    call.respond(BookingRepository.getFutureBookings())

                parameters["username"]?.isNullOrEmpty() == false ->
                    call.respond(BookingRepository.getUserBookings(parameters["username"]!!))

                else -> call.respond(BookingRepository.getAllBookings())
            }

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

