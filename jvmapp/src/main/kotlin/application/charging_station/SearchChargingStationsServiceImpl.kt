package application.charging_station

import domain.charging_station.ChargingStation
import domain.charging_station.ClosestChargingStationInput
import domain.charging_station.NearbyChargingStationsInput
import infrastructure.charging_station.ChargingStationsController
import infrastructure.charging_station.LocationController

class SearchChargingStationsServiceImpl(val queryParsingPort: QueryParsingPort) : SearchChargingStationsService {

    private companion object {
        const val DEFAULT_RADIUS = 5000.0
    }

    private val chargingStationService = ChargingStationsController.chargingStationService
    private val locationService = LocationController.locationService

    override suspend fun search(query: String): Collection<ChargingStation> {
        val chargingStationQuery = queryParsingPort.parse(query)
        println("ChargingStationQuery: $chargingStationQuery")
        val location = locationService.resolveAddressToLocationCoordinates(chargingStationQuery.address)
        println("Location: $location")
        return when (chargingStationQuery.intent) {
            Intent.NEAR -> {
                println("Near: $location with radius $DEFAULT_RADIUS")
                chargingStationService.getNearbyChargingStations(
                    NearbyChargingStationsInput(
                        location.longitude, location.latitude, DEFAULT_RADIUS
                    )
                )
            }
            Intent.CLOSEST -> {
                println("Closest: $location")
                listOf(chargingStationService.getClosestChargingStation(ClosestChargingStationInput(
                    location.longitude, location.latitude
                )))
            }
        }
    }
}