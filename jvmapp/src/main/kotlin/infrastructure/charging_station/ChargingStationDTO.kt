package infrastructure.charging_station

import domain.charging_station.AddChargingStationInput
import domain.charging_station.UpdateChargingStationInput
import domain.charging_station.ChargingStation
import domain.charging_station.ClosestChargingStationInput
import domain.charging_station.Location
import domain.charging_station.LocationImpl
import domain.charging_station.NearbyChargingStationsInput
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
) {
    init { validate(power, location) }
}

@Serializable
data class UpdateChargingStationDTO(
    val power: Int?,
    val available: Boolean?,
    val enabled: Boolean?,
    val location: LocationDTO?
) {
    init { validate(power, location) }
}

@Serializable
data class NearbyChargingStationsDTO(
    val longitude: Double,
    val latitude: Double,
    val radius: Double,
    val onlyEnabled: Boolean?
) {
    init { validate(location = LocationDTO(longitude, latitude), radius = radius) }
}

@Serializable
data class ClosestChargingStationDTO(
    val longitude: Double,
    val latitude: Double,
    val onlyEnabledAndAvailable: Boolean?
) {
    init { validate(location = LocationDTO(longitude, latitude)) }
}

fun ChargingStation.toDTO(): ChargingStationDTO = ChargingStationDTO(
    _id = id,
    power = power,
    available = available,
    enabled = enabled,
    location = location.toDTO()
)

fun Location.toDTO(): LocationDTO = LocationDTO(longitude, latitude)

fun LocationDTO.toDomain(): Location = LocationImpl(longitude, latitude)

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

fun NearbyChargingStationsDTO.toInput(): NearbyChargingStationsInput = NearbyChargingStationsInput(
    longitude = longitude,
    latitude = latitude,
    radius = radius,
    onlyEnabled = onlyEnabled
)

fun ClosestChargingStationDTO.toInput(): ClosestChargingStationInput = ClosestChargingStationInput(
    longitude = longitude,
    latitude = latitude,
    onlyEnabledAndAvailable = onlyEnabledAndAvailable
)

private fun validate(power: Int? = null, location: LocationDTO? = null, radius: Double? = null) {
    power?.also { require(it > 0) { "Power must be greater than zero."} }
    location?.also {
        require(it.longitude in -180.0..180.0) { "Longitude must be between -180 and 180 degrees" }
        require(it.latitude in -90.0..90.0) { "Latitude must be between -90 and 90 degrees" }
    }
    radius?.also { require(it > 0) { "Radius must be greater than zero." }}
}
