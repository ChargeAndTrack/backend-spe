package domain.charging_station

data class StartRechargeLogicInput(val chargingStationPower: Int, val batteryCapacity: Int)

data class StartRechargeInput(val carId: String)

data class StopRechargeInput(val carId: String)
