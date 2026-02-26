package domain.user

data class CarImpl(
    override val id: String,
    override val plate: String,
    override val maxBattery: Int,
    override val currentBattery: Int?
) : Car {
    override fun update(updateCarInput: UpdateCarInput): Car {
        updateCarInput.validate()
        return copy(
            plate = updateCarInput.plate ?: plate,
            maxBattery = updateCarInput.maxBattery ?: maxBattery,
            currentBattery = updateCarInput.currentBattery ?: currentBattery
        )
    }

    private fun UpdateCarInput.validate() {
        plate?.also { require(it.length in 3..10 && it.matches(Regex("^[A-Z0-9 -]+$"))) { "Invalid plate format" } }
        maxBattery?.also { require(it > 0) { "Invalid max battery, value must be a positive number" } }
        currentBattery?.also { require(it in 0..100) { "Invalid current battery, value must be between 0 and 100" } }
    }
}