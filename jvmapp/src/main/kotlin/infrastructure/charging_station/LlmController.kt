package infrastructure.charging_station

import application.charging_station.SearchChargingStationsService
import domain.InvalidInputException
import io.ktor.http.HttpStatusCode
import io.ktor.http.Parameters
import io.ktor.server.application.ApplicationCall
import io.ktor.server.response.respond

class LlmController(private val searchService: SearchChargingStationsService) {

    suspend fun search(call: ApplicationCall) {
        println("LLM search")
        val query = call.request.queryParameters.getSafely("q", "Query")
        call.respond(HttpStatusCode.OK, searchService.search(query).toDTO())
    }

    private fun Parameters.getSafely(parameter: String, name: String): String =
        this[parameter] ?: throw InvalidInputException("$name not provided")
}