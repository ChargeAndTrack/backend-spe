package domain.charging_station

interface ChargingStationFilter {
    fun matches(chargingStation: ChargingStation): Boolean
}

data class MinPowerKwFilter(val minPowerKw: Int) : ChargingStationFilter {
    override fun matches(chargingStation: ChargingStation): Boolean = chargingStation.power >= minPowerKw
}