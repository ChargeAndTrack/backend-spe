package domain.charging_station

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.floor

data class RechargeImpl(override val carId: String, override val chargingStationId: String) : Recharge {
    private companion object {
        const val RECHARGE_PERCENTAGE = 1
        const val PERCENT_CHANGE = RECHARGE_PERCENTAGE.toDouble() / 100.0
        const val LIMIT_BATTERY_PERCENTAGE = 100
    }

    private val rechargeObservers = mutableListOf<RechargeObserver>()
    private lateinit var rechargeJob: Job
    private lateinit var userId: String
    private var currentBattery = 0

    override fun addRechargeObserver(observer: RechargeObserver) { rechargeObservers.add(observer) }

    override fun removeRechargeObserver(observer: RechargeObserver) { rechargeObservers.remove(observer) }

    override suspend fun start(userId: String, startRechargeLogicInput: StartRechargeLogicInput) {
        this.userId = userId
        currentBattery = startRechargeLogicInput.currentCarBattery
        rechargeJob = CoroutineScope(Dispatchers.Default).launch {
            ChargingStationUpdated(userId, this@RechargeImpl, available = false).notify()
            while (true) {
                delay(timeToRechargeOnePercent(startRechargeLogicInput))
                if (currentBattery + RECHARGE_PERCENTAGE < LIMIT_BATTERY_PERCENTAGE) {
                    currentBattery += RECHARGE_PERCENTAGE
                    RechargeUpdate(userId, this@RechargeImpl, RECHARGE_PERCENTAGE).notify()
                } else {
                    RechargeCompleted(userId, this@RechargeImpl).notify()
                    stop()
                }
            }
        }
    }

    override suspend fun stop() {
        if (this::rechargeJob.isInitialized && rechargeJob.isActive) {
            ChargingStationUpdated(userId, this, available = true).notify()
            rechargeJob.cancel()
        }
    }

    private suspend fun RechargeEvent.notify() = rechargeObservers.forEach { it.notifyRechargeEvent(this) }

    private fun Double.convertHoursIntoMs(): Double = this * 60 * 60 * 1000

    private fun timeToRechargeOnePercent(startRechargeLogicInput: StartRechargeLogicInput): Long = floor(
        ((startRechargeLogicInput.batteryCapacity * PERCENT_CHANGE) / startRechargeLogicInput.chargingStationPower)
            .convertHoursIntoMs()
    ).toLong()
}
