package infrastructure.charging_station

import application.charging_station.ActiveRechargeRepositoryImpl
import application.charging_station.ChargingStationService
import application.charging_station.RechargeServiceImpl
import application.user.CarServiceImpl
import infrastructure.Socket
import infrastructure.user.MongoDbUserRepository
import io.ktor.http.HttpStatusCode
import io.ktor.http.Parameters
import io.ktor.server.application.ApplicationCall
import io.ktor.server.request.receive
import io.ktor.server.response.respond

class ChargingStationsController(val chargingStationService: ChargingStationService) {

    private val rechargeService = RechargeServiceImpl(
        MongoDbRechargeRepository(),
        chargingStationService,
        CarServiceImpl(MongoDbUserRepository()),
        SocketIORechargeEventObserver(Socket.server),
        ActiveRechargeRepositoryImpl()
    )

    suspend fun listChargingStations(call: ApplicationCall) {
        println("Get all charging stations")
        call.respond(
            HttpStatusCode.OK,
            chargingStationService.listChargingStations()
                .map { it.toDTO(rechargeService.getCarIdByChargingStationId(it.id)) }
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
        call.respond(
            HttpStatusCode.OK,
            chargingStationService.getChargingStation(chargingStationId).toDTO(
                rechargeService.getCarIdByChargingStationId(chargingStationId)
            )
        )
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
            ).map { it.toDTO(rechargeService.getCarIdByChargingStationId(it.id)) }
            .toList()
        )
    }

    suspend fun getClosestChargingStation(call: ApplicationCall) {
        println("Get closest charging stations")
        call.respond(
            HttpStatusCode.OK,
            chargingStationService.getClosestChargingStation(
                 call.request.queryParameters.toClosestChargingStationDTO()
                    .also { it.validate() }
                    .toDomainEntity()
            ).let { it.toDTO(rechargeService.getCarIdByChargingStationId(it.id)) }
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
