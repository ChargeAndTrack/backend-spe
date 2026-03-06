package charging_station

import Setup.BASE_URL
import Setup.adminLoginDTO
import Setup.buildRequest
import Setup.createClient
import Setup.loginAndGetToken
import infrastructure.Router.assemblePath
import infrastructure.charging_station.AddChargingStationDTO
import infrastructure.charging_station.ChargingStationDTO
import infrastructure.charging_station.ChargingStationRechargingDTO
import infrastructure.charging_station.LocationDTO
import infrastructure.charging_station.StartRechargeDTO
import infrastructure.charging_station.StopRechargeDTO
import infrastructure.charging_station.UpdateChargingStationDTO
import infrastructure.user.AddCarDTO
import infrastructure.user.CarDTO
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.equals.shouldBeEqual
import io.kotest.matchers.shouldBe
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.statement.HttpResponse
import io.ktor.client.HttpClient
import io.ktor.client.request.put
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpStatusCode
import kotlin.collections.forEach

class RechargeTest : FunSpec() {

    private companion object {
        const val CARS_URL = "$BASE_URL/cars"
    }

    lateinit var client: HttpClient
    lateinit var token: String

    private val addCar1Body = AddCarDTO("AB123YZ", 20)
    val chargingStation1 = AddChargingStationDTO(130, LocationDTO(longitude = 50.44, latitude = 37.12))

    init {
        beforeSpec {
            client = createClient()
            token = loginAndGetToken(client, adminLoginDTO)
            token.isNotEmpty() shouldBe true
        }

        afterSpec { client.close() }

        beforeEach {
            deleteAllUserCars()
            deleteAllChargingStations()
        }

        context("start recharge tests") {
            lateinit var chargingStation: ChargingStationDTO
            lateinit var car: CarDTO

            beforeEach {
                car = insertCar(addCar1Body).body<CarDTO>()
                chargingStation = insertChargingStation(chargingStation1).body<ChargingStationDTO>()
            }

            afterEach {
                stopRecharge(car._id, chargingStation._id)
            }

            test("it should start a recharge successfully") {
                val startResponse = startRecharge(car._id, chargingStation._id)
                println(startResponse.bodyAsText())
                println(startResponse.status)
                startResponse.status shouldBe HttpStatusCode.OK
                val response = client.get(chargingStationPath(chargingStation._id)) {
                    buildRequest<Unit>(token)
                }
                response.status shouldBe HttpStatusCode.OK
                response.body<ChargingStationRechargingDTO>() shouldBeEqual ChargingStationRechargingDTO(
                    chargingStation._id,
                    chargingStation.power,
                    chargingStation.available,
                    chargingStation.enabled,
                    chargingStation.location,
                    car._id
                )
            }

            test("it should fail to start a recharge with wrong id") {
                startRecharge(car._id, "").status shouldBe HttpStatusCode.NotFound
            }

            test("it should fail to start a recharge in an unavailable or disabled charging station") {
                val updateChargingStation = UpdateChargingStationDTO(available = false, enabled = true)
                client.put(chargingStationPath(chargingStation._id)) {
                    buildRequest(token, updateChargingStation)
                }
                startRecharge(car._id, chargingStation._id).status shouldBe HttpStatusCode.BadRequest

                client.put(chargingStationPath(chargingStation._id)) {
                    buildRequest(token, updateChargingStation.copy(available = true, enabled = false))
                }
                startRecharge(car._id, chargingStation._id).status shouldBe HttpStatusCode.BadRequest
            }
        }

        context("stop recharge tests") {
            lateinit var chargingStation: ChargingStationDTO
            lateinit var car: CarDTO

            beforeEach {
                car = insertCar(addCar1Body).body<CarDTO>()
                chargingStation = insertChargingStation(chargingStation1).body<ChargingStationDTO>()
            }

            test("it should stop a recharge successfully") {
                startRecharge(car._id, chargingStation._id)
                stopRecharge(car._id, chargingStation._id).status shouldBe HttpStatusCode.OK
            }

            test("it should fail to stop a recharge not started") {
                stopRecharge(car._id, chargingStation._id).status shouldBe HttpStatusCode.NotFound
            }
        }
    }

    private fun chargingStationPath(vararg paths: String) = BASE_URL.assemblePath("charging-stations", *paths)

    private suspend fun deleteAllUserCars() {
        val cars: Collection<CarDTO> = client.get(CARS_URL) { buildRequest<Unit>(token) }.body()
        cars.forEach { client.delete(CARS_URL.assemblePath(it._id)) { buildRequest<Unit>(token) } }
    }

    private suspend fun deleteAllChargingStations() {
        val chargingStations = client.get(chargingStationPath()) { buildRequest<Unit>(token) }
            .body<Collection<ChargingStationDTO>>()
        chargingStations.forEach {
            client.delete(chargingStationPath(it._id)) { buildRequest<Unit>(token) }
        }
    }

    private suspend fun insertCar(addCarBody: AddCarDTO): HttpResponse =
        client.post(CARS_URL) { buildRequest(token, body = addCarBody) }

    private suspend fun insertChargingStation(chargingStationToAdd: AddChargingStationDTO): HttpResponse =
        client.post(chargingStationPath()) {
            buildRequest(token, chargingStationToAdd)
        }

    private suspend fun startRecharge(carId: String, chargingStationId: String): HttpResponse =
        client.post(chargingStationPath(chargingStationId, "start-recharge")) {
            buildRequest(token, StartRechargeDTO(carId))
        }

    private suspend fun stopRecharge(carId: String, chargingStationId: String): HttpResponse =
        client.post(chargingStationPath(chargingStationId, "stop-recharge")) {
            buildRequest(token, StopRechargeDTO(carId))
        }
}
