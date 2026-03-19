package domain.charging_station

/**
 * Represents an observer that listens to recharge events.
 */
interface RechargeObserver {

    /**
     * Notifies the observer that a new recharge domain event has occurred.
     *
     * @param rechargeEvent the recharge event.
     */
    suspend fun notifyRechargeEvent(rechargeEvent: RechargeEvent)
}