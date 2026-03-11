package application.charging_station

import application.user.CarService
import domain.charging_station.Recharge
import domain.charging_station.RechargeCompleted
import domain.charging_station.RechargeEvent
import domain.charging_station.RechargeImpl
import domain.charging_station.RechargeUpdate
import domain.charging_station.StartRechargeLogicInput
import domain.charging_station.StartRechargeInput
import domain.charging_station.StopRechargeInput
import domain.charging_station.UpdateChargingStationInput
import domain.user.IncrementCarBatteryInput

class RechargeServiceImpl(
    val rechargeRepository: RechargeRepository,
    val chargingStationService: ChargingStationService,
    val carService: CarService
) : RechargeService {

    private val recharges = mutableMapOf<Pair<String, String>, Recharge>()

    override suspend fun getChargingStationIdByCarId(carId: String): String =
        rechargeRepository.getChargingStationIdByCarId(carId)

    override suspend fun getCarIdByChargingStationId(chargingStationId: String): String =
        rechargeRepository.getCarIdByChargingStationId(chargingStationId)

    override suspend fun startRecharge(
        userId: String,
        startRechargeInput: StartRechargeInput,
        startRechargeLogicInput: StartRechargeLogicInput,
        chargingStationId: String
    ) = RechargeImpl(startRechargeInput.carId, chargingStationId).let {
        rechargeRepository.startRecharge(it)
        it.addRechargeObserver(this)
        it.start(userId, startRechargeLogicInput)
        recharges[Pair(startRechargeInput.carId, chargingStationId)] = it
    }

    override suspend fun stopRecharge(stopRechargeInput: StopRechargeInput, chargingStationId: String) {
        recharges[Pair(stopRechargeInput.carId, chargingStationId)]?.let {
            rechargeRepository.stopRecharge(it)
            it.stop()
        }.also { recharges.remove(Pair(stopRechargeInput.carId, chargingStationId)) }
    }

    override suspend fun notifyRechargeEvent(rechargeEvent: RechargeEvent) = when (rechargeEvent) {
        is RechargeUpdate -> {
            carService.incrementCarBattery(
                rechargeEvent.userId,
                rechargeEvent.recharge.carId,
                IncrementCarBatteryInput(rechargeEvent.percentageToAdd)
            )
            chargingStationService.updateChargingStation(
                rechargeEvent.recharge.chargingStationId,
                UpdateChargingStationInput(available = false)
            )
        }
        is RechargeCompleted -> {
            rechargeRepository.stopRecharge(rechargeEvent.recharge)
            chargingStationService.updateChargingStation(
                rechargeEvent.recharge.chargingStationId,
                UpdateChargingStationInput(available = true)
            )
        }
        else -> throw IllegalArgumentException("Unknown recharge event $rechargeEvent")
    }.let {}
}
