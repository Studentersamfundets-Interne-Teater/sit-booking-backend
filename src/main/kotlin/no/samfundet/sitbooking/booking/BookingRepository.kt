package no.samfundet.sitbooking.booking

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.toJavaInstant
import java.time.Instant as jtInstant
import no.samfundet.sitbooking.plugins.PGEnum
import no.samfundet.sitbooking.user.UserRepository
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.`java-time`.timestamp
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.UUID

object BookingRepository {
    object BookingTable : Table("booking") {
        val id: Column<UUID> = uuid("id").clientDefault(UUID::randomUUID)
        val username: Column<String> =
            text("username").references(
                UserRepository.UserTable.username,
                onDelete = ReferenceOption.CASCADE
            )
        val title: Column<String> = text("title")
        val description = text("description").nullable()
        val startTime = timestamp("starttime")
        val endTime = timestamp("endtime")
        val status = customEnumeration(
            "status",
            "bookingstatus",
            { value -> BookingStatus.valueOf(value as String) },
            { PGEnum("bookingstatus", it) })
        val created = timestamp("created").clientDefault { Clock.System.now().toJavaInstant() }
    }

    fun getAllBookings(): List<Booking> {
        return transaction {
            BookingTable.selectAll().map(::mapBooking)
        }
    }

    fun getBookings(
        username: String? = null,
        fromDate: jtInstant? = null,
        toDate: jtInstant? = null,
        status: BookingStatus? = null
    ): List<Booking> {
        return transaction {
            val query = BookingTable.selectAll()
            username?.let {
                query.andWhere { BookingTable.username eq it }
            }
            fromDate?.let {
                query.andWhere { BookingTable.startTime greater it }
            }
            toDate?.let {
                query.andWhere { BookingTable.startTime less it }
            }
            status?.let {
                query.andWhere { BookingTable.status eq it }
            }
            query.map(::mapBooking)
        }
    }

    fun create(booking: Booking): String {
        val newBookingId = transaction {
            BookingTable.insert {
                it[id] = booking.id
                it[username] = booking.username
                it[title] = booking.title
                it[description] = booking.description
                it[startTime] = java.time.Instant.ofEpochSecond(booking.startTime.epochSeconds)
                it[endTime] = java.time.Instant.ofEpochSecond(booking.endTime.epochSeconds)
                it[status] = booking.status
            } get BookingTable.id
        }
        return newBookingId.toString()
    }

    private fun mapBooking(row: ResultRow) = Booking(
        id = row[BookingTable.id],
        username = row[BookingTable.username],
        title = row[BookingTable.title],
        description = row[BookingTable.description],
        startTime = Instant.fromEpochSeconds(row[BookingTable.startTime].epochSecond),
        endTime = Instant.fromEpochSeconds(row[BookingTable.endTime].epochSecond)
    )
}
