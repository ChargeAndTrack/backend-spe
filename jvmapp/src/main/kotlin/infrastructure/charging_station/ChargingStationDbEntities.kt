package infrastructure.charging_station

import domain.charging_station.AddChargingStationInput
import domain.charging_station.ChargingStation
import domain.charging_station.ChargingStationImpl
import domain.charging_station.Location
import domain.charging_station.LocationImpl
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
    var location: LocationDbEntity
)

@Serializable
data class LocationDbEntity(
    val longitude: Double,
    val latitude: Double
)

fun ChargingStation.toDbEntity(): ChargingStationDbEntity = ChargingStationDbEntity(
    id = ObjectId(id),
    power = power,
    available = available,
    enabled = enabled,
    location = location.toDbEntity()
)

fun ChargingStationDbEntity.toDomain(): ChargingStation = ChargingStationImpl(
    id = id.toString(),
    power = power,
    available = available,
    enabled = enabled,
    location = location.toDomain()
)

fun Location.toDbEntity(): LocationDbEntity = LocationDbEntity(
    longitude = longitude,
    latitude = latitude
)

fun LocationDbEntity.toDomain(): Location = LocationImpl(
    longitude = longitude,
    latitude = latitude
)

fun AddChargingStationInput.toDbEntity(): ChargingStationDbEntity = ChargingStationDbEntity(
    id = ObjectId(),
    power = power,
    location = location.toDbEntity()
)
