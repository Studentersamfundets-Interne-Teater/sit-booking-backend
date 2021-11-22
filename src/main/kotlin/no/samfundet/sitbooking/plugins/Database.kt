package no.samfundet.sitbooking.plugins

import io.ktor.application.*
import no.samfundet.sitbooking.booking.BookingRepository
import no.samfundet.sitbooking.user.UserRepository
import org.jetbrains.exposed.exceptions.ExposedSQLException
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.*

fun createBookingStatusEnum() {
    transaction {
        try {
            exec("CREATE TYPE bookingstatus AS ENUM ('approved', 'pending', 'rejected');")
        } catch (e: ExposedSQLException) {
            if (e.message?.contains("ERROR: type \"bookingstatus\" already exists") == true) {
                println("bookingstatus ENUM already defined, skipping")
            } else {
                throw(e)
            }
        }
    }
}

fun createSchemas() {
    transaction {
        SchemaUtils.create(UserRepository.UserTable)
        SchemaUtils.create(BookingRepository.BookingTable)
    }
}

fun Application.setUpDatabase() {
    Database.connect(
        environment.config.property("ktor.database.url").getString(),
        driver = "org.postgresql.Driver",
        user = environment.config.property("ktor.database.user").getString(),
        password = environment.config.property("ktor.database.password").getString()
    )

    createBookingStatusEnum()
    createSchemas()
}
