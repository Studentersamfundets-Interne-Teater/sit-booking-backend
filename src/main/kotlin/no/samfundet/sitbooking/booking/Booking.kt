package no.samfundet.sitbooking.booking

import kotlinx.serialization.Serializable
import kotlinx.datetime.Instant
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.*
import kotlinx.serialization.descriptors.*
import kotlinx.serialization.encoding.*
import java.util.*

@Serializable
data class Booking(
    @Serializable(with = UUIDSerializer::class) val id: UUID = UUID.randomUUID(),
    val username: String,
    val title: String,
    val description: String? = null,
    val startTime: Instant,
    val endTime: Instant,
    var status: BookingStatus = BookingStatus.pending
)

@Serializer(forClass = UUID::class)
object UUIDSerializer : KSerializer<UUID> {
    @ExperimentalSerializationApi
    override val descriptor: SerialDescriptor
        get() = PrimitiveSerialDescriptor("UUID", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: UUID) {
        encoder.encodeString(value.toString())
    }

    override fun deserialize(decoder: Decoder): UUID {
        return UUID.fromString(decoder.decodeString())
    }
}

@Suppress("EnumEntryName")
enum class BookingStatus {
    approved, pending, rejected
}
