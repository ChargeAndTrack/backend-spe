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
    override fun updateCar(carId: String, updateCarInput: UpdateCarInput): Car {
        runCatching {
            require(isNewPlateNotPresent(carId, updateCarInput.plate)) {
                "Car with the same plate already exists"
            }
        }.onFailure { throw InvalidInputException(it.message ?: "Invalid input") }
        return getCar(carId).update(updateCarInput)
    }

    private fun isNewPlateNotPresent(currentCarId: String, newPlate: String?): Boolean =
        newPlate == null || cars.none { it.id != currentCarId && it.plate == newPlate }

    private fun getCar(carId: String): Car =
        cars.find { it.id == carId } ?: throw NotFoundException("Car not found")
}
