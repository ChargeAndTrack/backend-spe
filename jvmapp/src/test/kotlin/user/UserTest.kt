package user

import Setup.BASE_URL
import Setup.adminLoginDTO
import Setup.buildRequest
import Setup.createClient
import Setup.loginAndGetToken
import infrastructure.user.UserDTO
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.ktor.client.*
import io.ktor.client.call.body
import io.ktor.client.request.*
import io.ktor.http.*

class UserTest : FunSpec({
    lateinit var client: HttpClient
    lateinit var token: String

    beforeSpec {
        client = createClient()
        token = loginAndGetToken(client, adminLoginDTO)
        token.isNotEmpty() shouldBe true
    }

    afterSpec {
        client.close()
    }

    test("it should get the correct user") {
        val response = client.get("$BASE_URL/user") { buildRequest<Unit>(token) }
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