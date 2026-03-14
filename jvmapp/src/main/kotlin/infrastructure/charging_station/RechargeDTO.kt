package infrastructure.charging_station

import domain.InvalidInputException
import domain.charging_station.ChargingStation
import domain.charging_station.StartRechargeInput
import domain.charging_station.StartRechargeLogicInput
import domain.charging_station.StopRechargeInput
import infrastructure.AbstractQueryDTO
import kotlinx.serialization.Serializable

@Serializable
data class RechargeDTO(val carId: String, val chargingStationId: String)

@Serializable
data class StartRechargeLogicDTO(
    val chargingStationPower: Int,
    val batteryCapacity: Int,
    val currentCarBattery: Int
) : AbstractQueryDTO<StartRechargeLogicInput>() {
    override fun internalValidation() {
        require(chargingStationPower > 0) { "Charging station power must be greater than 0." }
        require(batteryCapacity > 0) { "Battery capacity must be greater than 0." }
    }

    override fun toDomainEntity(): StartRechargeLogicInput = StartRechargeLogicInput(
        chargingStationPower,
        batteryCapacity,
        currentCarBattery
    )
}

@Serializable
data class StartRechargeDTO(val carId: String) : AbstractQueryDTO<StartRechargeInput>() {
    override fun internalValidation() {}

    override fun toDomainEntity(): StartRechargeInput = StartRechargeInput(carId)
}

@Serializable
data class StopRechargeDTO(val carId: String) : AbstractQueryDTO<StopRechargeInput>() {
    override fun internalValidation() {}

    override fun toDomainEntity(): StopRechargeInput = StopRechargeInput(carId)
}

fun ChargingStation.validateRecharge() = runCatching {
    require(available) { "Charging station is not available." }
    require(enabled) { "Charging station is disabled." }
}.onFailure { throw InvalidInputException(it.message ?: "Invalid input") }
