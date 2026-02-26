package infrastructure

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.typesafe.config.ConfigFactory
import domain.AppException
import domain.InternalErrorException
import domain.InvalidInputException
import domain.NotFoundException
import domain.user.Role
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.application.*
import io.ktor.server.auth.Authentication
import io.ktor.server.auth.jwt.JWTAuthenticationProvider
import io.ktor.server.auth.jwt.JWTCredential
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.jwt.jwt
import io.ktor.server.config.HoconApplicationConfig
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.statuspages.StatusPages
import io.ktor.server.response.respond
import kotlinx.serialization.json.Json

class Server(routing: Application.() -> Unit) {
    init {
        loadConfiguration()
    }

    private val server = embeddedServer(Netty, host = Config.host ?: "0.0.0.0", port = Config.port ?: 3000) {
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                isLenient = true
                ignoreUnknownKeys = true
            })
        }
        install(Authentication) {
            jwt("auth-jwt") {
                configuration("Token is not valid or has expired") { JWTPrincipal(it.payload) }
            }
            jwt("auth-admin") {
                configuration("User unauthorized or the token is not valid/expired") {
                    if (it.payload.getClaim("userRole").asString() == Role.ADMIN.displayName) {
                        JWTPrincipal(it.payload)
                    } else {
                        null
                    }
                }
            }
        }
        install(StatusPages) {
            exception<AppException> { call, cause -> call.respond(cause.statusCode(), cause.message) }
        }
        routing()
    }

    private fun loadConfiguration() {
        val config = HoconApplicationConfig(ConfigFactory.load())
        Config.host = config.property("ktor.deployment.host").getString()
        Config.port = config.property("ktor.deployment.port").getString().toInt()
        Config.rootPath = config.property("ktor.deployment.rootPath").getString()
        Config.jwtSecret = config.property("jwt.secret").getString()
    }

    private fun JWTAuthenticationProvider.Config.configuration(message: String, validation: (JWTCredential) -> Any?) {
        verifier(JWT.require(Algorithm.HMAC256(Config.jwtSecret)).build())
        validate { validation(it) }
        challenge { _, _ -> call.respond(HttpStatusCode.Unauthorized, message) }
    }

    private fun AppException.statusCode(): HttpStatusCode =
        when(this) {
            is InvalidInputException -> HttpStatusCode.BadRequest
            is NotFoundException -> HttpStatusCode.NotFound
            is InternalErrorException -> HttpStatusCode.InternalServerError
        }

    fun start() {
        server.start(wait = true)
    }
}
