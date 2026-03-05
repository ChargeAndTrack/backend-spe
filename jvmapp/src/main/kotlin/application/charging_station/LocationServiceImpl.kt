package application.charging_station

import domain.charging_station.Address
import domain.charging_station.Location

class LocationServiceImpl(private val geocodingPort: GeocodingPort): LocationService {

    override suspend fun resolveAddressToLocationCoordinates(address: String): Location =
        geocodingPort.geocode(address)

    override suspend fun reverseLocationCoordinatesToAddress(location: Location): Address =
        geocodingPort.reverseGeocode(location)
}