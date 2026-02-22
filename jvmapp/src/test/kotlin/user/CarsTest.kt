package user

import infrastructure.user.AddCarDTO
import infrastructure.user.CarDTO
import io.ktor.client.*
import io.ktor.client.call.body
import io.ktor.client.request.*
import io.ktor.client.statement.HttpResponse
import io.ktor.http.*
import kotlinx.coroutines.runBlocking
import kotlin.test.*

class CarsTest {
    private lateinit var client: HttpClient
    private lateinit var token: String

    private val addCar1Body = AddCarDTO("AB123YZ", 20)
    private val addCar2Body = AddCarDTO("CD123YZ", 40)
    private val addCar3Body = AddCarDTO("EF123YZ", 75)

    @BeforeTest
    fun setup() {
        client = createClient()
        token = runBlocking { loginAndGetToken(client, adminLoginDTO) }
        assertTrue(token.isNotEmpty())
        runBlocking { deleteAllUserCars() }
    }

    @AfterTest
    fun teardown() {
        client.close()
    }

    @Test
    fun `it should get the cars correctly`() = runBlocking {
        insertCars()
        val response = client.get("$BASE_URL/cars") { header(token)() }
        assertEquals(HttpStatusCode.OK, response.status)
        val cars: Collection<CarDTO> = response.body()
        assertEquals(3, cars.size)
    }

    @Test
    fun `it should add a car correctly`() = runBlocking {
        val response = insertCar(addCar1Body)
        assertEquals(HttpStatusCode.Created, response.status)
        val insertedCar: CarDTO = response.body()
        assertEquals(addCar1Body.plate, insertedCar.plate)
        assertEquals(addCar1Body.maxBattery, insertedCar.maxBattery)
    }

    @Test
    fun `it should get the correct car`() = runBlocking {
        val insertedCar: CarDTO = insertCar(addCar1Body).body()
        val getResponse = client.get("$BASE_URL/cars/${insertedCar._id}") { header(token)() }
        assertEquals(HttpStatusCode.OK, getResponse.status)
        val car: CarDTO = getResponse.body()
        assertEquals(addCar1Body.plate, car.plate)
        assertEquals(addCar1Body.maxBattery, car.maxBattery)
    }

    @Test
    fun `it should delete a car correctly`() = runBlocking {
        val insertedCar: CarDTO = insertCar(addCar1Body).body()
        val deleteResponse = client.delete("$BASE_URL/cars/${insertedCar._id}") { header(token)() }
        assertEquals(HttpStatusCode.OK, deleteResponse.status)
        val cars: Collection<CarDTO> = deleteResponse.body()
        assertTrue(cars.isEmpty())
    }

    private suspend fun insertCar(body: AddCarDTO): HttpResponse {
        return client.post("$BASE_URL/cars") {
            header(token)()
            setBody(body)
        }
    }

    private suspend fun insertCars() {
        insertCar(addCar1Body)
        insertCar(addCar2Body)
        insertCar(addCar3Body)
    }

    private suspend fun deleteAllUserCars() {
        val cars: Collection<CarDTO> = client.get("$BASE_URL/cars") { header(token)() }.body()
        cars.forEach { client.delete("$BASE_URL/cars/${it._id}") { header(token)() } }
    }
}
