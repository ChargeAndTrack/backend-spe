package user

import infrastructure.user.LoginRequestDTO
import infrastructure.user.LoginResponseDTO
import infrastructure.user.UserDTO
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.ktor.client.*
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json

const val BASE_URL = "http://localhost:3000/api/v1"

val adminLoginDTO = LoginRequestDTO("admin", "admin1234")
val userLoginDTO = LoginRequestDTO("user1", "user11234")

val header: (String) -> HttpRequestBuilder.() -> Unit = { token ->
    {
        header("Authorization", "Bearer $token")
        contentType(ContentType.Application.Json)
    }
}

fun createClient(): HttpClient = HttpClient(CIO) {
    install(ContentNegotiation) {
        json(Json {
            prettyPrint = true
            isLenient = true
            ignoreUnknownKeys = true
        })
    }
}

suspend fun loginAndGetToken(client: HttpClient, loginDTO: LoginRequestDTO): String {
    val responseBody: LoginResponseDTO = client.post("$BASE_URL/login") {
        contentType(ContentType.Application.Json)
        setBody(loginDTO)
    }.body()
    return responseBody.token
}

class UserTest : FunSpec({
    lateinit var client: HttpClient
    lateinit var token: String

    beforeSpec {
        client = createClient()
        token = runBlocking { loginAndGetToken(client, adminLoginDTO) }
        token.isNotEmpty() shouldBe true
    }

    afterSpec {
        client.close()
    }

    test("it should get the correct user") {
        val response = client.get("$BASE_URL/user") { header(token)() }
        response.status shouldBe HttpStatusCode.OK
        val body: UserDTO = response.body()
        body.username shouldBe adminLoginDTO.username
        body.password shouldBe adminLoginDTO.password
    }

    test("it should fail to get the user without a valid token") {
        val response = client.get("$BASE_URL/user") {}
        response.status shouldBe HttpStatusCode.Unauthorized
    }
})