package domain.charging_station

data class LocationImpl(
    override val longitude: Double,
    override val latitude: Double
) : Location {

    init {
        require(longitude in -180.0..180.0) { "Longitude must be between -180 and 180 degrees" }
        require(latitude in -90.0..90.0) { "Latitude must be between -90 and 90 degrees" }
    }
}
