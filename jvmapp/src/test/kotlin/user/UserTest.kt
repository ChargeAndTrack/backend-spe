package user

import infrastructure.user.LoginRequestDTO
import infrastructure.user.LoginResponseDTO
import infrastructure.user.UserDTO
import io.ktor.client.*
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import kotlin.test.*

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

class UserTest {
    private lateinit var client: HttpClient
    private lateinit var token: String

    @BeforeTest
    fun setup() {
        client = createClient()
        token = runBlocking { loginAndGetToken(client, adminLoginDTO) }
        assertTrue(token.isNotEmpty())
    }

    @AfterTest
    fun teardown() {
        client.close()
    }

    @Test
    fun `it should get the correct user`() = runBlocking {
        val response = client.get("$BASE_URL/user") { header(token)() }
        assertEquals(HttpStatusCode.OK, response.status)
        val body: UserDTO = response.body()
        assertEquals(adminLoginDTO.username, body.username)
        assertEquals(adminLoginDTO.password, body.password)
    }

    @Test
    fun `it should fail to get the user without a valid token`() = runBlocking {
        val response = client.get("$BASE_URL/user") {}
        assertEquals(HttpStatusCode.Unauthorized, response.status)
    }
}