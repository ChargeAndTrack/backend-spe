package infrastructure.user

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

class MongoDbUserRepository : UserRepository {

    private companion object {
        const val USER_NOT_FOUND_MESSAGE = "User not found"
        const val CAR_NOT_FOUND_MESSAGE = "Car not found"
        const val CAR_NOT_DELETED_MESSAGE = "Couldn't delete car"
        const val OPERATION_FAILED_MESSAGE = "An unexpected error occurred while performing the operation"
    }

    private val users = MongoDb.database.getCollection<UserDbEntity>("users")

    override fun getNewId(): String = ObjectId().toString()

    override suspend fun findUser(username: String, password: String): User = execute {
        users
            .find(
                and(
                    eq("username", username),
                    eq("password", password)
                )
            ).firstOrNull()
            ?.toDomain()
            ?: throw NotFoundException(USER_NOT_FOUND_MESSAGE)
    }

    private suspend fun getUserDbEntity(userId: String): UserDbEntity = execute {
        users
            .find(eq("_id", ObjectId(userId)))
            .firstOrNull()
            ?: throw NotFoundException(USER_NOT_FOUND_MESSAGE)
    }

    override suspend fun getUser(userId: String): User = getUserDbEntity(userId).toDomain()

    override suspend fun getCars(userId: String): Collection<Car> = getUser(userId).cars

    override suspend fun addCar(userId: String, newCar: Car) = execute {
        users
            .updateOne(eq("_id", ObjectId(userId)), push("cars", newCar.toDbEntity()))
            .also { if (it.matchedCount == 0L) { throw NotFoundException(USER_NOT_FOUND_MESSAGE) } }
            .let {}
    }

    override suspend fun getCar(userId: String, carId: String): Car =
        getCars(userId).find { it.id == carId } ?: throw NotFoundException(CAR_NOT_FOUND_MESSAGE)

    override suspend fun updateCar(userId: String, updatedCar: Car) = execute {
        users
            .findOneAndUpdate(
                and(
                    eq("_id", ObjectId(userId)),
                    eq("cars._id", ObjectId(updatedCar.id))
                ),
                set("cars.$", updatedCar.toDbEntity())
            ).let {}
    }

    override suspend fun deleteCar(userId: String, carId: String): Collection<Car> = execute {
        users.updateOne(
            eq("_id", ObjectId(userId)),
            pull("cars", eq("_id", ObjectId(carId)))
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