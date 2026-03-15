package infrastructure.charging_station

import domain.InvalidInputException
import domain.charging_station.ChargingStation
import domain.charging_station.StartRechargeInput
import domain.charging_station.StartRechargeLogicInput
import domain.charging_station.StopRechargeInput
import infrastructure.QueryDTO
import kotlinx.serialization.Serializable

@Serializable
data class RechargeDTO(val carId: String, val chargingStationId: String)

@Serializable
data class StartRechargeLogicDTO(
    val chargingStationPower: Int,
    val batteryCapacity: Int,
    val currentCarBattery: Int
) : QueryDTO<StartRechargeLogicInput> {
    override fun validate() {
        runCatching {
            require(chargingStationPower > 0) { "Charging station power must be greater than 0." }
            require(batteryCapacity > 0) { "Battery capacity must be greater than 0." }
        }.onFailure { throw InvalidInputException(it.message ?: "Invalid input") }
    }

    override fun toInput(): StartRechargeLogicInput = StartRechargeLogicInput(
        chargingStationPower,
        batteryCapacity,
        currentCarBattery
    )
}

@Serializable
data class StartRechargeDTO(val carId: String) : QueryDTO<StartRechargeInput> {
    override fun validate() {}

    override fun toInput(): StartRechargeInput = StartRechargeInput(carId)
}

@Serializable
data class StopRechargeDTO(val carId: String) : QueryDTO<StopRechargeInput> {
    override fun validate() {}

    override fun toInput(): StopRechargeInput = StopRechargeInput(carId)
}

fun ChargingStation.validateRecharge() = runCatching {
    require(available) { "Charging station is not available." }
    require(enabled) { "Charging station is disabled." }
}.onFailure { throw InvalidInputException(it.message ?: "Invalid input") }
