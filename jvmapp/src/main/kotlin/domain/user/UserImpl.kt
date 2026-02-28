package domain.user

import domain.InvalidInputException
import domain.NotFoundException

data class UserImpl(
    override val id: String,
    override val username: String,
    override val password: String,
    override val role: Role,
    override val cars: Collection<Car> = emptySet()
) : User {
    private val CAR_NOT_FOUND_MESSAGE = "Car not found"
    private val NOT_UNIQUE_PLATE_MESSAGE = "Car with the same plate already exists"

    override fun addCar(carId: String, addCarInput: AddCarInput): Car {
        requireUniquePlate(carId, addCarInput.plate)
        return CarImpl.create(carId, addCarInput)
    }

    override fun updateCar(carId: String, updateCarInput: UpdateCarInput): Car {
        requireUniquePlate(carId, updateCarInput.plate)
        return getCar(carId).update(updateCarInput)
    }

    private fun requireUniquePlate(currentCarId: String, newPlate: String?) =
        runCatching { require(isPlateUnique(currentCarId, newPlate)) { NOT_UNIQUE_PLATE_MESSAGE } }
            .onFailure { throw InvalidInputException(it.message ?: "Invalid input") }

    private fun isPlateUnique(currentCarId: String, newPlate: String?): Boolean =
        newPlate == null || cars.none { it.id != currentCarId && it.plate == newPlate }

    private fun getCar(carId: String): Car =
        cars.find { it.id == carId } ?: throw NotFoundException(CAR_NOT_FOUND_MESSAGE)
}
