package application.charging_station

import domain.charging_station.AddChargingStationInput
import domain.charging_station.ChargingStation
import domain.charging_station.UpdateChargingStationInput

class ChargingStationServiceImpl(val repository: ChargingStationRepository) : ChargingStationService {
    override suspend fun listChargingStations(): Collection<ChargingStation> =
        repository.listChargingStations()

    override suspend fun addChargingStation(chargingStationToAdd: AddChargingStationInput): ChargingStation =
        repository.addChargingStation(chargingStationToAdd)

    override suspend fun getChargingStation(chargingStationId: String): ChargingStation =
        repository.getChargingStation(chargingStationId)

    override suspend fun updateChargingStation(
        chargingStationId: String,
        updateChargingStationInput: UpdateChargingStationInput
    ): ChargingStation =
        repository.getChargingStation(chargingStationId).let {
            repository.updateChargingStation(
                chargingStationId,
                it.update(updateChargingStationInput)
            )
        }

    override suspend fun deleteChargingStation(chargingStationId: String) =
        repository.deleteChargingStation(chargingStationId)
}
