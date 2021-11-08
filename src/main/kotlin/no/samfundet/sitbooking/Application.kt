package no.samfundet.sitbooking

import io.ktor.server.engine.*
import io.ktor.server.netty.*
import no.samfundet.sitbooking.plugins.*
import no.samfundet.sitbooking.user.registerUserRoutes

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0") {
        configureRouting()
        registerUserRoutes()
        configureSerialization()
    }.start(wait = true)
}
