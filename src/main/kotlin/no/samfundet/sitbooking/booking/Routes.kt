package no.samfundet.sitbooking.booking

import io.ktor.application.*
import io.ktor.response.*
import io.ktor.routing.*
import kotlinx.datetime.toLocalDateTime
import java.util.*


fun Route.eventRouting() {
    route("/event") {
        get {
            call.respond(
                Booking(
                    UUID.randomUUID(),
                    "erikaja",
                    "test booking",
                    null,
                    "2021-11-11T10:00".toLocalDateTime(),
                    "2021-11-11T11:00".toLocalDateTime(),
                    BookingStatus.pending
                )
            )
        }
    }
}
