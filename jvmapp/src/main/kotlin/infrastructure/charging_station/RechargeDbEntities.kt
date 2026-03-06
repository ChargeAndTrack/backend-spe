package infrastructure.charging_station

import kotlinx.serialization.Serializable

@Serializable
data class RechargeDbEntity(val carId: String, val chargingStationId: String)
