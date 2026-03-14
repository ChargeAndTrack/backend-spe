package infrastructure.charging_station

import application.charging_station.ChargingStationSearchQuery
import application.charging_station.ChargingStationSearchQueryImpl
import application.charging_station.Intent
import domain.charging_station.ChargingStationFilter
import domain.charging_station.MinPowerKwFilter
import infrastructure.AbstractQueryDTO
import kotlinx.serialization.Serializable

@Serializable
data class ChargingStationQueryDTO(
    val intent: String,
    val address: String,
    val filters: ChargingStationFiltersDTO? = null
) : AbstractQueryDTO<ChargingStationSearchQuery>() {
    override fun internalValidation() {
        require(intent == "NEAR" || intent == "CLOSEST") { "Invalid intent" }
        require(address.length >= 3) { "Invalid address" }
    }

    override fun toDomainEntity(): ChargingStationSearchQuery =
        ChargingStationSearchQueryImpl(
            Intent.valueOf(intent),
            address,
            filters?.toDomainEntity() ?: emptyList()
        )
}

@Serializable
data class ChargingStationFiltersDTO(
    val minPowerKw: Int? = null
) : AbstractQueryDTO<Collection<ChargingStationFilter>>() {
    override fun internalValidation() {
        require(minPowerKw == null || minPowerKw > 0) { "Minimum power must be a positive number" }
    }

    override fun toDomainEntity(): Collection<ChargingStationFilter> = listOfNotNull(
        minPowerKw?.let { MinPowerKwFilter(it) }
    )
}