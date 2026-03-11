package domain.charging_station

interface RechargeEvent {
    val userId: String
    val recharge: Recharge
}