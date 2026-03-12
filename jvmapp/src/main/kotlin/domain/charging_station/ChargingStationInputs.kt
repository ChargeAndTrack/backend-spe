package domain.charging_station

data class AddChargingStationInput(
    val power: Int,
    val location: Location
)

data class UpdateChargingStationInput(
    val power: Int? = null,
    val available: Boolean? = null,
    val enabled: Boolean? = null,
    val location: Location? = null
)

data class NearbyChargingStationsInput(
    val longitude: Double,
    val latitude: Double,
    val radius: Double,
    val onlyEnabled: Boolean = true
)

data class ClosestChargingStationInput(
    val longitude: Double,
    val latitude: Double,
    val onlyEnabledAndAvailable: Boolean = true,
    val filters: Collection<ChargingStationFilter> = emptyList()
)
