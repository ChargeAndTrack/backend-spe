package application.user

import common.InBoundPort
import domain.user.AddCarInput
import domain.user.Car
import domain.user.IncrementCarBatteryInput
import domain.user.UpdateCarInput

@InBoundPort
interface CarService {
    suspend fun getCars(userId: String): Collection<Car>
    suspend fun addCar(userId: String, addCarInput: AddCarInput): Car
    suspend fun getCar(userId: String, carId: String): Car
    suspend fun updateCar(userId: String, carId: String, updateCarInput: UpdateCarInput): Car
    suspend fun incrementCarBattery(
        userId: String,
        carId: String,
        incrementCarBatteryInput: IncrementCarBatteryInput
    ): Car
    suspend fun deleteCar(userId: String, carId: String): Collection<Car>
}