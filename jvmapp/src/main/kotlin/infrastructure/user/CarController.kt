package infrastructure.user

import application.user.CarService
import application.user.CarServiceImpl
import domain.InvalidInputException
import domain.user.Car
import infrastructure.charging_station.RechargeController
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.ApplicationCall
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.principal
import io.ktor.server.request.receive
import io.ktor.server.response.respond

class CarController(private val rechargeController: RechargeController) {
    private val carService: CarService = CarServiceImpl(MongoDbUserRepository())

    suspend fun getCars(call: ApplicationCall) {
        println("getCars")
        call.respond(HttpStatusCode.OK, carService.getCars(getUserId(call)).toDTO())
    }

    suspend fun addCar(call: ApplicationCall) {
        println("addCar")
        val body = call.receive<AddCarDTO>().also { it.validate() }
        call.respond(HttpStatusCode.Created, carService.addCar(getUserId(call), body.toInput()).toDTO())
    }

    suspend fun getCar(call: ApplicationCall) = handleCarRequest(call) { carId ->
        println("getCar, carId $carId")
        call.respond(
            HttpStatusCode.OK,
            carService.getCar(getUserId(call), carId)
                .toDTO(rechargeController.rechargeService.getChargingStationIdByCarId(carId))
        )
    }

    suspend fun updateCar(call: ApplicationCall) = handleCarRequest(call) { carId ->
        println("updateCar, carId $carId")
        val body = call.receive<UpdateCarDTO>().also { it.validate() }
        call.respond(HttpStatusCode.OK, carService.updateCar(getUserId(call), carId, body.toInput()).toDTO())
    }

    suspend fun deleteCar(call: ApplicationCall) = handleCarRequest(call) { carId ->
        println("deleteCar, carId $carId")
        call.respond(HttpStatusCode.OK, carService.deleteCar(getUserId(call), carId).toDTO())
    }

    private suspend fun Collection<Car>.toDTO() = map {
        it.toDTO(rechargeController.rechargeService.getChargingStationIdByCarId(it.id))
    }

    private fun getUserId(call: ApplicationCall): String =
        call.principal<JWTPrincipal>()!!.payload.getClaim("userId").asString()

    private suspend fun handleCarRequest(call: ApplicationCall, handler: suspend (carId: String) -> Unit) =
        handler(call.parameters["id"] ?: throw InvalidInputException("Id not provided"))
}