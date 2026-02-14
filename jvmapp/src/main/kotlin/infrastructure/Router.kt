package infrastructure

import io.ktor.server.application.*
import io.ktor.server.routing.*

object Router {
    val module: Application.() -> Unit = {
        routing {
            route("/api/v1") {
                get("/user") { UserController.getUser(call) }
            }
        }
    }
}
