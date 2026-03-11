package infrastructure.charging_station

import application.charging_station.ChargingStationSearchQuery
import application.charging_station.ChargingStationSearchQueryImpl
import application.charging_station.Intent
import domain.InvalidInputException
import domain.charging_station.ChargingStationFilter
import domain.charging_station.MinPowerKwFilter
import infrastructure.QueryDTO
import kotlinx.serialization.Serializable

@Serializable
data class ChargingStationQueryDTO(
    val intent: String,
    val address: String,
    val filters: ChargingStationFiltersDTO? = null
) : QueryDTO<ChargingStationSearchQuery> {
    override fun validate() {
        runCatching {
            require(intent == "NEAR" || intent == "CLOSEST") { "Invalid intent" }
            require(address.length >= 3) { "Invalid address" }
        }.onFailure { throw InvalidInputException(it.message ?: "Invalid input") }.let {}
    }

    override fun toInput(): ChargingStationSearchQuery =
        ChargingStationSearchQueryImpl(
            Intent.valueOf(intent),
            address,
            filters?.toInput() ?: emptyList()
        )
}

@Serializable
data class ChargingStationFiltersDTO(
    val minPowerKw: Int? = null
) : QueryDTO<Collection<ChargingStationFilter>> {
    override fun validate() {
        runCatching {
            require(minPowerKw == null || minPowerKw > 0) { "Minimum power must be a positive number" }
        }.onFailure { throw InvalidInputException(it.message ?: "Invalid input") }.let {}
    }

    override fun toInput(): Collection<ChargingStationFilter> = listOfNotNull(
        minPowerKw?.let { MinPowerKwFilter(it) }
    )
}