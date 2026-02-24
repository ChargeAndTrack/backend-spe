package infrastructure

import infrastructure.charging_station.ChargingStationsController
import infrastructure.user.UserController
import io.ktor.server.application.*
import io.ktor.server.auth.authenticate
import io.ktor.server.routing.*

object Router {
    val module: Application.() -> Unit = {
        routing {
            val chargingStationPath = "/charging-stations"
            route(Config.rootPath ?: "api/v1") {
                post("/login") { UserController.login(call) }
                authenticate("auth-jwt") {
                    get("/user") { UserController.getUser(call) }
                    get(chargingStationPath) { ChargingStationsController.listChargingStations(call) }
                    get(chargingStationPath.assemblePath("/{id}")) {
                        ChargingStationsController.getChargingStation(call)
                    }
                }
                authenticate("auth-admin") {
                    get("/admin") { UserController.getUser(call) }
                    post(chargingStationPath) { ChargingStationsController.addChargingStation(call) }
                    delete(chargingStationPath.assemblePath("/{id}")) {
                        ChargingStationsController.deleteChargingStation(call)
                    }
                    put(chargingStationPath.assemblePath("/{id}")) {
                        ChargingStationsController.updateChargingStation(call)
                    }
                }
            }
        }
    }

    fun String.assemblePath(vararg paths: String): String = buildString {
        append(this@assemblePath)
        paths.forEach(this::append)
    }
}