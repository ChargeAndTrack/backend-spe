package infrastructure.charging_station

import kotlinx.serialization.Serializable

@Serializable
data class ChargingStationDTO(
    val _id: String?,
    var power: Int,
    var available: Boolean = true,
    var enabled: Boolean = true,
    var location: LocationDTO,
    var currentCarId: String? = null
)

@Serializable
data class LocationDTO(
    val longitude: Double,
    val latitude: Double
)

@Serializable
data class AddChargingStationDTO(
    val power: Int,
    val location: LocationDTO
)
