package application.charging_station

import domain.charging_station.Recharge

interface RechargeEventObserver {
    suspend fun rechargeUpdate(recharge: Recharge, level: Int)
    suspend fun chargingStationUpdated(recharge: Recharge)
    suspend fun rechargeCompleted(recharge: Recharge)
}