package infrastructure.charging_station

import application.charging_station.ChargingStationServiceImpl
import io.ktor.http.HttpStatusCode
import io.ktor.http.Parameters
import io.ktor.server.application.ApplicationCall
import io.ktor.server.request.receive
import io.ktor.server.response.respond

object ChargingStationsController {

    val chargingStationService = ChargingStationServiceImpl(MongoDbChargingStationRepository())

    suspend fun listChargingStations(call: ApplicationCall) {
        println("Get all charging stations")
        call.respond(
            HttpStatusCode.OK,
            chargingStationService.listChargingStations()
                .map { it.toDTO() }
                .toList()
        )
    }

    suspend fun addChargingStation(call: ApplicationCall) {
        println("Add charging station")
        call.respond(
            HttpStatusCode.Created,
            chargingStationService.addChargingStation(
                call.receive<AddChargingStationDTO>()
                    .also { it.validate() }
                    .toDomainEntity()
            ).toDTO()
        )
    }

    suspend fun getChargingStation(call: ApplicationCall) = call.handleChargingStationRequest { chargingStationId ->
        println("Get charging station by id")
        call.respond(HttpStatusCode.OK, chargingStationService.getChargingStation(chargingStationId).toDTO())
    }

    suspend fun deleteChargingStation(call: ApplicationCall) = call.handleChargingStationRequest { chargingStationId ->
        println("Delete charging station by id")
        call.respond(HttpStatusCode.OK, chargingStationService.deleteChargingStation(chargingStationId))
    }


    suspend fun updateChargingStation(call: ApplicationCall) = call.handleChargingStationRequest { chargingStationId ->
        println("Update charging station by id")
        call.respond(
            HttpStatusCode.OK,
            chargingStationService.updateChargingStation(
                chargingStationId,
                call.receive<UpdateChargingStationDTO>()
                    .also { it.validate() }
                    .toDomainEntity()
            ).toDTO()
        )
    }

    suspend fun getNearbyChargingStations(call: ApplicationCall) {
        println("Get nearby charging stations")
        call.respond(
            HttpStatusCode.OK,
            chargingStationService.getNearbyChargingStations(
                call.request.queryParameters.toNearbyChargingStationDTO()
                    .also { it.validate() }
                    .toDomainEntity()
            ).map { it.toDTO() }
            .toList()
        )
    }

    suspend fun getClosestChargingStation(call: ApplicationCall) {
        print("Get nearby charging stations")
        call.respond(
            HttpStatusCode.OK,
            chargingStationService.getClosestChargingStation(
                 call.request.queryParameters.toClosestChargingStationDTO()
                    .also { it.validate() }
                    .toDomainEntity()
            ).toDTO()
        )
    }

    private suspend fun ApplicationCall.handleChargingStationRequest(
        handler: suspend (chargingStationId: String) -> Unit
    ) = parameters["id"]?.let { handler(it) } ?: respond(HttpStatusCode.BadRequest)

    private fun Parameters.toNearbyChargingStationDTO() = NearbyChargingStationsDTO(
        this["lng"]!!.toDouble(),
        this["lat"]!!.toDouble(),
        this["radius"]!!.toDouble(),
        this["onlyEnabled"].toBoolean()
    )

    private fun Parameters.toClosestChargingStationDTO() = ClosestChargingStationDTO(
        this["lng"]!!.toDouble(),
        this["lat"]!!.toDouble(),
        this["onlyEnabledAndAvailable"].toBoolean()
    )
}
