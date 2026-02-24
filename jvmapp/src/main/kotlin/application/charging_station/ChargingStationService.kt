package application.charging_station

import domain.charging_station.AddChargingStationInput
import domain.charging_station.ChargingStation
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
}
