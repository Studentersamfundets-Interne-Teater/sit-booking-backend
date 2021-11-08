package no.samfundet.sitbooking.user

import kotlinx.serialization.Serializable

@Serializable
data class User(val userName: String, val firstName: String, val lastName: String, val email: String, val phone: String)