package application.charging_station

import domain.charging_station.Recharge

/**
 * Repository for managing currently active recharges.
 */
interface ActiveRechargeRepository {

    /**
     * Retrieves an active recharge by car and charging station identifiers.
     *
     * @param carId the identifier of the car.
     * @param chargingStationId the identifier of the charging station.
     * @return the matching [Recharge] if found; otherwise null.
     */
    fun getRechargeByIds(carId: String, chargingStationId: String): Recharge?

    /**
     * Adds a recharge to the active recharges.
     *
     * @param recharge the recharge to store as active.
     */
    fun addRecharge(recharge: Recharge)

    /**
     * Removes an active recharge identified by car and charging station identifiers.
     *
     * @param carId the identifier of the car.
     * @param chargingStationId the identifier of the charging station.
     */
    fun removeRecharge(carId: String, chargingStationId: String)
}
