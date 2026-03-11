package domain.charging_station

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.math.floor

data class RechargeImpl(override val carId: String, override val chargingStationId: String) : Recharge {
    private companion object {
        const val RECHARGE_PERCENTAGE = 1
        const val PERCENT_CHANGE = (RECHARGE_PERCENTAGE / 100).toDouble()
        const val LIMIT_BATTERY_PERCENTAGE = 100
    }

    private val rechargeObservers = mutableListOf<RechargeObserver>()
    private lateinit var rechargeJob: Job

    override fun addRechargeObserver(observer: RechargeObserver) { rechargeObservers.add(observer) }

    override fun removeRechargeObserver(observer: RechargeObserver) { rechargeObservers.remove(observer) }

    override suspend fun start(userId: String, startRechargeLogicInput: StartRechargeLogicInput) {
        rechargeJob = withContext(Dispatchers.Default) {
            this.launch {
                while (true) {
                    delay(calculateTimeForOnePercent(startRechargeLogicInput))
                    if (startRechargeLogicInput.batteryCapacity + RECHARGE_PERCENTAGE < LIMIT_BATTERY_PERCENTAGE) {
                        RechargeUpdate(userId, this@RechargeImpl, RECHARGE_PERCENTAGE).notify()
                    } else {
                        RechargeCompleted(userId, this@RechargeImpl).notify()
                    }
                }
            }
        }
    }

    override suspend fun stop() { if (this::rechargeJob.isInitialized && rechargeJob.isActive) rechargeJob.cancel() }

    private suspend fun RechargeEvent.notify() = rechargeObservers.forEach { it.notifyRechargeEvent(this) }

    private fun Double.convertHoursIntoMs(): Double = this * 60 * 60 * 1000

    private fun calculateTimeForOnePercent(startRechargeLogicInput: StartRechargeLogicInput): Long = floor(
        ((startRechargeLogicInput.batteryCapacity * PERCENT_CHANGE) / startRechargeLogicInput.chargingStationPower)
            .convertHoursIntoMs()
    ).toLong()
}
