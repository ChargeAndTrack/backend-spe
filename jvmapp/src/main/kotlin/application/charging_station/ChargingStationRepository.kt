package application.charging_station

import domain.charging_station.ChargingStation
import domain.charging_station.ClosestChargingStationInput
import domain.charging_station.NearbyChargingStationsInput

interface ChargingStationRepository {
    fun getNewId(): String

    suspend fun listChargingStations(): Collection<ChargingStation>

    suspend fun addChargingStation(chargingStationToAdd: ChargingStation): ChargingStation

    suspend fun getChargingStation(chargingStationId: String): ChargingStation

    suspend fun updateChargingStation(
        chargingStationId: String,
        updatedChargingStation: ChargingStation
    ): ChargingStation

    suspend fun deleteChargingStation(chargingStationId: String)

    suspend fun getNearbyChargingStations(
        nearbyChargingStationsInput: NearbyChargingStationsInput
    ): Collection<ChargingStation>

    suspend fun getClosestChargingStation(closestChargingStationInput: ClosestChargingStationInput): ChargingStation
}
