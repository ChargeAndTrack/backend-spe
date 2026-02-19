package domain.charging_station

import domain.user.Car

data class RechargeImpl(
    override val car: Car,
    override val chargingStation: ChargingStation
) : Recharge
