package infrastructure.charging_station

import application.charging_station.ChargingStationServiceImpl
import io.ktor.http.HttpStatusCode
import io.ktor.http.Parameters
import io.ktor.server.application.ApplicationCall
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import java.lang.IllegalArgumentException

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
                    .toInput()
            ).toDTO()
        )
    }

    suspend fun getChargingStation(call: ApplicationCall) {
        println("Get charging station by id")
        call.respond(
            HttpStatusCode.OK,
            chargingStationService.getChargingStation(call.parameters["id"] ?: "")
                .toDTO()
        )
    }

    suspend fun deleteChargingStation(call: ApplicationCall) {
        println("Delete charging station by id")
        call.respond(
            HttpStatusCode.OK,
            chargingStationService.deleteChargingStation(call.parameters["id"] ?: "")
        )
    }


    suspend fun updateChargingStation(call: ApplicationCall) {
        println("Update charging station by id")
        call.respond(
            HttpStatusCode.OK,
            chargingStationService.updateChargingStation(
                call.parameters["id"] ?: "",
                call.receive<UpdateChargingStationDTO>()
                    .also { it.validate() }
                    .toInput()
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
                    .toInput()
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
                    .toInput()
            ).toDTO()
        )
    }

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

    private suspend inline fun <reified T : Any> ApplicationCall.execute(
        message: String,
        status: HttpStatusCode,
        response: suspend () -> T
    ) {
        println(message)
        respond(status, response())
    }
}
