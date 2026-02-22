package application.user

import AddCarInput
import domain.user.Car
import domain.user.User

interface UserRepository {
    suspend fun findUser(username: String, password: String): User
    suspend fun getUser(userId: String): User
    suspend fun getCars(userId: String): Collection<Car>
    suspend fun addCar(userId: String, addCarInput: AddCarInput): Car
    suspend fun getCar(userId: String, carId: String): Car
    suspend fun updateCar(userId: String, carId: String): Car   // TODO add updateCarInput
    suspend fun deleteCar(userId: String, carId: String): Collection<Car>
}