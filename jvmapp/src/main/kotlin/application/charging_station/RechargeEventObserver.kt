package application.charging_station

import common.OutBoundPort
import domain.charging_station.Recharge

@OutBoundPort
interface RechargeEventObserver {
    suspend fun rechargeUpdate(recharge: Recharge, level: Int)
    suspend fun chargingStationUpdated(recharge: Recharge)
    suspend fun rechargeCompleted(recharge: Recharge)
}