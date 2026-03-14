package infrastructure

import domain.InternalErrorException
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.request
import io.ktor.http.isSuccess
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

abstract class AbstractExternalServiceAdapter {
    protected companion object {
        const val EXTERNAL_SERVICE_ERROR_MESSAGE = "Can't contact external service"
    }

    protected val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                isLenient = true
                ignoreUnknownKeys = true
            })
        }
    }

    protected fun HttpResponse.checkStatus(): HttpResponse =
        this.also {
            val url = "${request.url.protocol.name}://${request.url.host}${request.url.encodedPath}"
            if (!status.isSuccess()) {
                println("Can't contact '$url': ${status.description}")
                throw InternalErrorException(EXTERNAL_SERVICE_ERROR_MESSAGE)
            }
        }
}