package domain.charging_station

data class ChargingStationImpl(
    override val id: String,
    override var power: Int,
    override var available: Boolean,
    override var enabled: Boolean,
    override var location: Location
) : ChargingStation
