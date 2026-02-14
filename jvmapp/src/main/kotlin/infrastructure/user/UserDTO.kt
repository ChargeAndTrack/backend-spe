package infrastructure.user

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