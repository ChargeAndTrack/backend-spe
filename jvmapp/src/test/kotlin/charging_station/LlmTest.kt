package charging_station

import Setup.adminLoginDTO
import Setup.BASE_URL
import Setup.buildRequest
import Setup.createClient
import Setup.deleteAllChargingStations
import Setup.insertChargingStation
import Setup.loginAndGetToken
import Setup.userLoginDTO
import infrastructure.charging_station.AddChargingStationDTO
import infrastructure.charging_station.ChargingStationDTO
import infrastructure.charging_station.LocationDTO
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.ktor.client.*
import io.ktor.client.call.body
import io.ktor.client.request.*
import io.ktor.client.statement.HttpResponse
import io.ktor.http.*

class LlmTest : FunSpec() {
    private companion object {
        const val LLM_SEARCH_URL = "$BASE_URL/llm/search"
    }

    private lateinit var client: HttpClient
    private lateinit var adminToken: String
    private lateinit var userToken: String

    private val chargingStation1 = AddChargingStationDTO(110, LocationDTO(longitude = 12.236000, latitude = 44.1475470))
    private val chargingStation2 = AddChargingStationDTO(50, LocationDTO(longitude = 12.236008, latitude = 44.1475460))
    private val chargingStation3 = AddChargingStationDTO(22, LocationDTO(longitude = 12.3, latitude = 44.2))

    init {
        beforeSpec {
            client = createClient()
            adminToken = loginAndGetToken(client, adminLoginDTO)
            adminToken.isNotEmpty() shouldBe true
            userToken = loginAndGetToken(client, userLoginDTO)
            userToken.isNotEmpty() shouldBe true
            deleteAllChargingStations(client, adminToken)
            insertChargingStations()
        }

        afterSpec { client.close() }

        test("it should get the correct nearest charging stations (no filters)") {
            val response = makeRequest("Find charging stations near Via dell'Università in Cesena")
            response.status shouldBe HttpStatusCode.OK
            val chargingStations: Collection<ChargingStationDTO> = response.body()
            println("chargingStations: $chargingStations")
            chargingStations.size shouldBe 2
        }
    }

    private suspend fun makeRequest(query: String): HttpResponse =
        client.post(LLM_SEARCH_URL) { buildRequest<Unit>(userToken, parametersMap = mapOf("q" to query)) }

    private suspend fun insertChargingStations() {
        insertChargingStation(client, adminToken, chargingStation1)
        insertChargingStation(client, adminToken, chargingStation2)
        insertChargingStation(client, adminToken, chargingStation3)
    }
}