package application.charging_station

import common.InBoundPort
import domain.charging_station.AddChargingStationInput
import domain.charging_station.ChargingStation
import domain.charging_station.ClosestChargingStationInput
import domain.charging_station.NearbyChargingStationsInput
import domain.charging_station.UpdateChargingStationInput

/**
 * Application service exposing charging station use cases.
 */
@InBoundPort
interface ChargingStationService {

    /**
     * Retrieves all charging stations.
     *
     * @return all charging stations.
     */
    suspend fun listChargingStations(): Collection<ChargingStation>

    /**
     * Creates a new charging station.
     *
     * @param chargingStationToAdd input data used to create a station.
     * @return the created charging station.
     */
    suspend fun addChargingStation(chargingStationToAdd: AddChargingStationInput): ChargingStation

    /**
     * Retrieves a charging station by id.
     *
     * @param chargingStationId charging station identifier.
     * @return the matching charging station.
     */
    suspend fun getChargingStation(chargingStationId: String): ChargingStation

    /**
     * Updates an existing charging station.
     *
     * @param chargingStationId charging station identifier.
     * @param updateChargingStationInput input data for the update.
     * @return the updated charging station.
     */
    suspend fun updateChargingStation(
        chargingStationId: String,
        updateChargingStationInput: UpdateChargingStationInput
    ): ChargingStation

    /**
     * Deletes a charging station.
     *
     * @param chargingStationId charging station identifier.
     */
    suspend fun deleteChargingStation(chargingStationId: String)

    /**
     * Returns charging stations near a given location.
     *
     * @param nearbyChargingStationsInput nearby-search parameters.
     * @return a collection of nearby charging stations.
     */
    suspend fun getNearbyChargingStations(
        nearbyChargingStationsInput: NearbyChargingStationsInput
    ): Collection<ChargingStation>

    /**
     * Returns the closest charging station for the provided input.
     *
     * @param closestChargingStationInput closest-search parameters.
     * @return the closest charging station.
     */
    suspend fun getClosestChargingStation(closestChargingStationInput: ClosestChargingStationInput): ChargingStation
}
