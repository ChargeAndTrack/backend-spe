package application.charging_station

import common.InBoundPort
import domain.charging_station.ChargingStation

@InBoundPort
interface SearchChargingStationsService {
    suspend fun search(query: String): Collection<ChargingStation>
}