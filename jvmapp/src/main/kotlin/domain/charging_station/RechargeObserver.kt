package domain.charging_station

interface RechargeObserver {
    suspend fun notifyRechargeEvent(rechargeEvent: RechargeEvent)
}