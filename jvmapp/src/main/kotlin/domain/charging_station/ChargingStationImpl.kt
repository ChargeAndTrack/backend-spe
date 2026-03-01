package domain.charging_station

import domain.InvalidInputException

data class ChargingStationImpl(
    override val id: String,
    override val power: Int,
    override val available: Boolean,
    override val enabled: Boolean,
    override val location: Location
) : ChargingStation {

    init {
        runCatching {
            require(power > 0) { "Power must be greater than zero." }
        }.onFailure { throw InvalidInputException(it.message ?: "Invalid input") }
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
