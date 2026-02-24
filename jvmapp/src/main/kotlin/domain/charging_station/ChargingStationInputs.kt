package domain.charging_station

data class AddChargingStationInput(
    val power: Int,
    val location: Location
)

data class UpdateChargingStationInput(
    val power: Int?,
    val available: Boolean?,
    val enabled: Boolean?,
    val location: Location?
)
