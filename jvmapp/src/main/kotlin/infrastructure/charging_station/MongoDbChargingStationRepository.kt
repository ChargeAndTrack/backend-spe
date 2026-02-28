package infrastructure.charging_station

import application.charging_station.ChargingStationRepository
import com.mongodb.client.model.Filters.*
import com.mongodb.client.model.FindOneAndReplaceOptions
import com.mongodb.client.model.ReturnDocument
import com.mongodb.client.model.geojson.Point
import com.mongodb.client.model.geojson.Position
import com.mongodb.client.result.InsertOneResult
import domain.charging_station.ChargingStation
import domain.charging_station.ClosestChargingStationInput
import domain.charging_station.NearbyChargingStationsInput
import infrastructure.MongoDb
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import org.bson.types.ObjectId
import java.lang.IllegalArgumentException

class MongoDbChargingStationRepository : ChargingStationRepository {

    private companion object {
        const val CHARGING_STATION_NOT_FOUND_MESSAGE = "Charging station not found"
        const val INVALID_REQUEST_DATA = "Invalid request data"
    }

    private val chargingStations = MongoDb.database.getCollection<ChargingStationDbEntity>("chargingStations")

    override fun getNewId(): String = ObjectId().toString()

    override suspend fun listChargingStations(): Collection<ChargingStation> {
        return chargingStations
            .find()
            .map { it.toDomain() }
            .toList()
    }

    override suspend fun addChargingStation(chargingStationToAdd: ChargingStation): ChargingStation {
        val newChargingStation = chargingStationToAdd.toDbEntity()
        chargingStations
            .insertOne(newChargingStation)
            .takeIf(InsertOneResult::wasAcknowledged)
            ?.let { return newChargingStation.toDomain() }
        throw IllegalArgumentException(INVALID_REQUEST_DATA)
    }

    override suspend fun getChargingStation(chargingStationId: String): ChargingStation {
        return (chargingStations
            .find(eq("_id", ObjectId(chargingStationId)))
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
                eq("_id", ObjectId(chargingStationId)),
                updatedChargingStation.toDbEntity(),
                FindOneAndReplaceOptions().returnDocument(ReturnDocument.AFTER)
            )?.toDomain()
            ?: throw IllegalArgumentException(CHARGING_STATION_NOT_FOUND_MESSAGE)
    }

    override suspend fun deleteChargingStation(chargingStationId: String) {
        if (!chargingStations
                .deleteOne(eq("_id", ObjectId(chargingStationId)))
                .wasAcknowledged()
        ) {
            throw IllegalArgumentException(CHARGING_STATION_NOT_FOUND_MESSAGE)
        }
    }

    override suspend fun getNearbyChargingStations(
        nearbyChargingStationsInput: NearbyChargingStationsInput
    ): Collection<ChargingStation> {
        return chargingStations
            .find(
                and(
                    listOfNotNull(
                        nearSphere(
                            "location",
                            Point(Position(
                                nearbyChargingStationsInput.longitude,
                                nearbyChargingStationsInput.latitude
                            )),
                            null,
                            null
                        ),
                        if (nearbyChargingStationsInput.onlyEnabled == true) eq("enabled", true) else null
                    )
                )
            ).map { it.toDomain() }
            .toList()
    }

    override suspend fun getClosestChargingStation(
        closestChargingStationInput: ClosestChargingStationInput
    ): ChargingStation {
        return chargingStations
            .find(
                and(
                    listOfNotNull(
                        nearSphere(
                            "location",
                            Point(Position(
                                closestChargingStationInput.longitude,
                                closestChargingStationInput.latitude)
                            ),
                            null,
                            null
                        )
                    ) + if (closestChargingStationInput.onlyEnabledAndAvailable == true) {
                        listOf(
                            eq("enabled", true),
                            eq("available", true)
                        )
                    } else {
                        emptyList()
                    }
                )
            ).firstOrNull()
            ?.toDomain()
            ?: throw IllegalArgumentException(CHARGING_STATION_NOT_FOUND_MESSAGE)
    }
}
