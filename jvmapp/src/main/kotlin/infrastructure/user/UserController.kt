package infrastructure.user

import application.user.UserService
import application.user.UserServiceImpl
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*

object UserController {
    private val userService: UserService = UserServiceImpl(UserRepositoryImpl())

    suspend fun login(call: ApplicationCall) {
        val request = call.receive<LoginRequestDTO>()
        val user = userService.login(request.username, request.password)
        println("Login, user: $user")
        call.respond(
            HttpStatusCode.OK,
            LoginResponseDTO(user.role.displayName, "token")
        )
    }

    suspend fun getUser(call: ApplicationCall) {
        call.respondText("Hello Ktor")
    }
}