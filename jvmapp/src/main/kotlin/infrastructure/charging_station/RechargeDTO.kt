package infrastructure.charging_station

import domain.charging_station.StartRechargeInput
import domain.charging_station.StopRechargeInput
import infrastructure.QueryDTO
import kotlinx.serialization.Serializable

@Serializable
data class RechargeDTO(val carId: String, val chargingStationId: String)

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
