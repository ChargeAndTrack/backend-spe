package infrastructure.user

import domain.user.Car
import domain.user.CarImpl
import domain.user.Role
import domain.user.User
import domain.user.UserImpl
import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.bson.types.ObjectId

@Serializable
data class UserDbEntity(
    @SerialName("_id")
    @Contextual val id: ObjectId,
    val username: String,
    val password: String,
    val role: String,
    val cars: List<CarDbEntity> = emptyList()
)

@Serializable
data class CarDbEntity(
    @SerialName("_id")
    @Contextual val id: ObjectId,
    val plate: String,
    val maxBattery: Int,
    val currentBattery: Int? = null
)

fun UserDbEntity.toDomain(): User =
    UserImpl(
        id = id.toString(),
        username = username,
        password = password,
        role = Role.valueOf(role),
        cars = cars.map { it.toDomain() }
    )

fun CarDbEntity.toDomain(): Car =
    CarImpl(
        id = id.toString(),
        plate = plate,
        maxBattery = maxBattery,
        currentBattery = currentBattery
    )

fun User.toDbEntity(): UserDbEntity =
    UserDbEntity(
        id = ObjectId(id),
        username = username,
        password = password,
        role = role.displayName,
        cars = cars.map { it.toDbEntity() }
    )

fun Car.toDbEntity(): CarDbEntity =
    CarDbEntity(
        id = ObjectId(id),
        plate = plate,
        maxBattery = maxBattery,
        currentBattery = currentBattery
    )