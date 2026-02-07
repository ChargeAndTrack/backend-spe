package infrastructure

import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.application.*

class Server(port: Int, routing: Application.() -> Unit) {

    private val server = embeddedServer(Netty, port = port, module = { routing })

    fun start() {
        server.start(wait = true)
    }
}