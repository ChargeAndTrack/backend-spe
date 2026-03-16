package infrastructure

import infrastructure.user.CarController
import infrastructure.charging_station.ChargingStationsController
import infrastructure.charging_station.RechargeController
import infrastructure.charging_station.LocationController
import infrastructure.user.UserController
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.*
import io.ktor.server.auth.authenticate
import io.ktor.server.response.respond
import io.ktor.server.routing.*

object Router {
    val module: Application.() -> Unit = {
        val userController = UserController()
        val chargingStationsController = ChargingStationsController()
        val rechargeController = RechargeController()
        val carController = CarController(rechargeController)
        val locationController = LocationController()
        routing {
            get("/health") { call.respond(HttpStatusCode.OK) }
            val chargingStationPath = "/charging-stations"
            val carsPath = "/cars"
            val locationPath = "/location"
            route(Config.rootPath ?: "api/v1") {
                post("/login") { userController.login(call) }
                authenticate("auth-jwt") {
                    get("/user") { userController.getUser(call) }
                    get(chargingStationPath) { chargingStationsController.listChargingStations(call) }
                    get(chargingStationPath.assemblePath("{id}")) {
                        chargingStationsController.getChargingStation(call)
                    }
                    post(chargingStationPath.assemblePath("{id}", "start-recharge")) {
                        rechargeController.startRecharge(call)
                    }
                    post(chargingStationPath.assemblePath("{id}", "stop-recharge")) {
                        rechargeController.stopRecharge(call)
                    }
                    get(chargingStationPath.assemblePath("near")) {
                        chargingStationsController.getNearbyChargingStations(call)
                    }
                    get(chargingStationPath.assemblePath("closest")) {
                        chargingStationsController.getClosestChargingStation(call)
                    }
                    get(carsPath) { carController.getCars(call) }
                    post(carsPath) { carController.addCar(call) }
                    get(carsPath.assemblePath("{id}")) { carController.getCar(call) }
                    put(carsPath.assemblePath("{id}")) { carController.updateCar(call) }
                    delete(carsPath.assemblePath("{id}")) { carController.deleteCar(call) }
                    get(locationPath.assemblePath("resolve")) {
                        locationController.resolveAddressToLocationCoordinates(call)
                    }
                    get(locationPath.assemblePath("reverse")) {
                        locationController.reverseLocationCoordinatesToAddress(call)
                    }
                }
                authenticate("auth-admin") {
                    post(chargingStationPath) { chargingStationsController.addChargingStation(call) }
                    delete(chargingStationPath.assemblePath("{id}")) {
                        chargingStationsController.deleteChargingStation(call)
                    }
                    put(chargingStationPath.assemblePath("{id}")) {
                        chargingStationsController.updateChargingStation(call)
                    }
                }
            }
        }
    }

    fun String.assemblePath(vararg paths: String): String = buildString {
        append(this@assemblePath)
        paths.forEach { append("/$it") }
    }
}