package domain.user

data class CarImpl(
    override val id: String,
    override var plate: String,
    override var maxBattery: Int,
    override var currentBattery: Int?
) : Car {
    override fun update(updateCarInput: UpdateCarInput): Car {
        return copy(
            plate = updateCarInput.plate ?: plate,
            maxBattery = updateCarInput.maxBattery ?: maxBattery,
            currentBattery = updateCarInput.currentBattery ?: currentBattery
        )
    }
}