package infrastructure.charging_station

import application.charging_station.GeocodingPort
import common.Adapter
import domain.InternalErrorException
import domain.InvalidInputException
import domain.NotFoundException
import domain.charging_station.Address
import domain.charging_station.AddressImpl
import domain.charging_station.Location
import domain.charging_station.LocationImpl
import infrastructure.AbstractExternalServiceAdapter
import io.ktor.client.call.body
import io.ktor.client.request.*
import io.ktor.http.HttpHeaders
import kotlinx.serialization.Serializable

@Adapter
class NominatimGeocodingAdapter : AbstractExternalServiceAdapter(), GeocodingPort {

    private companion object {
        const val BASE_URL: String = "https://nominatim.openstreetmap.org"
        const val USER_AGENT: String = "ChargeAndTrack/1.0"

        const val COORDINATES_NOT_FOUND_MESSAGE: String = "No coordinates found for the specified address"
        const val LOCATION_NOT_FOUND_MESSAGE: String = "No location information found"
    }

    @Serializable
    private data class NominatimSearchResponse(val lat: String, val lon: String) {
        fun toLocation(): Location = LocationImpl(lon.toDouble(), lat.toDouble())
    }

    @Serializable
    private data class NominatimReverseResponse(val displayName: String? = null, val address: NominatimAddress? = null)

    @Serializable
    private data class NominatimAddress(
        val road: String? = null,
        val houseNumber: String? = null,
        val city: String? = null,
        val town: String? = null,
        val village: String? = null,
        val postcode: String? = null,
        val region: String? = null,
        val state: String? = null,
        val country: String? = null
    ) {
        fun toAddress(): Address = AddressImpl(
            street = road ?: "",
            houseNumber = houseNumber ?: "",
            city = city ?: town ?: village ?: "",
            postalCode = postcode ?: "",
            region = region ?: state ?: "",
            country = country ?: ""
        )
    }

    override suspend fun geocode(address: String): Location = execute {
        client
            .get("$BASE_URL/search") {
                nominatimDefaults()
                parameter("q", address)
                parameter("limit", 1)
            }.checkStatus()
            .body<Collection<NominatimSearchResponse>>()
            .ifEmpty { throw NotFoundException(COORDINATES_NOT_FOUND_MESSAGE) }
            .first()
            .toLocation()
    }

    override suspend fun reverseGeocode(location: Location): Address = execute {
        client
            .get("$BASE_URL/reverse") {
                nominatimDefaults()
                parameter("lat", location.latitude)
                parameter("lon", location.longitude)
            }.checkStatus()
            .body<NominatimReverseResponse>()
            .address
            ?.toAddress()
            ?: throw NotFoundException(LOCATION_NOT_FOUND_MESSAGE)
    }

    private fun HttpRequestBuilder.nominatimDefaults() {
        parameter("format", "json")
        header(HttpHeaders.UserAgent, USER_AGENT)
    }

    private suspend fun <T> execute(block: suspend () -> T): T =
        try {
            block()
        } catch (e: InvalidInputException) {
            println("NominatimGeocodingAdapter error: ${e.message}")
            throw InternalErrorException(EXTERNAL_SERVICE_ERROR_MESSAGE)
        } catch (e: Exception) {
            println("NominatimGeocodingAdapter unexpected error: ${e.message}")
            throw InternalErrorException()
        }
}