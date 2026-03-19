package application.charging_station

import common.OutBoundPort

@OutBoundPort
interface QueryParsingPort {
    suspend fun parse(query: String): ChargingStationSearchQuery
}