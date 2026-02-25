package infrastructure.user

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
        val cars = carService.getCars(getUserId(call))
        println("Cars: $cars")
        call.respond(HttpStatusCode.OK, cars.toDTO())
    }

    suspend fun addCar(call: ApplicationCall) {
        println("addCar")
        val request = call.receive<AddCarDTO>()
        val car = carService.addCar(getUserId(call), request.toInput())
        println("New car: $car")
        call.respond(HttpStatusCode.Created, car.toDTO())
    }

    suspend fun getCar(call: ApplicationCall) = handleCarRequest(call) { carId ->
        println("getCar, received carId $carId")
        val car = carService.getCar(getUserId(call), carId)
        println("Car: $car")
        call.respond(HttpStatusCode.OK, car.toDTO())
    }

    suspend fun updateCar(call: ApplicationCall) = handleCarRequest(call) { carId ->
        println("updateCar, received carId $carId")
        val request = call.receive<UpdateCarDTO>()
        val updatedCar = carService.updateCar(getUserId(call), carId, request.toInput())
        println("UpdatedCar: $updatedCar")
        call.respond(HttpStatusCode.OK, updatedCar.toDTO())
    }

    suspend fun deleteCar(call: ApplicationCall) = handleCarRequest(call) { carId ->
        println("deleteCar, received carId $carId")
        val cars = carService.deleteCar(getUserId(call), carId)
        println("Cars: $cars")
        call.respond(HttpStatusCode.OK, cars.toDTO())
    }

    private suspend fun handleCarRequest(call: ApplicationCall, handler: suspend (carId: String) -> Unit) {
        val carId = call.parameters["id"]
        if (carId == null) {
            call.respond(HttpStatusCode.BadRequest)
            return
        }
        handler(carId)
    }
}