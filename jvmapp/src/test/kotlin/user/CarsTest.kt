package user

import infrastructure.user.AddCarDTO
import infrastructure.user.CarDTO
import infrastructure.user.UpdateCarDTO
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.ktor.client.*
import io.ktor.client.call.body
import io.ktor.client.request.*
import io.ktor.client.statement.HttpResponse
import io.ktor.http.*

class CarsTest : FunSpec() {
    private val CARS_URL = "$BASE_URL/cars"
    private val carUrl = { id: String -> "$CARS_URL/$id" }

    private lateinit var client: HttpClient
    private lateinit var token: String

    private val addCar1Body = AddCarDTO("AB123YZ", 20)
    private val addCar2Body = AddCarDTO("CD123YZ", 40)
    private val addCar3Body = AddCarDTO("EF123YZ", 75)

    init {
        beforeSpec {
            client = createClient()
            token = loginAndGetToken(client, userLoginDTO)
            token.isNotEmpty() shouldBe true
        }

        afterSpec { client.close() }

        beforeEach { deleteAllUserCars() }

        test("it should get the user cars successfully") {
            insertCars()
            val response = client.get(CARS_URL) { header(token)() }
            response.status shouldBe HttpStatusCode.OK
            val cars: Collection<CarDTO> = response.body()
            cars.size shouldBe 3
        }

        context("add car tests") {
            beforeEach { insertCars() }

            test("it should add a new car successfully") {
                val newCarDTO = AddCarDTO("GH123YZ", 100)
                val response = insertCar(newCarDTO)
                response.status shouldBe HttpStatusCode.Created
                val insertedCar: CarDTO = response.body()
                insertedCar.plate shouldBe newCarDTO.plate
                insertedCar.maxBattery shouldBe newCarDTO.maxBattery
            }

            test("it should fail to add a new car when a plate which already exists is passed") {
                insertCar(AddCarDTO(addCar1Body.plate, 50)).status shouldBe HttpStatusCode.BadRequest
            }

            test("it should fail to add a new car when an invalid plate is passed") {
                insertCar(AddCarDTO("A1", 50)).status shouldBe HttpStatusCode.BadRequest
            }

            test("it should fail to add a new car when an invalid max battery value is passed") {
                insertCar(AddCarDTO("GH123YZ", -10)).status shouldBe HttpStatusCode.BadRequest
            }
        }

        context("get car tests") {
            test("it should get the requested existing car") {
                val insertedCar: CarDTO = insertCar(addCar1Body).body()
                val getResponse = client.get(carUrl(insertedCar._id)) { header(token)() }
                getResponse.status shouldBe HttpStatusCode.OK
                val car: CarDTO = getResponse.body()
                car.plate shouldBe addCar1Body.plate
                car.maxBattery shouldBe addCar1Body.maxBattery
            }

            test("it should fail to get the requested non-existent car") {
                val insertedCar: CarDTO = insertCar(addCar1Body).body()
                deleteAllUserCars()
                val getResponse = client.get(carUrl(insertedCar._id)) { header(token)() }
                getResponse.status shouldBe HttpStatusCode.NotFound
            }
        }

        context("update car tests") {
            lateinit var insertedCar1: CarDTO
            lateinit var insertedCar2: CarDTO

            beforeEach {
                insertedCar1 = insertCar(addCar1Body).body()
                insertedCar2 = insertCar(addCar2Body).body()
            }

            test("it should update an existing car successfully") {
                val updateCarBody = UpdateCarDTO(maxBattery = 50)
                val putResponse = updateCar(insertedCar1._id, updateCarBody)
                putResponse.status shouldBe HttpStatusCode.OK
                val car: CarDTO = putResponse.body()
                car.maxBattery shouldBe updateCarBody.maxBattery
            }

            test("it should fail to update an existing car when a plate which already exists is passed") {
                updateCar(insertedCar1._id, UpdateCarDTO(plate = insertedCar2.plate))
                    .status shouldBe HttpStatusCode.BadRequest
            }

            test("it should fail to update an existing car when an invalid plate is passed") {
                updateCar(insertedCar1._id, UpdateCarDTO(plate = "A1")).status shouldBe HttpStatusCode.BadRequest
            }

            test("it should fail to update an existing car when an invalid max battery value is passed") {
                updateCar(insertedCar1._id, UpdateCarDTO(maxBattery = -5)).status shouldBe HttpStatusCode.BadRequest
            }

            test("it should fail to update an existing car when an invalid current battery value is passed") {
                updateCar(insertedCar1._id, UpdateCarDTO(currentBattery = 200))
                    .status shouldBe HttpStatusCode.BadRequest
            }
        }

        test("it should delete an existing car successfully") {
            val insertedCar: CarDTO = insertCar(addCar1Body).body()
            val deleteResponse = client.delete(carUrl(insertedCar._id)) { header(token)() }
            deleteResponse.status shouldBe HttpStatusCode.OK
            val cars: Collection<CarDTO> = deleteResponse.body()
            cars.isEmpty() shouldBe true
        }
    }

    private suspend fun deleteAllUserCars() {
        val cars: Collection<CarDTO> = client.get(CARS_URL) { header(token)() }.body()
        cars.forEach { client.delete(carUrl(it._id)) { header(token)() } }
    }

    private suspend fun insertCar(body: AddCarDTO): HttpResponse =
        client.post(CARS_URL) {
            header(token)()
            setBody(body)
        }

    private suspend fun insertCars() {
        insertCar(addCar1Body)
        insertCar(addCar2Body)
        insertCar(addCar3Body)
    }

    private suspend fun updateCar(carId: String, updateCarBody: UpdateCarDTO): HttpResponse =
        client.put(carUrl(carId)) {
            header(token)()
            setBody(updateCarBody)
        }
}