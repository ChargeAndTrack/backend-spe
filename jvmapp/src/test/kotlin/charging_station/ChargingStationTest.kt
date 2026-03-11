package charging_station

import Setup.adminLoginDTO
import Setup.buildRequest
import Setup.chargingStationPath
import Setup.createClient
import Setup.deleteAllChargingStations
import Setup.insertChargingStation
import Setup.loginAndGetToken
import infrastructure.charging_station.AddChargingStationDTO
import infrastructure.charging_station.ChargingStationDTO
import infrastructure.charging_station.ChargingStationRechargingDTO
import infrastructure.charging_station.LocationDTO
import infrastructure.charging_station.UpdateChargingStationDTO
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.equals.shouldBeEqual
import io.kotest.matchers.shouldBe
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.put
import io.ktor.http.HttpStatusCode

class ChargingStationTest : FunSpec() {

    lateinit var client: HttpClient
    lateinit var token: String

    val chargingStation1 = AddChargingStationDTO(130, LocationDTO(longitude = 50.44, latitude = 37.12))
    val chargingStation2 = AddChargingStationDTO(80, LocationDTO(longitude = 50.45, latitude = 37.15))

    init {
        beforeSpec {
            client = createClient()
            token = loginAndGetToken(client, adminLoginDTO)
            token.isNotEmpty() shouldBe true
        }

        afterSpec { client.close() }

        beforeEach { deleteAllChargingStations(client, token) }

        test("it should list all charging stations successfully") {
            val chargingStations: Collection<ChargingStationRechargingDTO> = insertChargingStations()
            val response = client.get(chargingStationPath()) { buildRequest<Unit>(token) }
            response.status shouldBeEqual HttpStatusCode.OK
            response.body<Collection<ChargingStationRechargingDTO>>() shouldContainExactly chargingStations
        }

        context("add charging station tests") {
            test("it should add a charging station successfully") {
                val response = insertChargingStation(client, token, chargingStation1)
                val responseBody = response.body<ChargingStationDTO>()
                response.status shouldBeEqual HttpStatusCode.Created
                responseBody.power shouldBeEqual chargingStation1.power
                responseBody.location shouldBeEqual chargingStation1.location
            }

            test("it should fail to add a charging station with non-positive power") {
                val addChargingStation = AddChargingStationDTO(
                    0,
                    LocationDTO(longitude = 50.44, latitude = 37.12)
                )
                insertChargingStation(client, token, addChargingStation).status shouldBeEqual HttpStatusCode.BadRequest
                insertChargingStation(client, token, addChargingStation.copy(power = -10))
                    .status shouldBeEqual HttpStatusCode.BadRequest
            }
        }

        context("get charging station tests") {
            lateinit var chargingStation: ChargingStationDTO

            beforeEach {
                chargingStation = insertChargingStation(client, token, chargingStation1).body<ChargingStationDTO>()
            }

            test("it should get the charging station by id successfully") {
                val response = client.get(chargingStationPath(chargingStation._id)) {
                    buildRequest<Unit>(token, parametersMap = mapOf("without-car-in-charge" to ""))
                }
                response.status shouldBeEqual HttpStatusCode.OK
                response.body<ChargingStationDTO>() shouldBeEqual chargingStation
            }

            test("it should fail to get the charging station with a wrong id") {
                client.get(chargingStationPath("")) {
                    buildRequest<Unit>(token, parametersMap = mapOf("without-car-in-charge" to ""))
                }.status shouldBeEqual HttpStatusCode.NotFound
            }
        }

        context("update charging station tests") {
            val updateChargingStation = UpdateChargingStationDTO(power = 150, available = false)
            lateinit var chargingStation: ChargingStationDTO

            beforeEach {
                chargingStation = insertChargingStation(client, token, chargingStation1).body<ChargingStationDTO>()
            }

            test("it should update the charging station by id successfully") {
                val response = client.put(chargingStationPath(chargingStation._id)) {
                    buildRequest(token, updateChargingStation)
                }
                response.status shouldBeEqual HttpStatusCode.OK
                response.body<ChargingStationDTO>() shouldBeEqual ChargingStationDTO(
                    _id = chargingStation._id,
                    power = updateChargingStation.power ?: chargingStation.power,
                    available = updateChargingStation.available ?: chargingStation.available,
                    enabled = updateChargingStation.enabled ?: chargingStation.enabled,
                    location = updateChargingStation.location ?: chargingStation.location
                )
            }

            test("it should fail to update the charging station with a wrong id") {
                client.put(chargingStationPath("")) {
                    buildRequest(token, updateChargingStation)
                }.status shouldBeEqual HttpStatusCode.NotFound
            }

            test("it should fail to update the charging station with a non-positive power") {
                client.put(chargingStationPath(chargingStation._id)) {
                    buildRequest(token, updateChargingStation.copy(power = 0))
                }.status shouldBeEqual HttpStatusCode.BadRequest
                client.put(chargingStationPath(chargingStation._id)) {
                    buildRequest(token, updateChargingStation.copy(power = -10))
                }.status shouldBeEqual HttpStatusCode.BadRequest
            }
        }

        context("delete charging station tests") {
            lateinit var chargingStation: ChargingStationDTO

            beforeEach {
                chargingStation = insertChargingStation(client, token, chargingStation1).body<ChargingStationDTO>()
            }

            test("it should delete the charging station successfully") {
                client.delete(chargingStationPath(chargingStation._id)) {
                    buildRequest<Unit>(token)
                }.status shouldBeEqual HttpStatusCode.OK
            }

            test("it should fail to delete the charging station with wrong id") {
                client.delete(chargingStationPath("")) {
                    buildRequest<Unit>(token)
                }.status shouldBeEqual HttpStatusCode.NotFound
            }
        }

        context("get charging stations by position tests") {
            lateinit var chargingStations: Collection<ChargingStationRechargingDTO>

            beforeEach { chargingStations = insertChargingStations() }

            test("it get near charging stations successfully") {
                val response = client.get(chargingStationPath("near")) {
                    buildRequest<Unit>(token, parametersMap = mapOf(
                        "lng" to 50.44.toString(),
                        "lat" to 37.12.toString(),
                        "radius" to 50_000.0.toString()
                    ))
                }
                response.status shouldBeEqual HttpStatusCode.OK
                response.body<Collection<ChargingStationRechargingDTO>>() shouldContainExactly chargingStations
            }

            test("it should fail to get the near charging stations and the closest one when invalid coordinates are passed") {
                client.get(chargingStationPath("near")) {
                    buildRequest<Unit>(token, parametersMap = mapOf(
                        "lng" to (-182.44).toString(),
                        "lat" to 94.12.toString(),
                        "radius" to 50_000.0.toString()
                    ))
                }.status shouldBeEqual HttpStatusCode.BadRequest
                client.get(chargingStationPath("closest")) {
                    buildRequest<Unit>(token, parametersMap = mapOf(
                        "lng" to (-182.44).toString(),
                        "lat" to 94.12.toString(),
                        "onlyEnabledAndAvailable" to true.toString()
                    ))
                }.status shouldBeEqual HttpStatusCode.BadRequest
            }

            test("it should fail to get the near charging stations when a negative radius is passed") {
                client.get(chargingStationPath("near")) {
                    buildRequest<Unit>(token, parametersMap = mapOf(
                        "lng" to 50.44.toString(),
                        "lat" to 37.12.toString(),
                        "radius" to (-10.0).toString()
                    ))
                }.status shouldBeEqual HttpStatusCode.BadRequest
            }

            test("it should get closest charging station successfully") {
                val response1 = client.get(chargingStationPath("closest")) {
                    buildRequest<Unit>(token, parametersMap = mapOf(
                        "lng" to 50.44.toString(),
                        "lat" to 37.00.toString()
                    ))
                }
                client.put(chargingStationPath(chargingStations.first()._id)) {
                    buildRequest(token, UpdateChargingStationDTO(enabled = false))
                }
                val response2 = client.get(chargingStationPath("closest")) {
                    buildRequest<Unit>(
                        token,
                        parametersMap = mapOf(
                            "lng" to 50.44.toString(),
                            "lat" to 37.00.toString(),
                            "onlyEnabledAndAvailable" to true.toString()
                        )
                    )
                }
                response1.status shouldBeEqual HttpStatusCode.OK
                response1.body<ChargingStationRechargingDTO>() shouldBeEqual chargingStations.first()
                response2.status shouldBeEqual HttpStatusCode.OK
                response2.body<ChargingStationRechargingDTO>() shouldBeEqual chargingStations.elementAt(1)
            }
        }
    }

    private suspend inline fun <reified T> insertChargingStations(): Collection<T> = listOf(
        insertChargingStation(client, token,chargingStation1),
        insertChargingStation(client, token, chargingStation2)
    ).map { it.body<T>() }.toList()
}
