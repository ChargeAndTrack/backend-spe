package charging_station

import Setup.BASE_URL
import Setup.buildRequest
import Setup.createClient
import Setup.loginAndGetToken
import Setup.userLoginDTO
import infrastructure.Router.Companion.assemblePath
import infrastructure.charging_station.AddressDTO
import infrastructure.charging_station.LocationDTO
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.ktor.client.*
import io.ktor.client.call.body
import io.ktor.client.request.*
import io.ktor.http.*

private const val LOCATION_URL = "$BASE_URL/location"

class LocationTest : FunSpec({
    lateinit var client: HttpClient
    lateinit var token: String

    beforeSpec {
        client = createClient()
        token = loginAndGetToken(client, userLoginDTO)
        token.isNotEmpty() shouldBe true
    }

    afterSpec { client.close() }

    val place: Pair<LocationDTO, AddressDTO> = Pair(
        LocationDTO(12.236007, 44.1475459),
        AddressDTO("Via dell'Università", "", "Cesena", "", "", "Italia")
    )

    test("it should get the coordinates corresponding to the given address") {
        val response = client.get(LOCATION_URL.assemblePath("resolve")) {
            buildRequest<Unit>(token, parametersMap = mapOf("q" to "${place.second.street}, ${place.second.city}"))
        }
        response.status shouldBe HttpStatusCode.OK
        val location: LocationDTO = response.body()
        location.longitude shouldBe place.first.longitude
        location.latitude shouldBe place.first.latitude
    }

    test("it should get the address corresponding to the given coordinates") {
        val response = client.get(LOCATION_URL.assemblePath("reverse")) {
            buildRequest<Unit>(
                token,
                parametersMap = mapOf(
                    "lng" to place.first.longitude.toString(),
                    "lat" to place.first.latitude.toString()
                )
            )
        }
        response.status shouldBe HttpStatusCode.OK
        val address: AddressDTO = response.body()
        address.street shouldBe place.second.street
        address.city shouldBe place.second.city
        address.country shouldBe place.second.country
    }
})