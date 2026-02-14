package domain

import domain.user.Car

interface Recharge {
    val car: Car
    val chargingStation: ChargingStation
}