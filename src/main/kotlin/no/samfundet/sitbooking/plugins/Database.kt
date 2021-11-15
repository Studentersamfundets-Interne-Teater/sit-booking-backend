package no.samfundet.sitbooking.plugins

import io.ktor.application.*
import no.samfundet.sitbooking.user.UserRepository
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

fun Application.setUpDatabase() {
    Database.connect(
        "jdbc:postgresql://localhost:55000/sitbooking",
        driver = "org.postgresql.Driver",
        user = "sitbooking",
        password = "secret"
    )
    transaction {
        SchemaUtils.create(UserRepository.UserTable)
    }
}
