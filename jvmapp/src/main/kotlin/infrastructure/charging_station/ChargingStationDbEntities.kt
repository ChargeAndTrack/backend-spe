package infrastructure.charging_station

import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.bson.types.ObjectId

@Serializable
data class ChargingStationDbEntity(
    @SerialName("_id")
    @Contextual val id: ObjectId,
    var power: Int,
    var available: Boolean = true,
    var enabled: Boolean = true,
    var location: LocationDbEntity,
    var currentCarId: String? = null
)

@Serializable
data class LocationDbEntity(
    val longitude: Double,
    val latitude: Double
)
