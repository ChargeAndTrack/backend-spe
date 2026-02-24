package charging_station

import infrastructure.Router.assemblePath
import infrastructure.charging_station.AddChargingStationDTO
import infrastructure.charging_station.ChargingStationDTO
import infrastructure.charging_station.LocationDTO
import infrastructure.charging_station.UpdateChargingStationDTO
import infrastructure.user.LoginRequestDTO
import infrastructure.user.LoginResponseDTO
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import kotlin.test.*

class ChargingStationTest {

    private val BASE_PATH = "/api/v1"
    private val BASE_URL = "http://localhost:3000".assemblePath(BASE_PATH)
    private val CHARGING_STATION_PATH = "/charging-stations"

    private lateinit var client: HttpClient
    private lateinit var token: String
    private lateinit var completeChargingStation: ChargingStationDTO

    private val chargingStation = AddChargingStationDTO(
        power = 130,
        location = LocationDTO(longitude = 50.44, latitude = 37.12)
    )

    @BeforeTest
    fun setup() {
        client = HttpClient(CIO) {
            install(ContentNegotiation) {
                json(Json {
                    prettyPrint = true
                    isLenient = true
                    ignoreUnknownKeys = true
                })
            }
        }
        runBlocking {
            val loginResponse: LoginResponseDTO = client.post(BASE_URL.assemblePath("/login")) {
                contentType(ContentType.Application.Json)
                setBody(LoginRequestDTO(username = "admin", password = "admin1234"))
            }.body()

            token = loginResponse.token
            assertTrue(token.isNotEmpty())
        }
        runBlocking {
            completeChargingStation = client.post(BASE_URL.assemblePath(CHARGING_STATION_PATH)) {
                contentType(ContentType.Application.Json)
                headers { bearerAuth(token) }
                setBody(chargingStation)
            }.body<ChargingStationDTO>()
        }
    }

    @AfterTest
    fun teardown() {
        client.close()
    }

    @Test
    fun `add charging station test`() = runBlocking {
        val response = client.post(BASE_URL.assemblePath(CHARGING_STATION_PATH)) {
            contentType(ContentType.Application.Json)
            headers { bearerAuth(token) }
            setBody(chargingStation)
        }
        val responseBody = response.body<ChargingStationDTO>()
        completeChargingStation = responseBody
        assertEquals(HttpStatusCode.Created, response.status)
        assertEquals(chargingStation.power, responseBody.power)
        assertEquals(chargingStation.location, responseBody.location)
    }

    @Test
    fun `list all charging stations test`() = runBlocking {
        val response = client.get(BASE_URL.assemblePath(CHARGING_STATION_PATH)) {
            headers { bearerAuth(token) }
        }
        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals(
            completeChargingStation,
            response.body<Collection<ChargingStationDTO>>().find{ it._id == completeChargingStation._id }
        )
    }

    @Test
    fun `get charging station by id test`() = runBlocking {
        val response = client.get(BASE_URL.assemblePath(CHARGING_STATION_PATH, "/${completeChargingStation._id}")) {
            headers { bearerAuth(token) }
        }
        val wrongIdResponse = client.get(BASE_URL.assemblePath(CHARGING_STATION_PATH, "/")) {
            headers { bearerAuth(token) }
        }
        val responseBody = response.body<ChargingStationDTO>()
        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals(responseBody, completeChargingStation)
        assertEquals(HttpStatusCode.NotFound, wrongIdResponse.status)
    }

    @Test
    fun `update charging station by id test`() = runBlocking {
        val updateChargingStation = UpdateChargingStationDTO(
            power = 150,
            available = false,
            enabled = null,
            location = null
        )
        val response = client.put(BASE_URL.assemblePath(CHARGING_STATION_PATH, "/${completeChargingStation._id}")) {
            headers { bearerAuth(token) }
            contentType(ContentType.Application.Json)
            setBody(updateChargingStation)
        }
        val expectedResult = ChargingStationDTO(
            _id = completeChargingStation._id,
            power = updateChargingStation.power ?: completeChargingStation.power,
            available = updateChargingStation.available ?: completeChargingStation.available,
            enabled = updateChargingStation.enabled ?: completeChargingStation.enabled,
            location = updateChargingStation.location ?: completeChargingStation.location
        )
        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals(expectedResult, response.body<ChargingStationDTO>())
    }

    @Test
    fun `delete charging station by id test`() = runBlocking {
        val response = client.delete(BASE_URL.assemblePath(CHARGING_STATION_PATH, "/${completeChargingStation._id}")) {
            headers { bearerAuth(token) }
        }
        val wrongResponse = client.delete(BASE_URL.assemblePath(CHARGING_STATION_PATH, "/")) {
            headers { bearerAuth(token) }
        }
        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals(HttpStatusCode.NotFound, wrongResponse.status)
    }
}
