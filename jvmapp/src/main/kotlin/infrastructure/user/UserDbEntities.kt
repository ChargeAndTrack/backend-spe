package infrastructure.user

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
    var cars: List<CarDbEntity> = emptyList()
)

@Serializable
data class CarDbEntity(
    @SerialName("_id")
    @Contextual val id: ObjectId,
    var plate: String,
    var maxBattery: Int,
    var currentBattery: Int? = null
)