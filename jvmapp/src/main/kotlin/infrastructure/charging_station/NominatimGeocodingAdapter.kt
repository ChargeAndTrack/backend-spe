package infrastructure.charging_station

import application.charging_station.GeocodingPort
import domain.InternalErrorException
import domain.NotFoundException
import domain.charging_station.Address
import domain.charging_station.Location
import domain.charging_station.LocationImpl
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.parameter
import io.ktor.http.HttpHeaders
import io.ktor.http.isSuccess
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

class NominatimGeocodingAdapter : GeocodingPort {

    private companion object {
        const val BASE_URL: String = "https://nominatim.openstreetmap.org/"
        const val USER_AGENT: String = "ChargeAndTrack/1.0"
        const val COORDINATES_NOT_FOUND_MESSAGE: String = "Couldn't find coordinates matching the specified address"
    }

    @Serializable
    private data class NominatimSearchResponse(val lat: String, val lon: String) {
        fun toLocation(): Location = LocationImpl(lon.toDouble(), lat.toDouble())
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

    override suspend fun geocode(address: String): Location {
        val response = client.get("$BASE_URL/search") {
            parameter("q", address)
            parameter("format", "json")
            parameter("limit", 1)
            header(HttpHeaders.UserAgent, USER_AGENT)
        }
        if (!response.status.isSuccess()) {
            println("Can't contact '$BASE_URL/search': ${response.status.description}")
            throw InternalErrorException("Can't contact external service")
        }
        return response.body<List<NominatimSearchResponse>>()
            .ifEmpty { throw NotFoundException(COORDINATES_NOT_FOUND_MESSAGE) }
            .first()
            .toLocation()
    }

    override suspend fun reverseGeocode(location: Location): Address {
        TODO("Not yet implemented")
    }
}