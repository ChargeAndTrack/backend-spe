package application.charging_station

import domain.charging_station.StartRechargeInput
import domain.charging_station.StopRechargeInput

interface RechargeService {

    suspend fun getChargingStationIdByCarId(carId: String): String

    suspend fun getCarIdByChargingStationId(chargingStationId: String): String

    suspend fun startRecharge(startRechargeInput: StartRechargeInput, chargingStationId: String)

    suspend fun stopRecharge(stopRechargeInput: StopRechargeInput, chargingStationId: String)
}
