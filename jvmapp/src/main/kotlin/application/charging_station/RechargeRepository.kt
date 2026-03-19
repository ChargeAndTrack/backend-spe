package application.charging_station

import common.OutBoundPort
import domain.charging_station.Recharge

/**
 * Repository for persisting recharges and lookup operations.
 */
@OutBoundPort
interface RechargeRepository {

    /**
     * Finds the charging station which is recharging a car.
     *
     * @param carId car identifier.
     * @return charging station id, otherwise null.
     */
    suspend fun getChargingStationIdByCarId(carId: String): String?

    /**
     * Finds the car currently recharging at a charging station.
     *
     * @param chargingStationId charging station identifier.
     * @return car id, otherwise null.
     */
    suspend fun getCarIdByChargingStationId(chargingStationId: String): String?

    /**
     * Adds a new recharge.
     *
     * @param recharge recharge to add.
     */
    suspend fun addRecharge(recharge: Recharge)

    /**
     * Deletes a recharge.
     *
     * @param recharge recharge to delete.
     */
    suspend fun deleteRecharge(recharge: Recharge)

    /**
     * Starts a recharge process.
     *
     * @param recharge recharge to start.
     */
    suspend fun startRecharge(recharge: Recharge)

    /**
     * Stops a recharge process.
     *
     * @param recharge recharge to stop.
     */
    suspend fun stopRecharge(recharge: Recharge)
}
