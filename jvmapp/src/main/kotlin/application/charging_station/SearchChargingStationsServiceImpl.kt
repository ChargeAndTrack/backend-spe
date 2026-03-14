package application.charging_station

import domain.charging_station.ChargingStation
import domain.charging_station.ClosestChargingStationInput
import domain.charging_station.NearbyChargingStationsInput

class SearchChargingStationsServiceImpl(
    val chargingStationService: ChargingStationService,
    val locationService: LocationService,
    val queryParsingPort: QueryParsingPort
) : SearchChargingStationsService {

    private companion object {
        const val DEFAULT_RADIUS = 5000.0
    }

    override suspend fun search(query: String): Collection<ChargingStation> {
        val searchQuery = queryParsingPort.parse(query)
        val location = locationService.resolveAddressToLocationCoordinates(searchQuery.address)
        return when (searchQuery.intent) {
            Intent.NEAR -> searchQuery.filter(
                chargingStationService.getNearbyChargingStations(
                    NearbyChargingStationsInput(location.longitude, location.latitude, DEFAULT_RADIUS)
                )
            )
            Intent.CLOSEST -> listOf(
                chargingStationService.getClosestChargingStation(
                    ClosestChargingStationInput(location.longitude, location.latitude, filters = searchQuery.filters)
                )
            )
        }
    }
}