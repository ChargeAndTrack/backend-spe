package application.charging_station

import common.InBoundPort
import domain.charging_station.RechargeObserver
import domain.charging_station.StartRechargeLogicInput
import domain.charging_station.StartRechargeInput
import domain.charging_station.StopRechargeInput

@InBoundPort
interface RechargeService : RechargeObserver {

    suspend fun getChargingStationIdByCarId(carId: String): String?

    suspend fun getCarIdByChargingStationId(chargingStationId: String): String?

    suspend fun startRecharge(
        userId: String,
        startRechargeInput: StartRechargeInput,
        startRechargeLogicInput: StartRechargeLogicInput,
        chargingStationId: String
    )

    suspend fun stopRecharge(stopRechargeInput: StopRechargeInput, chargingStationId: String)
}
