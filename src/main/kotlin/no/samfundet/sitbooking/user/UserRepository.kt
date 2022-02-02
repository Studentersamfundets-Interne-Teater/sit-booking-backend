package no.samfundet.sitbooking.user

import kotlinx.datetime.Clock
import kotlinx.datetime.toJavaInstant
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.`java-time`.timestamp
import org.jetbrains.exposed.sql.transactions.transaction

object UserRepository {
    object UserTable : Table("user_account") {
        val username: Column<String> = text("username")
        val fullName: Column<String> = text("fullname")
        val email: Column<String> = text("email")
        val phone: Column<String> = text("phone")
        val hashedPassword: Column<String> = text("hashedpw")
        val isAdmin: Column<Boolean> = bool("isadmin")
        override val primaryKey = PrimaryKey(username)
    }

    object InvitedUsersTable : Table("invited_users") {
        val email: Column<String> = text("email")
        val invitedAt = timestamp("invitedat").clientDefault { Clock.System.now().toJavaInstant() }
        val acceptedAt = timestamp("acceptedat").nullable()
    }

    fun getAllUsers(): List<User> {
        return transaction {
            UserTable.selectAll().map(::mapUser)
        }
    }

    fun getByUsername(username: String): User? {
        return transaction {
            UserTable.select { UserTable.username eq username }.map(::mapUser).firstOrNull()
        }
    }

    fun getUserWithHashedPasswordByUsername(username: String): UserWithHashedPassword? {
        return transaction {
            UserTable.select { UserTable.username eq username }.map(::mapUserWithHashedPassword).firstOrNull()
        }
    }

    fun getInvitedUserByEmail(email: String) {
        return transaction {
            InvitedUsersTable.select { InvitedUsersTable.email eq email }
        }
    }

    fun setAcceptedInvitationTimeToNow(email: String) {
        return transaction {
            InvitedUsersTable.update({ InvitedUsersTable.email eq email }) {
                it[acceptedAt] = Clock.System.now().toJavaInstant()
            }
        }
    }

    fun addNewInvitations(newEmails: List<String>) {
        return transaction {
            InvitedUsersTable.batchInsert(newEmails) { newEmail ->
                this[InvitedUsersTable.email] = newEmail
            }
        }
    }

    fun create(user: UserWithHashedPassword): String {
        return transaction {
            UserTable.insert {
                it[username] = user.username
                it[fullName] = user.fullName
                it[email] = user.email
                it[phone] = user.phone
                it[hashedPassword] = user.hashedPassword
                it[isAdmin] = user.isAdmin
            } get UserTable.username
        }
    }

    private fun mapUser(row: ResultRow) = User(
        username = row[UserTable.username],
        fullName = row[UserTable.fullName],
        email = row[UserTable.email],
        phone = row[UserTable.phone],
        isAdmin = row[UserTable.isAdmin],
    )

    private fun mapUserWithHashedPassword(row: ResultRow) = UserWithHashedPassword(
        username = row[UserTable.username],
        fullName = row[UserTable.fullName],
        email = row[UserTable.email],
        phone = row[UserTable.phone],
        isAdmin = row[UserTable.isAdmin],
        hashedPassword = row[UserTable.hashedPassword],
    )
}