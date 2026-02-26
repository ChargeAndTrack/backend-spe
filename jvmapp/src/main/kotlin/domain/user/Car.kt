package domain.user

import domain.InvalidInputException

interface Car {
    val id: String
    val plate: String
    val maxBattery: Int
    val currentBattery: Int?
    fun update(updateCarInput: UpdateCarInput): Car

    companion object Factory {
        fun create(id: String, addCarInput: AddCarInput): Car {
            addCarInput.validate()
            return CarImpl(
                id = id,
                plate = addCarInput.plate,
                maxBattery = addCarInput.maxBattery
            )
        }

        fun UpdateCarInput.validate() = validate(plate, maxBattery, currentBattery)

        private fun validate(plate: String? = null, maxBattery: Int? = null, currentBattery: Int? = null) =
            runCatching {
                plate?.also {
                    require(it.length in 3..10 && it.matches(Regex("^[A-Z0-9 -]+$"))) { "Invalid plate format" }
                }
                maxBattery?.also {
                    require(it > 0) { "Invalid max battery, value must be a positive number" }
                }
                currentBattery?.also {
                    require(it in 0..100) { "Invalid current battery, value must be between 0 and 100" }
                }
            }.onFailure { throw InvalidInputException(it.message ?: "Invalid input") }

        private fun AddCarInput.validate() = validate(plate = plate, maxBattery = maxBattery)
    }
}