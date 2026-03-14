import infrastructure.Router.assemblePath
import infrastructure.charging_station.AddChargingStationDTO
import infrastructure.charging_station.ChargingStationDTO
import infrastructure.user.LoginRequestDTO
import infrastructure.user.LoginResponseDTO
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
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

object Setup {
    const val BASE_URL = "http://localhost:3000/api/v1"

    val adminLoginDTO = LoginRequestDTO("admin", "admin1234")
    val userLoginDTO = LoginRequestDTO("user1", "user11234")

    fun createClient(): HttpClient = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                isLenient = true
                ignoreUnknownKeys = true
            })
        }
    }

    fun <T> HttpRequestBuilder.buildRequest(
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

    suspend fun loginAndGetToken(client: HttpClient, loginDTO: LoginRequestDTO): String =
        client.post(BASE_URL.assemblePath("login")) { buildRequest(token = null, body = loginDTO) }
            .body<LoginResponseDTO>()
            .token

    fun chargingStationPath(vararg paths: String) = BASE_URL.assemblePath("charging-stations", *paths)

    suspend fun deleteAllChargingStations(client: HttpClient, token: String) {
        val chargingStations = client.get(chargingStationPath()) { buildRequest<Unit>(token) }
            .body<Collection<ChargingStationDTO>>()
        chargingStations.forEach {
            client.delete(chargingStationPath(it._id)) { buildRequest<Unit>(token) }
        }
    }

    suspend fun insertChargingStation(
        client: HttpClient,
        token: String,
        chargingStationToAdd: AddChargingStationDTO
    ): HttpResponse =
        client.post(chargingStationPath()) { buildRequest(token, chargingStationToAdd) }
}