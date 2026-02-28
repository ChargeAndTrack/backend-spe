package application.charging_station

import domain.charging_station.AddChargingStationInput
import domain.charging_station.ChargingStation
import domain.charging_station.ChargingStationImpl
import domain.charging_station.ClosestChargingStationInput
import domain.charging_station.NearbyChargingStationsInput
import domain.charging_station.UpdateChargingStationInput

class ChargingStationServiceImpl(val repository: ChargingStationRepository) : ChargingStationService {
    override suspend fun listChargingStations(): Collection<ChargingStation> =
        repository.listChargingStations()

    override suspend fun addChargingStation(chargingStationToAdd: AddChargingStationInput): ChargingStation =
        repository.addChargingStation(
            ChargingStationImpl.create(repository.getNewId(), chargingStationToAdd)
        )

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

    override suspend fun getNearbyChargingStations(
        nearbyChargingStationsInput: NearbyChargingStationsInput
    ): Collection<ChargingStation> =
        repository.getNearbyChargingStations(nearbyChargingStationsInput)

    override suspend fun getClosestChargingStation(
        closestChargingStationInput: ClosestChargingStationInput
    ): ChargingStation =
        repository.getClosestChargingStation(closestChargingStationInput)
}
