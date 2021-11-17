package no.samfundet.sitbooking.booking

import no.samfundet.sitbooking.plugins.PGEnum
import no.samfundet.sitbooking.user.UserRepository
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.`java-time`.timestamp
import java.util.*

object BookingRepository {
    @Suppress("EnumEntryName")
    object BookingTable : Table("booking") {
        val id: Column<UUID> = uuid("id").clientDefault(UUID::randomUUID)
        val username: Column<String> =
            text("username").references(UserRepository.UserTable.username, onDelete = ReferenceOption.CASCADE)
        val title: Column<String> = text("title")
        val description = text("description")
        val startTime = timestamp("starttime")
        val endTime = timestamp("endtime")
        val status = customEnumeration(
            "status",
            "bookingstatus",
            { value -> StatusEnum.valueOf(value as String) },
            { PGEnum("bookingstatus", it) })

        enum class StatusEnum { approved, pending, rejected }
    }
}
