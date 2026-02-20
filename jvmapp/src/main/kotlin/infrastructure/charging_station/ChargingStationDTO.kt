package infrastructure.charging_station

import domain.charging_station.ChargingStation
import domain.charging_station.ChargingStationImpl
import domain.charging_station.Location
import domain.charging_station.LocationImpl
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

fun ChargingStation.toDTO(): ChargingStationDTO = ChargingStationDTO(
    _id = id,
    power = power,
    available = available,
    enabled = enabled,
    location = location.toDTO()
)

fun ChargingStationDTO.toDomain(): ChargingStation = ChargingStationImpl(
    id = _id ?: "",
    power = power,
    available = available,
    enabled = enabled,
    location = location.toDomain()
)

fun Location.toDTO(): LocationDTO = LocationDTO(
    longitude = longitude,
    latitude = latitude
)

fun LocationDTO.toDomain(): Location = LocationImpl(
    longitude = longitude,
    latitude = latitude
)

fun AddChargingStationDTO.toCompleteDTO(): ChargingStationDTO = ChargingStationDTO(
    _id = null,
    power = power,
    location = location
)

