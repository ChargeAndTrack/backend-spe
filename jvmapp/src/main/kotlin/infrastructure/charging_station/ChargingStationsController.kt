package infrastructure.charging_station

import application.charging_station.ChargingStationServiceImpl
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.ApplicationCall
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import java.lang.IllegalArgumentException

object ChargingStationsController {

    const val SERVER_ERROR_MESSAGE = "Error adding the charging station"
    val chargingStationService = ChargingStationServiceImpl(MongoDbChargingStationRepository())

    suspend fun listChargingStations(call: ApplicationCall) {
        println("Get all charging stations")
        try {
            call.respond(
                HttpStatusCode.OK,
                chargingStationService.listChargingStations()
                    .map { it.toDTO() }
                    .toList()
            )
        } catch (_: Exception) {
            call.respond(HttpStatusCode.InternalServerError, SERVER_ERROR_MESSAGE)
        }
    }

    suspend fun addChargingStation(call: ApplicationCall) =
        call.execute("Add charging station", HttpStatusCode.Created) {
            chargingStationService.addChargingStation(
                call.receive<AddChargingStationDTO>()
                    .also { it.validate() }
                    .toInput()
            ).toDTO()
        }

    suspend fun getChargingStation(call: ApplicationCall) =
        call.execute("Get charging station by id", HttpStatusCode.OK) {
            chargingStationService.getChargingStation(call.parameters["id"] ?: "").toDTO()
        }

    suspend fun deleteChargingStation(call: ApplicationCall) =
        call.execute("Delete charging station by id", HttpStatusCode.OK) {
            chargingStationService.deleteChargingStation(call.parameters["id"] ?: "")
            "charging station removed successfully"
        }

    suspend fun updateChargingStation(call: ApplicationCall) =
        call.execute("Update charging station by id", HttpStatusCode.OK) {
            chargingStationService.updateChargingStation(
                call.parameters["id"] ?: "",
                call.receive<UpdateChargingStationDTO>()
                    .also { it.validate() }
                    .toInput()
            ).toDTO()
        }

    suspend fun getNearbyChargingStations(call: ApplicationCall) =
        call.execute("Get nearby charging stations", HttpStatusCode.OK) {
            chargingStationService.getNearbyChargingStations(
                call.receive<NearbyChargingStationsDTO>()
                    .also { it.validate() }
                    .toInput()
            ).map { it.toDTO() }
            .toList()
        }

    suspend fun getClosestChargingStation(call: ApplicationCall) =
        call.execute("Get nearby charging stations", HttpStatusCode.OK) {
            chargingStationService.getClosestChargingStation(
                call.receive<ClosestChargingStationDTO>()
                    .also { it.validate() }
                    .toInput()
            ).toDTO()
        }

    private suspend inline fun <reified T : Any> ApplicationCall.execute(
        message: String,
        status: HttpStatusCode,
        response: suspend () -> T
    ) {
        println(message)
        try {
            respond(status, response())
        } catch (e: IllegalArgumentException) {
            respond(HttpStatusCode.NotFound, e.message ?: "")
        } catch (_: Exception) {
            respond(HttpStatusCode.InternalServerError, SERVER_ERROR_MESSAGE)
        }
    }
}
