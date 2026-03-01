package charging_station

import infrastructure.Router.assemblePath
import infrastructure.charging_station.AddChargingStationDTO
import infrastructure.charging_station.ChargingStationDTO
import infrastructure.charging_station.ClosestChargingStationDTO
import infrastructure.charging_station.LocationDTO
import infrastructure.charging_station.NearbyChargingStationsDTO
import infrastructure.charging_station.UpdateChargingStationDTO
import infrastructure.user.LoginRequestDTO
import infrastructure.user.LoginResponseDTO
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.equals.shouldBeEqual
import io.kotest.matchers.shouldBe
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

const val BASE_PATH = "api/v1"
val BASE_URL = "http://localhost:3000".assemblePath(BASE_PATH)

class ChargingStationTest : FunSpec({

    lateinit var client: HttpClient
    lateinit var token: String
    lateinit var completeChargingStation1: ChargingStationDTO
    lateinit var completeChargingStation2: ChargingStationDTO

    val chargingStation1 = AddChargingStationDTO(
        power = 130,
        location = LocationDTO(longitude = 50.44, latitude = 37.12)
    )
    val chargingStation2 = AddChargingStationDTO(
        power = 80,
        location = LocationDTO(longitude = 50.45, latitude = 37.15)
    )

    beforeSpec {
        client = HttpClient(CIO) {
            install(ContentNegotiation) {
                json(Json {
                    prettyPrint = true
                    isLenient = true
                    ignoreUnknownKeys = true
                })
            }
        }

        token = client
            .post(BASE_URL.assemblePath("login")) {
                buildRequest(token = null, body = LoginRequestDTO(username = "admin", password = "admin1234"))
            }.body< LoginResponseDTO>()
            .token
        token.isNotEmpty() shouldBe true
        completeChargingStation1 = client.post(chargingStationPath()) {
            buildRequest(token, chargingStation1)
        }.body<ChargingStationDTO>()
        completeChargingStation2 = client.post(chargingStationPath()) {
            buildRequest(token, chargingStation2)
        }.body<ChargingStationDTO>()
    }

    afterSpec {
        client.close()
    }

    test("get nearby charging stations test") {
        val response = client.get(chargingStationPath("near")) {
            buildRequest<Unit>(token, parametersMap = mapOf(
                "lng" to 50.44.toString(),
                "lat" to 37.12.toString(),
                "radius" to 50_000.0.toString()
            ))
        }
        response.status shouldBeEqual HttpStatusCode.OK
        response.body<Collection<ChargingStationDTO>>() shouldContainExactly listOf(
            completeChargingStation1,
            completeChargingStation2
        )
    }

    test("get closest charging station test") {
        client.put(chargingStationPath(completeChargingStation1._id ?: "")) {
            buildRequest(
                token,
                UpdateChargingStationDTO(
                    power = null,
                    enabled = false,
                    available = null,
                    location = null
                )
            )
        }
        val response = client.get(chargingStationPath("closest")) {
            buildRequest<Unit>(token, parametersMap = mapOf(
                "lng" to 50.44.toString(),
                "lat" to 37.00.toString(),
                "onlyEnabledAndAvailable" to true.toString()
            ))
        }
        response.status shouldBeEqual HttpStatusCode.OK
        response.body<ChargingStationDTO>() shouldBeEqual completeChargingStation2
    }

    test("add charging station test") {
        val response = client.post(chargingStationPath()) {
            buildRequest(token, chargingStation1)
        }
        val responseBody = response.body<ChargingStationDTO>()
        completeChargingStation1 = responseBody
        response.status shouldBeEqual HttpStatusCode.Created
        responseBody.power shouldBeEqual chargingStation1.power
        responseBody.location shouldBeEqual chargingStation1.location
    }

    test("list all charging stations test") {
        val response = client.get(chargingStationPath()) {
            buildRequest<Unit>(token)
        }
        response.status shouldBeEqual HttpStatusCode.OK
        response
            .body<Collection<ChargingStationDTO>>()
            .find { it._id == completeChargingStation1._id } shouldBe completeChargingStation1
    }

    test("get charging station by id test") {
        val response = client.get(chargingStationPath(completeChargingStation1._id ?: "")) {
            buildRequest<Unit>(token)
        }
        val wrongIdResponse = client.get(chargingStationPath("")) {
            buildRequest<Unit>(token)
        }
        val responseBody = response.body<ChargingStationDTO>()
        response.status shouldBeEqual HttpStatusCode.OK
        completeChargingStation1 shouldBeEqual responseBody
        wrongIdResponse.status shouldBeEqual HttpStatusCode.NotFound
    }

    test("update charging station by id test") {
        val updateChargingStation = UpdateChargingStationDTO(
            power = 150,
            available = false,
            enabled = null,
            location = null
        )
        val response = client.put(chargingStationPath(completeChargingStation1._id ?: "")) {
            buildRequest(token, updateChargingStation)
        }
        val expectedResult = ChargingStationDTO(
            _id = completeChargingStation1._id,
            power = updateChargingStation.power ?: completeChargingStation1.power,
            available = updateChargingStation.available ?: completeChargingStation1.available,
            enabled = updateChargingStation.enabled ?: completeChargingStation1.enabled,
            location = updateChargingStation.location ?: completeChargingStation1.location
        )
        response.status shouldBeEqual HttpStatusCode.OK
        response.body<ChargingStationDTO>() shouldBeEqual expectedResult
    }

    test("delete charging station by id test") {
        val response = client.delete(chargingStationPath(completeChargingStation1._id ?: "")) {
            buildRequest<Unit>(token)
        }
        val wrongResponse = client.delete(chargingStationPath("")) {
            buildRequest<Unit>(token)
        }
        response.status shouldBeEqual HttpStatusCode.OK
        wrongResponse.status shouldBeEqual HttpStatusCode.NotFound
    }
})

private fun <T> HttpRequestBuilder.buildRequest(
    token: String?,
    body: T? = null,
    parametersMap: Map<String, String>? = null
) {
    if (token != null) headers { bearerAuth(token) }
    parametersMap?.map { (key, value) -> parameter(key, value) }
    body?.also {
        contentType(ContentType.Application.Json)
        setBody(it)
    }
}

private fun chargingStationPath(vararg paths: String) = BASE_URL.assemblePath("charging-stations", *paths)
