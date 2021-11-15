package no.samfundet.sitbooking.user

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

object UserRepository {
    object UserTable : Table("user_account") {
        val username: Column<String> = text("username")
        val fullName: Column<String> = text("fullname")
        val email: Column<String> = text("email")
        val phone: Column<String> = text("phone")
        override val primaryKey = PrimaryKey(username)
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

    fun create(user: User): String {
        return transaction {
            UserTable.insert {
                it[username] = user.userName
                it[fullName] = user.fullName
                it[email] = user.email
                it[phone] = user.phone
            } get UserTable.username
        }
    }

    private fun mapUser(row: ResultRow) = User(
        userName = row[UserTable.username],
        fullName = row[UserTable.fullName],
        email = row[UserTable.email],
        phone = row[UserTable.phone]
    )
}