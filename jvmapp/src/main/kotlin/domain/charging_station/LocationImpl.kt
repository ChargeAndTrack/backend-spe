package domain.charging_station

data class LocationImpl(
    override val longitude: Double,
    override val latitude: Double
) : Location
