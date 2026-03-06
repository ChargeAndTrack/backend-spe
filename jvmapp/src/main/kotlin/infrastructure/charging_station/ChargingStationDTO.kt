package infrastructure.charging_station

import domain.InvalidInputException
import domain.charging_station.AddChargingStationInput
import domain.charging_station.UpdateChargingStationInput
import domain.charging_station.ChargingStation
import domain.charging_station.ClosestChargingStationInput
import domain.charging_station.Location
import domain.charging_station.LocationImpl
import domain.charging_station.NearbyChargingStationsInput
import infrastructure.QueryDTO
import kotlinx.serialization.Serializable

@Serializable
data class ChargingStationDTO(
    val _id: String,
    val power: Int,
    val available: Boolean = true,
    val enabled: Boolean = true,
    val location: LocationDTO
)

@Serializable
data class ChargingStationRechargingDTO(
    val _id: String,
    val power: Int,
    val available: Boolean = true,
    val enabled: Boolean = true,
    val location: LocationDTO,
    val currentCarId: String
)

@Serializable
data class LocationDTO(
    val longitude: Double,
    val latitude: Double
) {
    fun toDomain(): Location = LocationImpl(longitude, latitude)
}

@Serializable
data class AddChargingStationDTO(
    val power: Int,
    val location: LocationDTO
) : QueryDTO<AddChargingStationInput> {
    override fun validate() = validate(power, location)

    override fun toInput(): AddChargingStationInput = AddChargingStationInput(power, location.toDomain())
}

@Serializable
data class UpdateChargingStationDTO(
    val power: Int? = null,
    val available: Boolean? = null,
    val enabled: Boolean? = null,
    val location: LocationDTO? = null
) : QueryDTO<UpdateChargingStationInput> {
    override fun validate() = validate(power, location)

    override fun toInput(): UpdateChargingStationInput = UpdateChargingStationInput(
        power = power,
        available = available,
        enabled = enabled,
        location = location?.toDomain()
    )
}

@Serializable
data class NearbyChargingStationsDTO(
    val longitude: Double,
    val latitude: Double,
    val radius: Double,
    val onlyEnabled: Boolean? = null
) : QueryDTO<NearbyChargingStationsInput> {
    override fun validate() = validate(location = LocationDTO(longitude, latitude), radius = radius)

    override fun toInput(): NearbyChargingStationsInput = NearbyChargingStationsInput(longitude, latitude, radius, onlyEnabled)
}

@Serializable
data class ClosestChargingStationDTO(
    val longitude: Double,
    val latitude: Double,
    val onlyEnabledAndAvailable: Boolean? = null
) : QueryDTO<ClosestChargingStationInput> {
    override fun validate() = validate(location = LocationDTO(longitude, latitude))

    override fun toInput(): ClosestChargingStationInput = ClosestChargingStationInput(
        longitude,
        latitude,
        onlyEnabledAndAvailable
    )
}

fun ChargingStation.toDTO(): ChargingStationDTO = ChargingStationDTO(
    _id = id,
    power = power,
    available = available,
    enabled = enabled,
    location = location.toDTO()
)

fun ChargingStation.toDTO(currentCarId: String) = ChargingStationRechargingDTO(
    _id = id,
    power = power,
    available = available,
    enabled = enabled,
    location = location.toDTO(),
    currentCarId = currentCarId
)

fun Location.toDTO(): LocationDTO = LocationDTO(longitude, latitude)

private fun validate(power: Int? = null, location: LocationDTO? = null, radius: Double? = null) {
    runCatching {
        power?.also { require(it > 0) { "Power must be greater than zero." } }
        location?.also {
            require(it.longitude in -180.0..180.0) { "Longitude must be between -180 and 180 degrees" }
            require(it.latitude in -90.0..90.0) { "Latitude must be between -90 and 90 degrees" }
        }
        radius?.also { require(it > 0) { "Radius must be greater than zero." } }
    }.onFailure { throw InvalidInputException(it.message ?: "Invalid input") }
}
