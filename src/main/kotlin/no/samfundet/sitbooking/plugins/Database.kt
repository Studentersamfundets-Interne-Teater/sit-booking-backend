package no.samfundet.sitbooking.plugins

import io.ktor.application.*
import org.jetbrains.exposed.sql.*
import org.postgresql.*

fun Application.connectToDatabase() {
    Database.connect(
        "jdbc:postgresql://localhost:55000/sitbooking",
        driver = "org.postgresql.Driver",
        user = "sitbooking",
        password = "secret"
    )
}