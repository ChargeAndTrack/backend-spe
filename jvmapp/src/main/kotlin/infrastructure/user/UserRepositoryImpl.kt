package infrastructure.user

import application.user.UserRepository
import com.mongodb.client.model.Filters.*
import domain.user.Car
import domain.user.CarImpl
import domain.user.Role
import domain.user.User
import domain.user.UserImpl
import infrastructure.MongoDb
import kotlinx.coroutines.flow.firstOrNull
import org.bson.types.ObjectId

class UserRepositoryImpl : UserRepository {

    private val USER_NOT_FOUND_MESSAGE = "User not found"
    private val users = MongoDb.database.getCollection<UserDbEntity>("users")

    override suspend fun findUser(username: String, password: String): User {
        return (
                users
                    .find(
                        and(
                            eq<String>("username", username),
                            eq<String>("password", password)
                        )
                    ).firstOrNull()
                    ?: throw IllegalArgumentException(USER_NOT_FOUND_MESSAGE)
        ).toDomain()
    }

    override suspend fun getUser(userId: String): User {
        return (
            users
                .find(eq<ObjectId>("_id", ObjectId(userId)))
                .firstOrNull()
                ?: throw IllegalArgumentException(USER_NOT_FOUND_MESSAGE)
        ).toDomain()
    }

    private fun UserDbEntity.toDomain(): User =
        UserImpl(
            id = id.toString(),
            username = username,
            password = password,
            role = Role.valueOf(role),
            cars = cars.map { it.toDomain() }
        )

    private fun CarDbEntity.toDomain(): Car =
        CarImpl(
            id = id.toString(),
            plate = plate,
            maxBattery = maxBattery,
            currentBattery = currentBattery
        )

    private fun User.toDbEntity(): UserDbEntity =
        UserDbEntity(
            id = ObjectId(id),
            username = username,
            password = password,
            role = role.displayName,
            cars = cars.map { it.toDbEntity() }
        )

    private fun Car.toDbEntity(): CarDbEntity =
        CarDbEntity(
            id = ObjectId(id),
            plate = plate,
            maxBattery = maxBattery,
            currentBattery = currentBattery
        )
}