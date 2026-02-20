package infrastructure.charging_station

import application.charging_station.ChargingStationRepository
import domain.charging_station.ChargingStation
import infrastructure.MongoDb
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList

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
}
