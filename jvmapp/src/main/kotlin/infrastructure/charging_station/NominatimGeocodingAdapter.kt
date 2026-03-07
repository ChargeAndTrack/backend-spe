package infrastructure.charging_station

import application.charging_station.GeocodingPort
import domain.InternalErrorException
import domain.InvalidInputException
import domain.NotFoundException
import domain.charging_station.Address
import domain.charging_station.AddressImpl
import domain.charging_station.Location
import domain.charging_station.LocationImpl
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.*
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpHeaders
import io.ktor.http.isSuccess
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

class NominatimGeocodingAdapter : GeocodingPort {

    private companion object {
        const val BASE_URL: String = "https://nominatim.openstreetmap.org/"
        const val USER_AGENT: String = "ChargeAndTrack/1.0"
        const val EXTERNAL_SERVICE_ERROR_MESSAGE: String = "Can't contact external service"
        const val COORDINATES_NOT_FOUND_MESSAGE: String = "No coordinates found for the specified address"
        const val LOCATION_NOT_FOUND_MESSAGE: String = "No location information found"
        const val OPERATION_FAILED_MESSAGE = "An unexpected error occurred while performing the operation"
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

    private val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                isLenient = true
                ignoreUnknownKeys = true
            })
        }
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

    private fun HttpResponse.checkStatus(): HttpResponse =
        this.also {
            if (!status.isSuccess()) {
                println("Can't contact '$BASE_URL/search': ${status.description}")
                throw InternalErrorException(EXTERNAL_SERVICE_ERROR_MESSAGE)
            }
        }

    private suspend fun <T> execute(block: suspend () -> T): T =
        try {
            block()
        } catch (e: InvalidInputException) {
            println("NominatimGeocodingAdapter error: ${e.message}")
            throw InternalErrorException(EXTERNAL_SERVICE_ERROR_MESSAGE)
        } catch (e: Throwable) {
            println("NominatimGeocodingAdapter error: ${e.message}")
            throw InternalErrorException(OPERATION_FAILED_MESSAGE)
        }
}