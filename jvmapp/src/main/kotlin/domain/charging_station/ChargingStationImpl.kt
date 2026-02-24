package domain.charging_station

data class ChargingStationImpl(
    override val id: String,
    override val power: Int,
    override val available: Boolean,
    override val enabled: Boolean,
    override val location: Location
) : ChargingStation {

    override fun update(updateChargingStationInput: UpdateChargingStationInput): ChargingStation = copy(
        power = updateChargingStationInput.power ?: power,
        available = updateChargingStationInput.available ?: available,
        enabled = updateChargingStationInput.enabled ?: enabled,
        location = updateChargingStationInput.location ?: location
    )
}
