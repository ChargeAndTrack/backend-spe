package domain.charging_station

interface Address {
    val street: String
    val houseNumber: String
    val city: String
    val postalCode: String
    val region: String
    val country: String
}