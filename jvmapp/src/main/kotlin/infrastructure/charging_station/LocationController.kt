package infrastructure.charging_station

import application.charging_station.LocationService
import application.charging_station.LocationServiceImpl
import domain.InvalidInputException
import domain.charging_station.Location
import domain.charging_station.LocationImpl
import io.ktor.http.HttpStatusCode
import io.ktor.http.Parameters
import io.ktor.server.application.ApplicationCall
import io.ktor.server.response.respond

class LocationController {
    private val locationService: LocationService = LocationServiceImpl(NominatimGeocodingAdapter())

    suspend fun resolveAddressToLocationCoordinates(call: ApplicationCall) {
        println("resolve")
        val address: String = call.request.queryParameters.getSafely("q", "Address")
        call.respond(HttpStatusCode.OK, locationService.resolveAddressToLocationCoordinates(address).toDTO())
    }

    suspend fun reverseLocationCoordinatesToAddress(call: ApplicationCall) {
        println("reverse")
        call.respond(HttpStatusCode.OK, locationService.reverseLocationCoordinatesToAddress(getLocation(call)).toDTO())
    }

    private fun Parameters.getSafely(parameter: String, name: String): String =
        this[parameter] ?: throw InvalidInputException("$name not provided")

    private fun getLocation(call: ApplicationCall): Location =
        try {
            LocationImpl(
                call.request.queryParameters.getSafely("lng", "Longitude").toDouble(),
                call.request.queryParameters.getSafely("lat", "Latitude").toDouble()
            )
        } catch (_: NumberFormatException) {
            throw InvalidInputException("Provided longitude and latitude must be numbers")
        }
}