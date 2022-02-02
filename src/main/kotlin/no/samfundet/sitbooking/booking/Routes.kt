package no.samfundet.sitbooking.booking

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import kotlinx.serialization.SerializationException
import java.lang.IllegalArgumentException
import java.lang.NumberFormatException
import java.time.DateTimeException
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneOffset
import java.time.format.DateTimeParseException
import java.time.temporal.WeekFields
import java.util.*
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey

fun Route.bookingRouting() {
    route("/bookings") {
        get {
            val parameters = call.request.queryParameters
            val username = parameters["username"]

            val fromDate: Instant?
            try {
                fromDate = if (parameters["fromDate"] != null) Instant.parse(parameters["fromDate"]) else null
            } catch (e: DateTimeParseException) {
                return@get call.respondText(
                    "Error parsing fromDate" + if (e.message != null) " : ${e.message}" else "",
                    status = HttpStatusCode.BadRequest
                )
            }

            val toDate: Instant?
            try {
                toDate = if (parameters["toDate"] != null) Instant.parse(parameters["toDate"]) else null
            } catch (e: DateTimeParseException) {
                return@get call.respondText(
                    "Error parsing fromDate" + if (e.message != null) " : ${e.message}" else "",
                    status = HttpStatusCode.BadRequest
                )
            }

            val status: BookingStatus?
            try {
                status = if (parameters["status"] != null) BookingStatus.valueOf(parameters["status"]!!) else null
            } catch (e: IllegalArgumentException) {
                return@get call.respondText(
                    "Invalid booking status parameter '${parameters["status"]}'",
                    status = HttpStatusCode.BadRequest
                )
            }


            call.respond(BookingRepository.getBookings(username, fromDate, toDate, status))
        }

        post("/create") {
            val newBooking = try {
                call.receive<Booking>()
            } catch (e: SerializationException) {
                return@post call.respondText(
                    e.message ?: "Parsing JSON failed",
                    status = HttpStatusCode.BadRequest
                )
            }

            return@post call.respondText(
                BookingRepository.create(newBooking),
                status = HttpStatusCode.Created
            )
        }

        get("/weekly") {
            val year = call.request.queryParameters["year"]?.toIntOrNull() ?: return@get call.respondText(
                "Missing or invalid year parameter",
                status = HttpStatusCode.BadRequest
            )
            val weekNumber = call.request.queryParameters["week"]?.toLongOrNull() ?: return@get call.respondText(
                "Missing or invalid week parameter",
                status = HttpStatusCode.BadRequest
            )

            val wf = WeekFields.of(Locale.getDefault())
            val fromDateTime: LocalDate
            try {
                fromDateTime =
                    LocalDate.ofYearDay(year, 1)
                        .with(wf.weekOfWeekBasedYear(), weekNumber)
                        .with(wf.dayOfWeek(), 1)
            } catch (e: DateTimeException) {
                return@get call.respondText(e.message ?: "Bad week format", status = HttpStatusCode.BadRequest)
            }
            val fromDate = fromDateTime.atStartOfDay(ZoneOffset.UTC).toInstant()

            val toDateTime = fromDateTime.plusWeeks(1)
            val toDate = toDateTime.atStartOfDay(ZoneOffset.UTC).toInstant()

            call.respond(BookingRepository.getBookings(fromDate = fromDate, toDate = toDate))
        }
    }
}

