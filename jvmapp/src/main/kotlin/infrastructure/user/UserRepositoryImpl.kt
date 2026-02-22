package infrastructure.user

import AddCarInput
import application.user.UserRepository
import com.mongodb.client.model.Filters.*
import com.mongodb.client.model.Updates.*
import domain.user.Car
import domain.user.User
import infrastructure.MongoDb
import kotlinx.coroutines.flow.firstOrNull
import org.bson.types.ObjectId

class UserRepositoryImpl : UserRepository {

    private val USER_NOT_FOUND_MESSAGE = "User not found"
    private val CAR_NOT_FOUND_MESSAGE = "Car not found"
    private val CAR_NOT_DELETED_MESSAGE = "Couldn't delete car"

    private val users = MongoDb.database.getCollection<UserDbEntity>("users")

    override suspend fun findUser(username: String, password: String): User =
        users
            .find(
                and(
                    eq<String>("username", username),
                    eq<String>("password", password)
                )
            ).firstOrNull()
            ?.toDomain()
            ?: throw IllegalArgumentException(USER_NOT_FOUND_MESSAGE)

    private suspend fun getUserDbEntity(userId: String): UserDbEntity =
        users
            .find(eq<ObjectId>("_id", ObjectId(userId)))
            .firstOrNull()
            ?: throw IllegalArgumentException(USER_NOT_FOUND_MESSAGE)

    override suspend fun getUser(userId: String): User = getUserDbEntity(userId).toDomain()

    override suspend fun getCars(userId: String): Collection<Car> = getUser(userId).cars

    override suspend fun addCar(userId: String, addCarInput: AddCarInput): Car {
        val newCar = CarDbEntity(id = ObjectId(), plate = addCarInput.plate, maxBattery = addCarInput.maxBattery)
        users
            .updateOne(eq<ObjectId>("_id", ObjectId(userId)), push("cars", newCar))
            .takeIf { it.matchedCount > 0 }
            ?: throw IllegalArgumentException(USER_NOT_FOUND_MESSAGE)
        return newCar.toDomain()
    }

    override suspend fun getCar(userId: String, carId: String): Car =
        getCars(userId).find { it.id == carId } ?: throw IllegalArgumentException(CAR_NOT_FOUND_MESSAGE)

    override suspend fun updateCar(userId: String, carId: String): Car {
        TODO("Not yet implemented")
    }

    override suspend fun deleteCar(userId: String, carId: String): Collection<Car> {
        users.updateOne(
            eq<ObjectId>("_id", ObjectId(userId)),
            pull("cars", eq<ObjectId>("_id", ObjectId(carId)))
        )
        val cars = getCars(userId)
        if (cars.any { it.id == carId }) {
            throw IllegalStateException(CAR_NOT_DELETED_MESSAGE)
        }
        return cars
    }
}