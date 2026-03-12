package application.charging_station

import domain.charging_station.ChargingStation
import domain.charging_station.ChargingStationFilter

interface ChargingStationSearchQuery {
    val intent: Intent
    val address: String
    val filters: Collection<ChargingStationFilter>
    fun filter(chargingStations: Collection<ChargingStation>): Collection<ChargingStation>
}

enum class Intent { NEAR, CLOSEST }