package domain.user

import domain.user.Car.Factory.validate

data class CarImpl(
    override val id: String,
    override val plate: String,
    override val maxBattery: Int,
    override val currentBattery: Int? = null
) : Car {
    override fun update(updateCarInput: UpdateCarInput): Car {
        updateCarInput.validate()
        return copy(
            plate = updateCarInput.plate ?: plate,
            maxBattery = updateCarInput.maxBattery ?: maxBattery,
            currentBattery = updateCarInput.currentBattery ?: currentBattery
        )
    }
}