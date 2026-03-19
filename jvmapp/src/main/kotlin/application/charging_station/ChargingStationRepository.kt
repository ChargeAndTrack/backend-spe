package application.charging_station

import common.OutBoundPort
import domain.charging_station.ChargingStation
import domain.charging_station.ClosestChargingStationInput
import domain.charging_station.NearbyChargingStationsInput

/**
 * Repository for persisting and retrieving charging stations.
 */
@OutBoundPort
interface ChargingStationRepository {

    /**
     * Generates a new unique identifier for a charging station.
     *
     * @return a new charging station identifier.
     */
    fun getNewId(): String

    /**
     * Retrieves all charging stations.
     *
     * @return all stored charging stations.
     */
    suspend fun listChargingStations(): Collection<ChargingStation>

    /**
     * Adds a new charging station.
     *
     * @param chargingStationToAdd charging station to add.
     * @return the added charging station.
     */
    suspend fun addChargingStation(chargingStationToAdd: ChargingStation): ChargingStation

    /**
     * Retrieves a charging station by its identifier.
     *
     * @param chargingStationId charging station identifier.
     * @return the matching charging station.
     */
    suspend fun getChargingStation(chargingStationId: String): ChargingStation

    /**
     * Updates an existing charging station.
     *
     * @param chargingStationId identifier of the station to update.
     * @param updatedChargingStation updated version of the charging station.
     * @return the updated charging station.
     */
    suspend fun updateChargingStation(
        chargingStationId: String,
        updatedChargingStation: ChargingStation
    ): ChargingStation

    /**
     * Deletes a charging station by its identifier.
     *
     * @param chargingStationId charging station identifier.
     */
    suspend fun deleteChargingStation(chargingStationId: String)

    /**
     * Retrieves charging stations near the given position and constraints.
     *
     * @param nearbyChargingStationsInput criteria for nearby station search.
     * @return charging stations that satisfy the nearby search.
     */
    suspend fun getNearbyChargingStations(
        nearbyChargingStationsInput: NearbyChargingStationsInput
    ): Collection<ChargingStation>

    /**
     * Retrieves the closest charging station for the given input.
     *
     * @param closestChargingStationInput criteria used to determine the closest station.
     * @return the closest charging station.
     */
    suspend fun getClosestChargingStation(closestChargingStationInput: ClosestChargingStationInput): ChargingStation
}
