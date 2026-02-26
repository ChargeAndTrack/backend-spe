package infrastructure.user

import domain.user.AddCarInput
import application.user.UserRepository
import com.mongodb.MongoException
import com.mongodb.client.model.Filters.*
import com.mongodb.client.model.Updates.*
import domain.InternalErrorException
import domain.NotFoundException
import domain.user.Car
import domain.user.User
import infrastructure.MongoDb
import kotlinx.coroutines.flow.firstOrNull
import org.bson.types.ObjectId

class UserRepositoryImpl : UserRepository {

    private val USER_NOT_FOUND_MESSAGE = "User not found"
    private val CAR_NOT_FOUND_MESSAGE = "Car not found"
    private val CAR_NOT_DELETED_MESSAGE = "Couldn't delete car"
    private val OPERATION_FAILED_MESSAGE = "An unexpected error occurred while performing the operation"

    private val users = MongoDb.database.getCollection<UserDbEntity>("users")

    override suspend fun findUser(username: String, password: String): User = execute {
        users
            .find(
                and(
                    eq<String>("username", username),
                    eq<String>("password", password)
                )
            ).firstOrNull()
            ?.toDomain()
            ?: throw NotFoundException(USER_NOT_FOUND_MESSAGE)
    }

    private suspend fun getUserDbEntity(userId: String): UserDbEntity = execute {
        users
            .find(eq<ObjectId>("_id", ObjectId(userId)))
            .firstOrNull()
            ?: throw NotFoundException(USER_NOT_FOUND_MESSAGE)
    }

    override suspend fun getUser(userId: String): User = getUserDbEntity(userId).toDomain()

    override suspend fun getCars(userId: String): Collection<Car> = getUser(userId).cars

    override suspend fun addCar(userId: String, addCarInput: AddCarInput): Car = execute {
        val newCar = CarDbEntity(id = ObjectId(), plate = addCarInput.plate, maxBattery = addCarInput.maxBattery)
        users
            .updateOne(eq<ObjectId>("_id", ObjectId(userId)), push("cars", newCar))
            .takeIf { it.matchedCount > 0 }
            ?: throw NotFoundException(USER_NOT_FOUND_MESSAGE)
        newCar.toDomain()
    }

    override suspend fun getCar(userId: String, carId: String): Car =
        getCars(userId).find { it.id == carId } ?: throw NotFoundException(CAR_NOT_FOUND_MESSAGE)

    override suspend fun updateCar(userId: String, updatedCar: Car) = execute {
        users
            .findOneAndUpdate(
                and(
                    eq<ObjectId>("_id", ObjectId(userId)),
                    eq<ObjectId>("cars._id", ObjectId(updatedCar.id))
                ),
                set("cars.$", updatedCar.toDbEntity())
            ).let {}
    }

    override suspend fun deleteCar(userId: String, carId: String): Collection<Car> = execute {
        users.updateOne(
            eq<ObjectId>("_id", ObjectId(userId)),
            pull("cars", eq<ObjectId>("_id", ObjectId(carId)))
        )
        getCars(userId).takeIf { it.none { it.id == carId } } ?: throw InternalErrorException(CAR_NOT_DELETED_MESSAGE)
    }

    private suspend fun <T> execute(block: suspend () -> T): T =
        try {
            block()
        } catch (e: MongoException) {
            println("MongoDB exception: " + e.message)
            throw InternalErrorException(OPERATION_FAILED_MESSAGE)
        }
}