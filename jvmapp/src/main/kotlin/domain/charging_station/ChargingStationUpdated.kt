package domain.charging_station

data class ChargingStationUpdated(
    override val userId: String,
    override val recharge: Recharge,
    val available: Boolean
) : RechargeEvent
