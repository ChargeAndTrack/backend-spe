package application.charging_station

import domain.charging_station.ChargingStation
import domain.charging_station.ChargingStationFilter

data class ChargingStationSearchQueryImpl(
    override val intent: Intent,
    override val address: String,
    private val filters: Collection<ChargingStationFilter> = emptyList()
) : ChargingStationSearchQuery {
    override fun filter(chargingStations: Collection<ChargingStation>): Collection<ChargingStation> =
        chargingStations.filter { chargingStation -> filters.all { it.matches(chargingStation) } }
}