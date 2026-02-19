package infrastructure.charging_station

import application.charging_station.ChargingStationRepository
import domain.charging_station.ChargingStation
import domain.charging_station.ChargingStationImpl
import domain.charging_station.Location
import domain.charging_station.LocationImpl
import infrastructure.MongoDb
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import org.bson.types.ObjectId

class MongoDbChargingStationRepository : ChargingStationRepository {

    private val chargingStations = MongoDb.database.getCollection<ChargingStationDbEntity>("chargingStations")

    override suspend fun listChargingStations(): Collection<ChargingStation> {
        return chargingStations
            .find()
            .map { it.toDomain() }
            .toList()
    }

    override suspend fun addChargingStation(chargingStationToAdd: ChargingStation): String {
        return chargingStations
            .insertOne(chargingStationToAdd.toDbEntity())
            .insertedId
            ?.asObjectId()
            ?.value
            ?.toHexString() ?: ""
    }

    override suspend fun getChargingStation(chargingStationId: String): ChargingStation {
        TODO("Not yet implemented")
    }

    override suspend fun updateChargingStation(chargingStationId: String): ChargingStation {
        TODO("Not yet implemented")
    }

    override suspend fun deleteChargingStation(chargingStationId: String): Boolean {
        TODO("Not yet implemented")
    }

    private fun ChargingStationDbEntity.toDomain(): ChargingStation = ChargingStationImpl(
        id = id.toString(),
        power = power,
        available = available,
        enabled = enabled,
        location = location.toDomain()
    )

    private fun LocationDbEntity.toDomain(): Location = LocationImpl(
        longitude = longitude,
        latitude = latitude
    )

    private fun ChargingStation.toDbEntity(): ChargingStationDbEntity = ChargingStationDbEntity(
        id = ObjectId(),
        power = power,
        available = available,
        enabled = enabled,
        location = location.toDbEntity()
    )

    private fun Location.toDbEntity(): LocationDbEntity = LocationDbEntity(
        longitude = longitude,
        latitude = latitude
    )
}