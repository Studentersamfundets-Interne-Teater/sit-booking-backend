package no.samfundet.sitbooking

import io.ktor.application.*
import no.samfundet.sitbooking.plugins.*


fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

fun Application.module(testing: Boolean = false) {
    configureRouting()
    configureSerialization()
    setUpDatabase()
}
