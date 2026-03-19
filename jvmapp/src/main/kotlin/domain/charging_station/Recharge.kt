package domain.charging_station

/**
 * Represents the recharge value object of a car at a charging station.
 */
interface Recharge {
    /** Identifier of the car being recharged */
    val carId: String

    /** Identifier of the charging station being used */
    val chargingStationId: String

    /**
     * Registers an observer to be notified of recharge events.
     *
     * @param observer the observer to register.
     */
    fun addRechargeObserver(observer: RechargeObserver)

    /**
     * Unregisters an observer from recharge event notifications.
     *
     * @param observer the observer to unregister.
     */
    fun removeRechargeObserver(observer: RechargeObserver)

    /**
     * Starts the recharge process.
     *
     * @param userId the identifier of the user initiating the recharge.
     * @param startRechargeLogicInput the parameters needed to start the recharge process.
     */
    suspend fun start(userId: String, startRechargeLogicInput: StartRechargeLogicInput)

    /**
     * Stops the active recharge process.
     */
    suspend fun stop()
}