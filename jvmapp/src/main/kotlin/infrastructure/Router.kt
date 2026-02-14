package infrastructure

import infrastructure.user.UserController
import io.ktor.server.application.*
import io.ktor.server.routing.*

object Router {
    val module: Application.() -> Unit = {
        routing {
            route("/api/v1") {
                post("/login") { UserController.login(call) }
                get("/user") { UserController.getUser(call) }
            }
        }
    }
}