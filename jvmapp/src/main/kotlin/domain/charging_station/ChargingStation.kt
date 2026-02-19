package domain.charging_station

interface ChargingStation {
    val id: String
    var power: Int
    var available: Boolean
    var enabled: Boolean
    var location: Location
}