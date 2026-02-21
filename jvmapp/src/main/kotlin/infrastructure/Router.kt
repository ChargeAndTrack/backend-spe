package infrastructure

import infrastructure.user.CarController
import infrastructure.charging_station.ChargingStationsController
import infrastructure.user.UserController
import io.ktor.server.application.*
import io.ktor.server.auth.authenticate
import io.ktor.server.routing.*

object Router {
    val module: Application.() -> Unit = {
        routing {
            route(Config.rootPath ?: "api/v1") {
                post("/login") { UserController.login(call) }
                authenticate("auth-jwt") {
                    get("/user") { UserController.getUser(call) }
                    get("/cars") { CarController.getCars(call) }
                    post("/cars") { CarController.addCar(call) }
                    get("/cars/{id}") { CarController.getCar(call) }
                    put("/cars/{id}") { CarController.updateCar(call) }
                    delete("/cars/{id}") { CarController.deleteCar(call) }
                    get("/charging-stations") { ChargingStationsController.listChargingStations(call) }
                }
                authenticate("auth-admin") {
                    get("/admin") { UserController.getUser(call) }
                    post("/charging-stations") { ChargingStationsController.addChargingStation(call) }
                }
            }
        }
    }

    fun String.assemblePath(vararg paths: String): String = buildString {
        append(this@assemblePath)
        paths.forEach(this::append)
    }
}