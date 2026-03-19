package application.charging_station

import common.OutBoundPort
import domain.charging_station.Recharge

@OutBoundPort
interface RechargeRepository {
    suspend fun getChargingStationIdByCarId(carId: String): String?

    suspend fun getCarIdByChargingStationId(chargingStationId: String): String?

    suspend fun addRecharge(recharge: Recharge)

    suspend fun deleteRecharge(recharge: Recharge)

    suspend fun startRecharge(recharge: Recharge)

    suspend fun stopRecharge(recharge: Recharge)
}