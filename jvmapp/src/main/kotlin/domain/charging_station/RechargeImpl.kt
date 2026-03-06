package domain.charging_station

data class RechargeImpl(override val carId: String, override val chargingStationId: String) : Recharge
