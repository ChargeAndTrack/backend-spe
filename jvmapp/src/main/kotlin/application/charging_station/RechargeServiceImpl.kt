package application.charging_station

import application.user.CarService
import domain.NotFoundException
import domain.charging_station.ChargingStationUpdated
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
import domain.user.UpdateCarInput

class RechargeServiceImpl(
    val rechargeRepository: RechargeRepository,
    val chargingStationService: ChargingStationService,
    val carService: CarService,
    val rechargeEventObserver: RechargeEventObserver,
    val activeRechargeRepository: ActiveRechargeRepository
) : RechargeService {

    private companion object {
        const val RECHARGE_NOT_FOUND_MESSAGE = "Recharge not found"
    }

    override suspend fun getChargingStationIdByCarId(carId: String): String? =
        rechargeRepository.getChargingStationIdByCarId(carId)

    override suspend fun getCarIdByChargingStationId(chargingStationId: String): String? =
        rechargeRepository.getCarIdByChargingStationId(chargingStationId)

    override suspend fun startRecharge(
        userId: String,
        startRechargeInput: StartRechargeInput,
        startRechargeLogicInput: StartRechargeLogicInput,
        chargingStationId: String
    ) = RechargeImpl(startRechargeInput.carId, chargingStationId).let {
        rechargeRepository.startRecharge(it)
        carService.updateCar(
            userId,
            startRechargeInput.carId,
            UpdateCarInput(currentBattery = startRechargeLogicInput.currentCarBattery)
        )
        it.addRechargeObserver(this)
        it.start(userId, startRechargeLogicInput)
        activeRechargeRepository.addRecharge(it)
    }

    override suspend fun stopRecharge(stopRechargeInput: StopRechargeInput, chargingStationId: String) =
        activeRechargeRepository.getRechargeByIds(stopRechargeInput.carId, chargingStationId)?.let {
            it.stopHandler()
            it.stop()
        } ?: throw NotFoundException(RECHARGE_NOT_FOUND_MESSAGE)

    override suspend fun notifyRechargeEvent(rechargeEvent: RechargeEvent) = when (rechargeEvent) {
        is RechargeUpdate -> {
            println("Recharge update")
            carService.incrementCarBattery(
                rechargeEvent.userId,
                rechargeEvent.recharge.carId,
                IncrementCarBatteryInput(rechargeEvent.percentageToAdd)
            )
            rechargeEventObserver.rechargeUpdate(
                rechargeEvent.recharge,
                carService.getCar(rechargeEvent.userId, rechargeEvent.recharge.carId).currentBattery
                    ?: throw IllegalStateException("Car battery cannot be null")
            )
        }
        is ChargingStationUpdated -> {
            println("Charging station updated")
            chargingStationService.updateChargingStation(
                rechargeEvent.recharge.chargingStationId,
                UpdateChargingStationInput(available = rechargeEvent.available)
            )
            rechargeEventObserver.chargingStationUpdated(rechargeEvent.recharge)
        }
        is RechargeCompleted -> {
            println("Recharge completed")
            rechargeEvent.recharge.stopHandler()
            rechargeEventObserver.rechargeCompleted(rechargeEvent.recharge)
        }
        else -> throw IllegalArgumentException("Unknown recharge event $rechargeEvent")
    }.let {}

    private suspend fun Recharge.stopHandler() {
        rechargeRepository.stopRecharge(this)
        activeRechargeRepository.removeRecharge(carId, chargingStationId)
    }
}
