package infrastructure.charging_station

import domain.InvalidInputException
import domain.charging_station.AddChargingStationInput
import domain.charging_station.UpdateChargingStationInput
import domain.charging_station.ChargingStation
import domain.charging_station.ClosestChargingStationInput
import domain.charging_station.Location
import domain.charging_station.LocationImpl
import domain.charging_station.NearbyChargingStationsInput
import infrastructure.AbstractQueryDTO
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
    val currentCarId: String? = null
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
) : AbstractQueryDTO<AddChargingStationInput>() {
    override fun internalValidation() = validate(power, location)

    override fun toDomainEntity(): AddChargingStationInput = AddChargingStationInput(power, location.toDomain())
}

@Serializable
data class UpdateChargingStationDTO(
    val power: Int? = null,
    val available: Boolean? = null,
    val enabled: Boolean? = null,
    val location: LocationDTO? = null
) : AbstractQueryDTO<UpdateChargingStationInput>() {
    override fun internalValidation() = validate(power, location)

    override fun toDomainEntity(): UpdateChargingStationInput = UpdateChargingStationInput(
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
) : AbstractQueryDTO<NearbyChargingStationsInput>() {
    override fun internalValidation() = validate(location = LocationDTO(longitude, latitude), radius = radius)

    override fun toDomainEntity(): NearbyChargingStationsInput =
        NearbyChargingStationsInput(longitude, latitude, radius, onlyEnabled ?: true)
}

@Serializable
data class ClosestChargingStationDTO(
    val longitude: Double,
    val latitude: Double,
    val onlyEnabledAndAvailable: Boolean? = null
) : AbstractQueryDTO<ClosestChargingStationInput>() {
    override fun internalValidation() = validate(location = LocationDTO(longitude, latitude))

    override fun toDomainEntity(): ClosestChargingStationInput = ClosestChargingStationInput(
        longitude,
        latitude,
        onlyEnabledAndAvailable ?: true
    )
}

fun ChargingStation.toDTO(): ChargingStationDTO = ChargingStationDTO(
    _id = id,
    power = power,
    available = available,
    enabled = enabled,
    location = location.toDTO()
)

fun Collection<ChargingStation>.toDTO(): Collection<ChargingStationDTO> = map { it.toDTO() }

fun ChargingStation.toDTO(currentCarId: String? = null) = ChargingStationRechargingDTO(
    _id = id,
    power = power,
    available = available,
    enabled = enabled,
    location = location.toDTO(),
    currentCarId = currentCarId
)

fun Location.toDTO(): LocationDTO = LocationDTO(longitude, latitude)

private fun validate(power: Int? = null, location: LocationDTO? = null, radius: Double? = null) {
    power?.also { require(it > 0) { "Power must be greater than zero." } }
    location?.also {
        require(it.longitude in -180.0..180.0) { "Longitude must be between -180 and 180 degrees" }
        require(it.latitude in -90.0..90.0) { "Latitude must be between -90 and 90 degrees" }
    }
    radius?.also { require(it > 0) { "Radius must be greater than zero." } }
}
