package application.charging_station

import domain.charging_station.Recharge

class ActiveRechargeRepositoryImpl : ActiveRechargeRepository {

    private val _recharges: MutableSet<Recharge> = mutableSetOf()

    override fun getRechargeByIds(carId: String, chargingStationId: String): Recharge? = _recharges.find {
        it.carId == carId && it.chargingStationId == chargingStationId
    }

    override fun addRecharge(recharge: Recharge) {
        _recharges.add(recharge)
    }

    override fun removeRecharge(carId: String, chargingStationId: String) {
        _recharges.removeIf { it.carId == carId && it.chargingStationId == chargingStationId }
    }
}
