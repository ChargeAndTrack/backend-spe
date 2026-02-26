package application.user

import domain.user.Car
import domain.user.User

interface UserRepository {
    fun getNewId(): String
    suspend fun findUser(username: String, password: String): User
    suspend fun getUser(userId: String): User
    suspend fun getCars(userId: String): Collection<Car>
    suspend fun addCar(userId: String, newCar: Car)
    suspend fun getCar(userId: String, carId: String): Car
    suspend fun updateCar(userId: String, updatedCar: Car)
    suspend fun deleteCar(userId: String, carId: String): Collection<Car>
}