package domain.charging_station

import domain.InvalidInputException

data class AddressImpl(
    override val street: String,
    override val houseNumber: String,
    override val city: String,
    override val postalCode: String,
    override val region: String,
    override val country: String
) : Address {
    init { validate() }

    private fun validate() =
        runCatching {
            require(street.isNotEmpty()) { "Street cannot be empty" }
        }.onFailure { throw InvalidInputException(it.message ?: "Invalid input") }
}