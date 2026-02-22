package infrastructure.user

import application.user.UserService
import application.user.UserServiceImpl
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import infrastructure.Config
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.*
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.principal
import io.ktor.server.request.*
import io.ktor.server.response.*
import java.util.Date
import kotlin.time.Duration.Companion.days

object UserController {
    private val userService: UserService = UserServiceImpl(UserRepositoryImpl())

    suspend fun login(call: ApplicationCall) {
        val request = call.receive<LoginRequestDTO>()
        val user = userService.login(request.username, request.password)
        val token = JWT.create()
            .withClaim("userId", user.id)
            .withClaim("userRole", user.role.displayName)
            .withExpiresAt(Date(System.currentTimeMillis() + 3.days.inWholeMilliseconds))
            .sign(Algorithm.HMAC256(Config.jwtSecret))
        call.respond(
            HttpStatusCode.OK,
            LoginResponseDTO(user.role.displayName, token)
        )
    }

    suspend fun getUser(call: ApplicationCall) {
        val principal = call.principal<JWTPrincipal>()
        val userId = principal!!.payload.getClaim("userId").asString()
        val user = userService.getUser(userId)
        call.respond(HttpStatusCode.OK, user.toDTO())
    }
}