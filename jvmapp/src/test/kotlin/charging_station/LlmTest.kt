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
import io.kotest.matchers.ints.shouldBeAtLeast
import io.kotest.matchers.shouldBe
import io.ktor.client.*
import io.ktor.client.call.body
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.request.*
import io.ktor.client.statement.HttpResponse
import io.ktor.http.*

class LlmTest : FunSpec() {
    private companion object {
        const val LLM_SEARCH_URL = "$BASE_URL/llm/search"
        const val REQUEST_TIMEOUT_MILLIS: Long = 60_000
    }

    private lateinit var client: HttpClient
    private lateinit var adminToken: String
    private lateinit var userToken: String

    private val chargingStation1 = AddChargingStationDTO(110, LocationDTO(longitude = 12.236000, latitude = 44.1475470))
    private val chargingStation2 = AddChargingStationDTO(50, LocationDTO(longitude = 12.236008, latitude = 44.1475460))
    private val chargingStation3 = AddChargingStationDTO(22, LocationDTO(longitude = 12.3, latitude = 44.2))

    init {
        beforeSpec {
            client = createClient().config { install(HttpTimeout) { requestTimeoutMillis = REQUEST_TIMEOUT_MILLIS } }
            adminToken = loginAndGetToken(client, adminLoginDTO)
            adminToken.isNotEmpty() shouldBe true
            userToken = loginAndGetToken(client, userLoginDTO)
            userToken.isNotEmpty() shouldBe true
            deleteAllChargingStations(client, adminToken)
            insertChargingStations()
        }

        afterSpec { client.close() }

        context("search without filters") {
            test("it should return the nearest charging stations") {
                val response = makeRequest("Find charging stations near Via dell'Università in Cesena")
                response.status shouldBe HttpStatusCode.OK
                val chargingStations: Collection<ChargingStationDTO> = response.body()
                chargingStations.size shouldBe 2
            }

            test("it should return the closest charging station") {
                val response = makeRequest("Find the closest charging station to Via dell'Università in Cesena")
                response.status shouldBe HttpStatusCode.OK
                val chargingStations: Collection<ChargingStationDTO> = response.body()
                chargingStations.size shouldBe 1
                chargingStations.first().location shouldBe chargingStation2.location
            }
        }

        context("search with filters") {
            val minPower = 100

            test("it should return the nearest charging stations filtered by minimum power") {
                val response = makeRequest(
                    "Find charging stations near Via dell'Università in Cesena with power greater than $minPower kW"
                )
                response.status shouldBe HttpStatusCode.OK
                val chargingStations: Collection<ChargingStationDTO> = response.body()
                chargingStations.forEach { it.power shouldBeAtLeast minPower }
            }

            test("it should return the closest charging station filtered by minimum power") {
                val response = makeRequest(
                    "Find the closest charging station to Via dell'Università in Cesena with power greater than"
                            + "$minPower kW"
                )
                response.status shouldBe HttpStatusCode.OK
                val chargingStations: Collection<ChargingStationDTO> = response.body()
                chargingStations.size shouldBe 1
                chargingStations.first().power shouldBeAtLeast minPower
            }
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