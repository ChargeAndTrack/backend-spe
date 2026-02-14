package infrastructure

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.typesafe.config.ConfigFactory
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.application.*
import io.ktor.server.auth.Authentication
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.jwt.jwt
import io.ktor.server.config.HoconApplicationConfig
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.response.respond
import kotlinx.serialization.json.Json

class Server(port: Int, routing: Application.() -> Unit) {

    private val config = HoconApplicationConfig(ConfigFactory.load())
    private val jwtSecret = config.property("jwt.secret").getString()

    private val server = embeddedServer(Netty, host = "0.0.0.0", port = port) {
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                isLenient = true
                ignoreUnknownKeys = true
            })
        }
        Config.jwtSecret = jwtSecret
        install(Authentication) {
            jwt("auth-jwt") {
                verifier(JWT.require(Algorithm.HMAC256(Config.jwtSecret)).build())
                validate { credential -> JWTPrincipal(credential.payload) }
                challenge { _, _ ->
                    call.respond(HttpStatusCode.Unauthorized, "Token is not valid or has expired")
                }
            }
            jwt("auth-admin") {
                verifier(JWT.require(Algorithm.HMAC256(Config.jwtSecret)).build())
                validate { credential ->
                    println("Role: ${credential.payload.getClaim("userRole").asString()}")
                    if (credential.payload.getClaim("userRole").asString() == "Admin") {
                        JWTPrincipal(credential.payload)
                    } else null
                }
                challenge { _, _ ->
                    call.respond(HttpStatusCode.Unauthorized, "Token is not valid or has expired")
                }
            }
        }
        routing()
    }

    fun start() {
        server.start(wait = true)
    }
}
