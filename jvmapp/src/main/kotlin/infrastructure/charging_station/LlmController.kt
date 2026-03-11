package infrastructure.charging_station

import application.charging_station.SearchChargingStationsService
import application.charging_station.SearchChargingStationsServiceImpl
import domain.InvalidInputException
import io.ktor.http.HttpStatusCode
import io.ktor.http.Parameters
import io.ktor.server.application.ApplicationCall
import io.ktor.server.response.respond

object LlmController {
    private val searchService: SearchChargingStationsService =
        SearchChargingStationsServiceImpl(HuggingFaceQueryParsingAdapter())

    suspend fun search(call: ApplicationCall) {
        println("LLM search")
        val query = call.request.queryParameters.getSafely("q", "Query")
        call.respond(HttpStatusCode.OK, searchService.search(query).toDTO())
    }

    private fun Parameters.getSafely(parameter: String, name: String): String =
        this[parameter] ?: throw InvalidInputException("$name not provided")
}