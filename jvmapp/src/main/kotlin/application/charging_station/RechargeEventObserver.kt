package application.charging_station

import common.OutBoundPort
import domain.charging_station.Recharge

/**
 * Observer contract for receiving recharge-related events.
 */
@OutBoundPort
interface RechargeEventObserver {

    /**
     * The battery of a charging station in recharge has been updated.
     *
     * @param recharge recharge updated.
     * @param level current recharge level.
     */
    suspend fun rechargeUpdate(recharge: Recharge, level: Int)

    /**
     * The related charging station state has been updated.
     *
     * @param recharge recharge during which the charging station has been updated.
     */
    suspend fun chargingStationUpdated(recharge: Recharge)

    /**
     * A recharge has completed.
     *
     * @param recharge recharge completed.
     */
    suspend fun rechargeCompleted(recharge: Recharge)
}
