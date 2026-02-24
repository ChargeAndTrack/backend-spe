package application.charging_station

import domain.charging_station.AddChargingStationInput
import domain.charging_station.ChargingStation

interface ChargingStationRepository {
    suspend fun listChargingStations(): Collection<ChargingStation>

    suspend fun addChargingStation(chargingStationToAdd: AddChargingStationInput): ChargingStation

    suspend fun getChargingStation(chargingStationId: String): ChargingStation

    suspend fun updateChargingStation(
        chargingStationId: String,
        updatedChargingStation: ChargingStation
    ): ChargingStation

    suspend fun deleteChargingStation(chargingStationId: String)
}
