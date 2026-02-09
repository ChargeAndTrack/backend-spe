package infrastructure

import io.ktor.server.application.*
import io.ktor.server.response.*

object UserController {
    suspend fun getUser(call: ApplicationCall) {
        call.respondText("Hello Ktor")
    }
}