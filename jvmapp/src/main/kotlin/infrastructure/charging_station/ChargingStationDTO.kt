package infrastructure.charging_station

import domain.charging_station.AddChargingStationInput
import domain.charging_station.UpdateChargingStationInput
import domain.charging_station.ChargingStation
import domain.charging_station.ChargingStationImpl
import domain.charging_station.Location
import domain.charging_station.LocationImpl
import kotlinx.serialization.Serializable

@Serializable
data class ChargingStationDTO(
    val _id: String?,
    val power: Int,
    val available: Boolean = true,
    val enabled: Boolean = true,
    val location: LocationDTO
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

@Serializable
data class UpdateChargingStationDTO(
    val power: Int?,
    val available: Boolean?,
    val enabled: Boolean?,
    val location: LocationDTO?
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

fun AddChargingStationDTO.toInput(): AddChargingStationInput = AddChargingStationInput(
    power = power,
    location = location.toDomain()
)

fun UpdateChargingStationDTO.toInput(): UpdateChargingStationInput = UpdateChargingStationInput(
    power = power,
    available = available,
    enabled = enabled,
    location = location?.toDomain()
)
