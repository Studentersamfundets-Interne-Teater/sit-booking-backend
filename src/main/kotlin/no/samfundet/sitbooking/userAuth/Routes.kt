package no.samfundet.sitbooking.userAuth

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import no.samfundet.sitbooking.user.UserWithHashedPassword
import no.samfundet.sitbooking.user.UserRepository
import org.mindrot.jbcrypt.BCrypt

fun Route.authRouting() {
    route("/login") {
        post {
            val userLogin = call.receive<UserLogin>();
            val user =
                UserRepository.getUserWithHashedPasswordByUsername(userLogin.userName)
                    ?: return@post call.respond(HttpStatusCode.Unauthorized);
            if (BCrypt.checkpw(userLogin.password, user.hashedPassword)) {
                return@post call.respondText("Success");
            }
            return@post call.respond(HttpStatusCode.Unauthorized);
        }
    }

    route("/newuser") {
        // TODO: Check that user email is in pre-approved users list – this is a closed site
        post {
            val newUserData = call.receive<NewUser>();
            val hashedPassword = BCrypt.hashpw(newUserData.password, BCrypt.gensalt())
            val newUser = UserWithHashedPassword(
                newUserData.userName,
                newUserData.fullName,
                newUserData.email,
                newUserData.phone,
                false,
                hashedPassword
            )
            val createdUser = UserRepository.create(newUser)
            call.respond(createdUser)
        }
    }
}