package application.charging_station

import domain.charging_station.Address
import domain.charging_station.Location

interface GeocodingPort {
    suspend fun geocode(address: String): Location
    suspend fun reverseGeocode(location: Location): Address
}