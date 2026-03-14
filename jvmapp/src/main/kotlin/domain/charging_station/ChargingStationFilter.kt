package domain.charging_station

import domain.InvalidInputException

interface ChargingStationFilter {
    fun matches(chargingStation: ChargingStation): Boolean
}

data class MinPowerKwFilter(val minPowerKw: Int) : ChargingStationFilter {
    init {
        runCatching {
            require(minPowerKw > 0) { "Minimum power must be a positive number" }
        }.onFailure { throw InvalidInputException(it.message ?: "Invalid input") }.let {}
    }

    override fun matches(chargingStation: ChargingStation): Boolean = chargingStation.power >= minPowerKw
}