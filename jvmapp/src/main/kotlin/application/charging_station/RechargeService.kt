package application.charging_station

import common.InBoundPort
import domain.charging_station.RechargeObserver
import domain.charging_station.StartRechargeLogicInput
import domain.charging_station.StartRechargeInput
import domain.charging_station.StopRechargeInput

/**
 * Application service for handling recharge operations.
 *
 * Extends [RechargeObserver] to observe to recharge process events.
 */
@InBoundPort
interface RechargeService : RechargeObserver {

    /**
     * Retrieves the charging station currently recharging the specified car.
     *
     * @param carId car identifier.
     * @return charging station id if present, otherwise null.
     */
    suspend fun getChargingStationIdByCarId(carId: String): String?

    /**
     * Retrieves the car currently in recharge at the specified charging station.
     *
     * @param chargingStationId charging station identifier.
     * @return car id if present, otherwise null.
     */
    suspend fun getCarIdByChargingStationId(chargingStationId: String): String?

    /**
     * Starts a recharge process for a car at a charging station.
     *
     * @param userId identifier of the user initiating the recharge.
     * @param startRechargeInput input payload for starting recharge.
     * @param startRechargeLogicInput parameters for recharge process logic.
     * @param chargingStationId charging station where recharge starts.
     */
    suspend fun startRecharge(
        userId: String,
        startRechargeInput: StartRechargeInput,
        startRechargeLogicInput: StartRechargeLogicInput,
        chargingStationId: String
    )

    /**
     * Stops an active recharge process.
     *
     * @param stopRechargeInput input payload for stopping recharge.
     * @param chargingStationId charging station where recharge stops.
     */
    suspend fun stopRecharge(stopRechargeInput: StopRechargeInput, chargingStationId: String)
}
