package application.user

import domain.user.Car

interface CarService {
    suspend fun getCars(userId: String): Collection<Car>
    suspend fun addCar(userId: String, addCarInput: AddCarInput): Car
    suspend fun getCar(userId: String, carId: String): Car
    suspend fun updateCar(userId: String, carId: String, updateCarInput: UpdateCarInput): Car
    suspend fun deleteCar(userId: String, carId: String): Collection<Car>
}