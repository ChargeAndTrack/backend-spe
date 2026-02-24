package infrastructure.charging_station

import domain.charging_station.AddChargingStationInput
import application.charging_station.ChargingStationRepository
import com.mongodb.client.model.Filters.eq
import com.mongodb.client.model.FindOneAndReplaceOptions
import com.mongodb.client.model.ReturnDocument
import com.mongodb.client.result.InsertOneResult
import domain.charging_station.ChargingStation
import infrastructure.MongoDb
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import org.bson.types.ObjectId
import java.lang.IllegalArgumentException

class MongoDbChargingStationRepository : ChargingStationRepository {

    private val CHARGING_STATION_NOT_FOUND_MESSAGE = "Charging station not found"
    private val INVALID_REQUEST_DATA = "Invalid request data"

    private val chargingStations = MongoDb.database.getCollection<ChargingStationDbEntity>("chargingStations")

    override suspend fun listChargingStations(): Collection<ChargingStation> {
        return chargingStations
            .find()
            .map { it.toDomain() }
            .toList()
    }

    override suspend fun addChargingStation(chargingStationToAdd: AddChargingStationInput): ChargingStation {
        val newChargingStation = chargingStationToAdd.toDbEntity()
        chargingStations
            .insertOne(newChargingStation)
            .takeIf(InsertOneResult::wasAcknowledged)
            ?.let { return newChargingStation.toDomain() }
        throw IllegalArgumentException(INVALID_REQUEST_DATA)
    }

    override suspend fun getChargingStation(chargingStationId: String): ChargingStation {
        return (chargingStations
            .find(eq<ObjectId>("_id", ObjectId(chargingStationId)))
            .firstOrNull()
            ?: throw IllegalArgumentException(CHARGING_STATION_NOT_FOUND_MESSAGE)
        ).toDomain()
    }

    override suspend fun updateChargingStation(
        chargingStationId: String,
        updatedChargingStation: ChargingStation
    ): ChargingStation {
        return chargingStations
            .findOneAndReplace(
                eq<ObjectId>("_id", ObjectId(chargingStationId)),
                updatedChargingStation.toDbEntity(),
                FindOneAndReplaceOptions().returnDocument(ReturnDocument.AFTER)
            )?.toDomain()
            ?: throw IllegalArgumentException(CHARGING_STATION_NOT_FOUND_MESSAGE)
    }

    override suspend fun deleteChargingStation(chargingStationId: String) {
        if (!chargingStations
                .deleteOne(eq<ObjectId>("_id", ObjectId(chargingStationId)))
                .wasAcknowledged()
        ) {
            throw IllegalArgumentException(CHARGING_STATION_NOT_FOUND_MESSAGE)
        }
    }
}
