package domain.charging_station

interface ChargingStation {
    val id: String
    val power: Int
    val available: Boolean
    val enabled: Boolean
    val location: Location

    fun update(updateChargingStationInput: UpdateChargingStationInput): ChargingStation
}