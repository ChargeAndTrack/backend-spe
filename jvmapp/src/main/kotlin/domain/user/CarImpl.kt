package domain.user

import domain.InvalidInputException

data class CarImpl(
    override val id: String,
    override val plate: String,
    override val maxBattery: Int,
    override val currentBattery: Int? = null
) : Car {
    init { validate() }

    override fun update(updateCarInput: UpdateCarInput): Car =
        copy(
            plate = updateCarInput.plate ?: plate,
            maxBattery = updateCarInput.maxBattery ?: maxBattery,
            currentBattery = updateCarInput.currentBattery ?: currentBattery
        )

    private fun validate() =
        runCatching {
            plate.also {
                require(it.length in 3..10 && it.matches(Regex("^[A-Z0-9 -]+$"))) { "Invalid plate format" }
            }
            maxBattery.also {
                require(it > 0) { "Invalid max battery, value must be a positive number" }
            }
            currentBattery?.also {
                require(it in 0..100) { "Invalid current battery, value must be between 0 and 100" }
            }
        }.onFailure { throw InvalidInputException(it.message ?: "Invalid input") }

    companion object Factory {
        fun create(id: String, addCarInput: AddCarInput): Car =
            CarImpl(
                id = id,
                plate = addCarInput.plate,
                maxBattery = addCarInput.maxBattery
            )
    }
}