package charging_station

import Setup.BASE_URL
import Setup.buildRequest
import Setup.createClient
import Setup.loginAndGetToken
import Setup.userLoginDTO
import infrastructure.Router.assemblePath
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

    test("it should get the coordinates corresponding to the given address") {
        val response = client.get(LOCATION_URL.assemblePath("resolve")) {
            buildRequest<Unit>(token, parametersMap = mapOf("q" to "Via dell'Università, Cesena"))
        }
        response.status shouldBe HttpStatusCode.OK
        val location: LocationDTO = response.body()
        location.longitude shouldBe 12.236007
        location.latitude shouldBe 44.1475459
    }
})