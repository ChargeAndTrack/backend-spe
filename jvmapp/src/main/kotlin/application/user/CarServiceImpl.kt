package application.user

import domain.user.AddCarInput
import domain.user.Car
import domain.user.UpdateCarInput

class CarServiceImpl(private val userRepository: UserRepository) : CarService {

    override suspend fun getCars(userId: String): Collection<Car> = userRepository.getCars(userId)

    override suspend fun addCar(userId: String, addCarInput: AddCarInput): Car =
        userRepository.addCar(userId, addCarInput)

    override suspend fun getCar(userId: String, carId: String): Car = userRepository.getCar(userId, carId)

    override suspend fun updateCar(userId: String, carId: String, updateCarInput: UpdateCarInput): Car {
        val updatedCar: Car = getCar(userId, carId).update(updateCarInput)
        userRepository.updateCar(userId, updatedCar)
        return updatedCar
    }

    override suspend fun deleteCar(userId: String, carId: String): Collection<Car> =
        userRepository.deleteCar(userId, carId)
}