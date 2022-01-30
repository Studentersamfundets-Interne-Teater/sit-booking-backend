package no.samfundet.sitbooking.userAuth

import kotlinx.serialization.Serializable

@Serializable
data class UserLogin(val userName: String, val password: String)
