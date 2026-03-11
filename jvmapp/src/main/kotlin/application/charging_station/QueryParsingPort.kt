package application.charging_station

interface QueryParsingPort {
    suspend fun parse(query: String): ChargingStationSearchQuery
}