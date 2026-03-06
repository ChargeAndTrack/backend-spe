package infrastructure

import infrastructure.user.CarController
import infrastructure.charging_station.ChargingStationsController
import infrastructure.charging_station.RechargeController
import infrastructure.user.UserController
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.*
import io.ktor.server.auth.authenticate
import io.ktor.server.response.respond
import io.ktor.server.routing.*

object Router {
    val module: Application.() -> Unit = {
        routing {
            get("/health") { call.respond(HttpStatusCode.OK) }
            val chargingStationPath = "/charging-stations"
            val carsPath = "/cars"
            route(Config.rootPath ?: "api/v1") {
                post("/login") { UserController.login(call) }
                authenticate("auth-jwt") {
                    get("/user") { UserController.getUser(call) }
                    get(chargingStationPath) { ChargingStationsController.listChargingStations(call) }
                    get(chargingStationPath.assemblePath("{id}")) {
                        ChargingStationsController.getChargingStation(call)
                    }
                    post(chargingStationPath.assemblePath("{id}", "start-recharge")) {
                        RechargeController.startRecharge(call)
                    }
                    post(chargingStationPath.assemblePath("{id}", "stop-recharge")) {
                        RechargeController.stopRecharge(call)
                    }
                    get(chargingStationPath.assemblePath("near")) {
                        ChargingStationsController.getNearbyChargingStations(call)
                    }
                    get(chargingStationPath.assemblePath("closest")) {
                        ChargingStationsController.getClosestChargingStation(call)
                    }
                    get(carsPath) { CarController.getCars(call) }
                    post(carsPath) { CarController.addCar(call) }
                    get(carsPath.assemblePath("{id}")) { CarController.getCar(call) }
                    put(carsPath.assemblePath("{id}")) { CarController.updateCar(call) }
                    delete(carsPath.assemblePath("{id}")) { CarController.deleteCar(call) }
                }
                authenticate("auth-admin") {
                    post(chargingStationPath) { ChargingStationsController.addChargingStation(call) }
                    delete(chargingStationPath.assemblePath("{id}")) {
                        ChargingStationsController.deleteChargingStation(call)
                    }
                    put(chargingStationPath.assemblePath("{id}")) {
                        ChargingStationsController.updateChargingStation(call)
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