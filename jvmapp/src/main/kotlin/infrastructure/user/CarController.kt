package infrastructure.user

import AddCarInput
import application.user.CarService
import application.user.CarServiceImpl
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.ApplicationCall
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.principal
import io.ktor.server.request.receive
import io.ktor.server.response.respond

object CarController {
    private val carService: CarService = CarServiceImpl(UserRepositoryImpl())

    private fun getUserId(call: ApplicationCall): String {
        val principal = call.principal<JWTPrincipal>()
        return principal!!.payload.getClaim("userId").asString()
    }

    suspend fun getCars(call: ApplicationCall) {
        println("getCars")
        val userId = getUserId(call)
        val cars = carService.getCars(userId)
        println("Cars: $cars")
        call.respond(HttpStatusCode.OK, cars.toDTO())
    }

    suspend fun addCar(call: ApplicationCall) {
        println("addCar")
        val userId = getUserId(call)
        val request = call.receive<AddCarDTO>()
        val car = carService.addCar(userId, request.toInput())
        println("New car: $car")
        call.respond(HttpStatusCode.Created, car.toDTO())
    }

    suspend fun getCar(call: ApplicationCall) {
        TODO()
    }

    suspend fun updateCar(call: ApplicationCall) {
        TODO()
    }

    suspend fun deleteCar(call: ApplicationCall) = handleCarRequest(call) { carId ->
        println("DeleteCar, received carId $carId")
        val cars = carService.deleteCar(getUserId(call), carId)
        println("Cars: $cars")
        call.respond(HttpStatusCode.OK, cars.toDTO())
    }

    private fun AddCarDTO.toInput(): AddCarInput = AddCarInput(plate = plate, maxBattery = maxBattery)

    private suspend fun handleCarRequest(call: ApplicationCall, handler: suspend (carId: String) -> Unit) {
        val carId = call.parameters["id"]
        if (carId == null) {
            call.respond(HttpStatusCode.BadRequest)
            return
        }
        handler(carId)
    }
}