package infrastructure.user

import domain.InvalidInputException
import domain.user.AddCarInput
import domain.user.UpdateCarInput
import domain.user.Car
import domain.user.User
import infrastructure.QueryDTO
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
    var currentBattery: Int?,
    var currentChargingStationId: String?
)

@Serializable
data class AddCarDTO(
    val plate: String,
    val maxBattery: Int
) : QueryDTO<AddCarInput> {
    override fun validate() = validate(plate = plate, maxBattery = maxBattery)

    override fun toInput(): AddCarInput = AddCarInput(plate = plate, maxBattery = maxBattery)
}

@Serializable
data class UpdateCarDTO(
    val plate: String? = null,
    val maxBattery: Int? = null,
    val currentBattery: Int? = null
) : QueryDTO<UpdateCarInput> {
    override fun validate() = validate(plate = plate, maxBattery = maxBattery, currentBattery = currentBattery)

    override fun toInput(): UpdateCarInput =
        UpdateCarInput(plate = plate, maxBattery = maxBattery, currentBattery = currentBattery)
}

fun User.toDTO(): UserDTO =
    UserDTO(
        _id = id,
        username = username,
        password = password,
        role = role.displayName,
        cars = cars.toDTO()
    )

fun Collection<Car>.toDTO() = map { it.toDTO() }

fun Car.toDTO(currentChargingStationId: String? = null): CarDTO = CarDTO(
    _id = id,
    plate = plate,
    maxBattery = maxBattery,
    currentBattery = currentBattery,
    currentChargingStationId = currentChargingStationId
)

private fun validate(plate: String? = null, maxBattery: Int? = null, currentBattery: Int? = null) =
    runCatching {
        plate?.also {
            require(it.length in 3..10 && it.matches(Regex("^[A-Z0-9 -]+$"))) { "Invalid plate format" }
        }
        maxBattery?.also {
            require(it > 0) { "Invalid max battery, value must be a positive number" }
        }
        currentBattery?.also {
            require(it in 0..100) { "Invalid current battery, value must be between 0 and 100" }
        }
    }.onFailure { throw InvalidInputException(it.message ?: "Invalid input") }.let {}