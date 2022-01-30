package no.samfundet.sitbooking.user

import kotlinx.serialization.Serializable

@Serializable
data class UserWithHashedPassword(
    val username: String,
    val fullName: String,
    val email: String,
    val phone: String,
    val isAdmin: Boolean,
    val hashedPassword: String
)
