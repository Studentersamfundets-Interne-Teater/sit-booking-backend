package no.samfundet.sitbooking.event

import kotlinx.serialization.Serializable
import kotlinx.datetime.LocalDateTime

@Serializable
data class Event(val startTime: LocalDateTime, val endTime: LocalDateTime)