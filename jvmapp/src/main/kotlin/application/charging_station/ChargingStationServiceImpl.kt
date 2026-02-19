package application.charging_station

import domain.charging_station.ChargingStation

class ChargingStationServiceImpl(val repository: ChargingStationRepository) : ChargingStationService {
    override suspend fun listChargingStations(): Collection<ChargingStation> =
        this.repository.listChargingStations()

    override suspend fun addChargingStation(chargingStationToAdd: ChargingStation): String =
        this.repository.addChargingStation(chargingStationToAdd)

    override suspend fun getChargingStation(chargingStationId: String): ChargingStation =
        this.repository.getChargingStation(chargingStationId)

    override suspend fun updateChargingStation(chargingStationId: String): ChargingStation =
        this.repository.updateChargingStation(chargingStationId)

    override suspend fun deleteChargingStation(chargingStationId: String): Boolean =
        this.repository.deleteChargingStation(chargingStationId)
}
