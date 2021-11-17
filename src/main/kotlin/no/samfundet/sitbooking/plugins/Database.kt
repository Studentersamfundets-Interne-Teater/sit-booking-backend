package no.samfundet.sitbooking.plugins

import io.ktor.application.*
import no.samfundet.sitbooking.booking.BookingRepository
import no.samfundet.sitbooking.user.UserRepository
import org.jetbrains.exposed.exceptions.ExposedSQLException
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

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
        "jdbc:postgresql://localhost:55000/sitbooking",
        driver = "org.postgresql.Driver",
        user = "sitbooking",
        password = "secret"
    )

    createBookingStatusEnum()
    createSchemas()
}
