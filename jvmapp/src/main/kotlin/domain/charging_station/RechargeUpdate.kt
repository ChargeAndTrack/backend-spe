package domain.charging_station

data class RechargeUpdate(
    override val userId: String,
    override val recharge: Recharge,
    val percentageToAdd: Int
) : RechargeEvent
