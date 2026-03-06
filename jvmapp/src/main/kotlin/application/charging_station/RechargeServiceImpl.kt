package application.charging_station

import domain.charging_station.RechargeImpl
import domain.charging_station.StartRechargeInput
import domain.charging_station.StopRechargeInput

class RechargeServiceImpl(val rechargeRepository: RechargeRepository) : RechargeService {

    override suspend fun getChargingStationIdByCarId(carId: String): String =
        rechargeRepository.getChargingStationIdByCarId(carId)

    override suspend fun getCarIdByChargingStationId(chargingStationId: String): String =
        rechargeRepository.getCarIdByChargingStationId(chargingStationId)

    override suspend fun startRecharge(startRechargeInput: StartRechargeInput, chargingStationId: String) =
        rechargeRepository.startRecharge(RechargeImpl(startRechargeInput.carId, chargingStationId))

    override suspend fun stopRecharge(stopRechargeInput: StopRechargeInput, chargingStationId: String) =
        rechargeRepository.stopRecharge(RechargeImpl(stopRechargeInput.carId, chargingStationId))
}
