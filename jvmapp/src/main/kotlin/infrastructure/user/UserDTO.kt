package infrastructure.user

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

fun User.toDTO(): UserDTO =
    UserDTO(
        _id = id,
        username = username,
        password = password,
        role = role.displayName,
        cars = cars.toDTO()
    )

fun Collection<Car>.toDTO() = map { it.toDTO() }

fun Car.toDTO(): CarDTO =
    CarDTO(
        _id = id,
        plate = plate,
        maxBattery = maxBattery,
        currentBattery = currentBattery
    )