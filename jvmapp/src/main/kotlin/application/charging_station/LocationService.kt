package application.charging_station

import common.InBoundPort
import domain.charging_station.Address
import domain.charging_station.Location

@InBoundPort
interface LocationService {
    suspend fun resolveAddressToLocationCoordinates(address: String): Location
    suspend fun reverseLocationCoordinatesToAddress(location: Location): Address
}