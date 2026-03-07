package infrastructure.charging_station

import domain.charging_station.Address
import kotlinx.serialization.Serializable

@Serializable
data class AddressDTO(
    val street: String,
    val houseNumber: String,
    val city: String,
    val postalCode: String,
    val region: String,
    val country: String
)

fun Address.toDTO(): AddressDTO =
    AddressDTO(
        street = street,
        houseNumber = houseNumber,
        city = city,
        postalCode = postalCode,
        region = region,
        country = country
    )