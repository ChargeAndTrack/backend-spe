package domain.charging_station

interface Recharge {
    val carId: String
    val chargingStationId: String

    fun addRechargeObserver(observer: RechargeObserver)
    fun removeRechargeObserver(observer: RechargeObserver)
    suspend fun start(userId: String, startRechargeLogicInput: StartRechargeLogicInput)
    suspend fun stop()
}