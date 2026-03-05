package infrastructure.charging_station

import application.charging_station.LocationService
import application.charging_station.LocationServiceImpl
import domain.InvalidInputException
import io.ktor.http.HttpStatusCode
import io.ktor.http.Parameters
import io.ktor.server.application.ApplicationCall
import io.ktor.server.response.respond

object LocationController {
    private val locationService: LocationService = LocationServiceImpl(NominatimGeocodingAdapter())

    suspend fun resolveAddressToLocationCoordinates(call: ApplicationCall) {
        println("resolve")
        val address: String = call.request.queryParameters.getSafely("q", "Address")
        call.respond(HttpStatusCode.OK, locationService.resolveAddressToLocationCoordinates(address).toDTO())
    }

    suspend fun reverseLocationCoordinatesToAddress(call: ApplicationCall) {

    }

    private fun Parameters.getSafely(parameter: String, name: String) =
        this[parameter] ?: throw InvalidInputException("$name not provided")
}