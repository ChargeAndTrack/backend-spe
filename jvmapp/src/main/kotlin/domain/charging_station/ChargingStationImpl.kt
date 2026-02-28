package domain.charging_station

data class ChargingStationImpl(
    override val id: String,
    override val power: Int,
    override val available: Boolean,
    override val enabled: Boolean,
    override val location: Location
) : ChargingStation {

    init {
        require(power > 0) { "Power must be greater than zero."}
    }

    companion object Factory {
        fun create(id: String, addChargingStationInput: AddChargingStationInput): ChargingStation = ChargingStationImpl(
            id = id,
            power = addChargingStationInput.power,
            available = true,
            enabled = true,
            location = addChargingStationInput.location
        )
    }

    override fun update(updateChargingStationInput: UpdateChargingStationInput): ChargingStation = copy(
        power = updateChargingStationInput.power ?: power,
        available = updateChargingStationInput.available ?: available,
        enabled = updateChargingStationInput.enabled ?: enabled,
        location = updateChargingStationInput.location ?: location
    )
}
