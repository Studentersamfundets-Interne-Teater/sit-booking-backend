package no.samfundet.sitbooking.userAuth

import kotlinx.serialization.Serializable

@Serializable
data class NewUser(
    val userName: String,
    val fullName: String,
    val email: String,
    val phone: String,
    val password: String
)
