package application.charging_station

import domain.charging_station.ChargingStation

interface SearchChargingStationsService {
    suspend fun search(query: String): Collection<ChargingStation>
}