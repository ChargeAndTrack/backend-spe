package application.charging_station

import domain.charging_station.Recharge

interface ActiveRechargeRepository {
    fun getRechargeByIds(carId: String, chargingStationId: String): Recharge?
    fun addRecharge(recharge: Recharge)
    fun removeRecharge(carId: String, chargingStationId: String)
}