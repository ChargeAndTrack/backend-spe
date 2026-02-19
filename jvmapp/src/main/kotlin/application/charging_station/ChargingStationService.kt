package application.charging_station

import domain.charging_station.ChargingStation

interface ChargingStationService {
    suspend fun listChargingStations(): Collection<ChargingStation>

    suspend fun addChargingStation(chargingStationToAdd: ChargingStation): String

    suspend fun getChargingStation(chargingStationId: String): ChargingStation

    suspend fun updateChargingStation(chargingStationId: String): ChargingStation

    suspend fun deleteChargingStation(chargingStationId: String): Boolean
}
