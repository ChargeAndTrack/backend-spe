package domain.charging_station

import domain.InvalidInputException

data class LocationImpl(
    override val longitude: Double,
    override val latitude: Double
) : Location {

    init {
        runCatching {
            require(longitude in -180.0..180.0) { "Longitude must be between -180 and 180 degrees" }
            require(latitude in -90.0..90.0) { "Latitude must be between -90 and 90 degrees" }
        }.onFailure { throw InvalidInputException(it.message ?: "Invalid input") }
    }
}
