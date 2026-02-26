package application.charging_station

import domain.charging_station.AddChargingStationInput
import domain.charging_station.ChargingStation
import domain.charging_station.ClosestChargingStationInput
import domain.charging_station.NearbyChargingStationsInput
import domain.charging_station.UpdateChargingStationInput

interface ChargingStationService {
    suspend fun listChargingStations(): Collection<ChargingStation>

    suspend fun addChargingStation(chargingStationToAdd: AddChargingStationInput): ChargingStation

    suspend fun getChargingStation(chargingStationId: String): ChargingStation

    suspend fun updateChargingStation(
        chargingStationId: String,
        updateChargingStationInput: UpdateChargingStationInput
    ): ChargingStation

    suspend fun deleteChargingStation(chargingStationId: String)

    suspend fun getNearbyChargingStations(
        nearbyChargingStationsInput: NearbyChargingStationsInput
    ): Collection<ChargingStation>

    suspend fun getClosestChargingStation(closestChargingStationInput: ClosestChargingStationInput): ChargingStation
}
