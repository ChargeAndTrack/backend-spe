package charging_station

import infrastructure.Router.assemblePath
import infrastructure.charging_station.AddChargingStationDTO
import infrastructure.charging_station.ChargingStationDTO
import infrastructure.charging_station.LocationDTO
import infrastructure.user.LoginRequestDTO
import infrastructure.user.LoginResponseDTO
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.headers
import io.ktor.client.request.post
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
    }

    @AfterTest
    fun teardown() {
        client.close()
    }

    @Test
    fun `add charging station test`() = runBlocking {
        val chargingStation = AddChargingStationDTO(
            power = 130,
            location = LocationDTO(longitude = 50.44, latitude = 37.12)
        )
        val response = client.post(BASE_URL.assemblePath(CHARGING_STATION_PATH)) {
            contentType(ContentType.Application.Json)
            headers { bearerAuth(token) }
            setBody(chargingStation)
        }
        val responseBody = response.body<ChargingStationDTO>()
        assertEquals(HttpStatusCode.Created, response.status)
        assertEquals(chargingStation.power, responseBody.power)
        assertEquals(chargingStation.location, responseBody.location)
    }
}