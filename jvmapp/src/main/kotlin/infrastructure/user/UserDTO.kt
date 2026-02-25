package infrastructure.user

import application.user.AddCarInput
import application.user.UpdateCarInput
import domain.user.Car
import domain.user.User
import kotlinx.serialization.Serializable

@Serializable
data class UserDTO(
    val _id: String,
    val username: String,
    val password: String,
    val role: String,
    var cars: Collection<CarDTO> = emptySet()
)

@Serializable
data class CarDTO(
    val _id: String,
    var plate: String,
    var maxBattery: Int,
    var currentBattery: Int?
)

@Serializable
data class AddCarDTO(
    val plate: String,
    val maxBattery: Int
)

@Serializable
data class UpdateCarDTO(
    val plate: String? = null,
    val maxBattery: Int? = null,
    val currentBattery: Int? = null
)

fun User.toDTO(): UserDTO =
    UserDTO(
        _id = id,
        username = username,
        password = password,
        role = role.displayName,
        cars = cars.toDTO()
    )

fun Collection<Car>.toDTO() = map { it.toDTO() }

fun Car.toDTO(): CarDTO = CarDTO(_id = id, plate = plate, maxBattery = maxBattery, currentBattery = currentBattery)

fun AddCarDTO.toInput(): AddCarInput = AddCarInput(plate = plate, maxBattery = maxBattery)

fun UpdateCarDTO.toInput(): UpdateCarInput =
    UpdateCarInput(plate = plate, maxBattery = maxBattery, currentBattery = currentBattery)