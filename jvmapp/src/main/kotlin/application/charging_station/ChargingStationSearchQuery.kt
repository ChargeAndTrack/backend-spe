package application.charging_station

import domain.charging_station.ChargingStation

interface ChargingStationSearchQuery {
    val intent: Intent
    val address: String
    fun filter(chargingStations: Collection<ChargingStation>): Collection<ChargingStation>
}

enum class Intent { NEAR, CLOSEST }