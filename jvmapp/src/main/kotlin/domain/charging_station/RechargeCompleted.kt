package domain.charging_station

data class RechargeCompleted(override val userId: String, override val recharge: Recharge) : RechargeEvent
