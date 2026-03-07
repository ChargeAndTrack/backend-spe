package infrastructure.charging_station

import application.charging_station.ChargingStationRepository
import com.mongodb.MongoException
import com.mongodb.client.model.Filters.*
import com.mongodb.client.model.FindOneAndReplaceOptions
import com.mongodb.client.model.ReturnDocument
import com.mongodb.client.model.geojson.Point
import com.mongodb.client.model.geojson.Position
import com.mongodb.client.result.InsertOneResult
import domain.InternalErrorException
import domain.NotFoundException
import domain.charging_station.ChargingStation
import domain.charging_station.ClosestChargingStationInput
import domain.charging_station.NearbyChargingStationsInput
import infrastructure.MongoDb
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import org.bson.types.ObjectId

class MongoDbChargingStationRepository : ChargingStationRepository {

    private companion object {
        const val CHARGING_STATION_NOT_FOUND_MESSAGE = "Charging station not found"
    }

    private val chargingStations = MongoDb.database.getCollection<ChargingStationDbEntity>("chargingStations")

    override fun getNewId(): String = ObjectId().toString()

    override suspend fun listChargingStations(): Collection<ChargingStation> = execute {
        chargingStations
            .find()
            .map { it.toDomain() }
            .toList()
    }

    override suspend fun addChargingStation(chargingStationToAdd: ChargingStation): ChargingStation = execute {
        val newChargingStation = chargingStationToAdd.toDbEntity()
        chargingStations
            .insertOne(newChargingStation)
            .takeIf(InsertOneResult::wasAcknowledged)
            ?.let { return@execute newChargingStation.toDomain() }
        throw InternalErrorException()
    }

    override suspend fun getChargingStation(chargingStationId: String): ChargingStation = execute {
        (chargingStations
            .find(eq("_id", ObjectId(chargingStationId)))
            .firstOrNull()
            ?: throw NotFoundException(CHARGING_STATION_NOT_FOUND_MESSAGE)
        ).toDomain()
    }

    override suspend fun updateChargingStation(
        chargingStationId: String,
        updatedChargingStation: ChargingStation
    ): ChargingStation = execute {
         chargingStations.findOneAndReplace(
             eq("_id", ObjectId(chargingStationId)),
             updatedChargingStation.toDbEntity(),
             FindOneAndReplaceOptions().returnDocument(ReturnDocument.AFTER)
         )?.toDomain() ?: throw NotFoundException(CHARGING_STATION_NOT_FOUND_MESSAGE)
    }

    override suspend fun deleteChargingStation(chargingStationId: String) = execute {
        if (!chargingStations
                .deleteOne(eq("_id", ObjectId(chargingStationId)))
                .wasAcknowledged()
        ) {
            throw NotFoundException(CHARGING_STATION_NOT_FOUND_MESSAGE)
        }
    }

    override suspend fun getNearbyChargingStations(
        nearbyChargingStationsInput: NearbyChargingStationsInput
    ): Collection<ChargingStation> = execute {
        chargingStations
            .find(
                and(
                    listOfNotNull(
                        nearSphere(
                            "location",
                            Point(
                                Position(
                                    nearbyChargingStationsInput.longitude,
                                    nearbyChargingStationsInput.latitude
                                )
                            ),
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
            ?: throw NotFoundException(CHARGING_STATION_NOT_FOUND_MESSAGE)
    }

    private suspend fun <T> execute(block: suspend () -> T): T =
        try {
            block()
        } catch (e: MongoException) {
            println("MongoDB exception: " + e.message)
            throw InternalErrorException()
        }
}
