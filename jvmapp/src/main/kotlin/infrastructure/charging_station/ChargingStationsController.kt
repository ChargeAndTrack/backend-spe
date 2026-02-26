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
                chargingStationService
                    .listChargingStations()
                    .map { it.toDTO() }
                    .toList()
            )
        } catch (_: Exception) {
            call.respond(HttpStatusCode.InternalServerError, SERVER_ERROR_MESSAGE)
        }
    }

    suspend fun addChargingStation(call: ApplicationCall) {
        println("Add charging station")
        try {
            call.respond(
                HttpStatusCode.Created,
                chargingStationService
                    .addChargingStation(call.receive<AddChargingStationDTO>().toInput())
                    .toDTO()
            )
        } catch (e: IllegalArgumentException) {
            call.respond(HttpStatusCode.BadRequest, e.message ?: "")
        } catch (_: Exception) {
            call.respond(HttpStatusCode.InternalServerError, SERVER_ERROR_MESSAGE)
        }
    }

    suspend fun getChargingStation(call: ApplicationCall) {
        println("Get charging station by id")
        try {
            call.respond(
                HttpStatusCode.OK,
                chargingStationService.getChargingStation(call.parameters["id"] ?: "").toDTO()
            )
        }  catch (e: IllegalArgumentException) {
            call.respond(HttpStatusCode.NotFound, e.message ?: "")
        } catch (_: Exception) {
            call.respond(HttpStatusCode.InternalServerError, SERVER_ERROR_MESSAGE)
        }
    }

    suspend fun deleteChargingStation(call: ApplicationCall) {
        println("Delete charging station by id")
        try {
            chargingStationService.deleteChargingStation(call.parameters["id"] ?: "")
            call.respond(HttpStatusCode.OK, "charging station removed successfully")
        } catch (e: IllegalArgumentException) {
            call.respond(HttpStatusCode.NotFound, e.message ?: "")
        } catch (_: Exception) {
            call.respond(HttpStatusCode.InternalServerError, SERVER_ERROR_MESSAGE)
        }
    }

    suspend fun updateChargingStation(call: ApplicationCall) {
        println("Update charging station by id")
        try {
            call.respond(
                HttpStatusCode.OK,
                chargingStationService.updateChargingStation(
                    call.parameters["id"] ?: "",
                    call.receive<UpdateChargingStationDTO>().toInput()
                ).toDTO()
            )
        } catch (e: IllegalArgumentException) {
            call.respond(HttpStatusCode.NotFound, e.message ?: "")
        } catch (_: Exception) {
            call.respond(HttpStatusCode.InternalServerError, SERVER_ERROR_MESSAGE)
        }
    }

    suspend fun getNearbyChargingStations(call: ApplicationCall) {
        println("Get nearby charging stations")
        try {
            call.respond(
                HttpStatusCode.OK,
                chargingStationService
                    .getNearbyChargingStations(call.receive<NearbyChargingStationsDTO>().toInput())
                    .map { it.toDTO() }
                    .toList()
            )
        } catch (_: Exception) {
            call.respond(HttpStatusCode.InternalServerError, SERVER_ERROR_MESSAGE)
        }
    }

    suspend fun getClosestChargingStation(call: ApplicationCall) {
        println("Get nearby charging stations")
        try {
            call.respond(
                HttpStatusCode.OK,
                chargingStationService
                    .getClosestChargingStation(call.receive<ClosestChargingStationDTO>().toInput())
                    .toDTO()
            )
        } catch (e: kotlin.IllegalArgumentException) {
            call.respond(HttpStatusCode.NotFound, e.message ?: "")
        } catch (_: Exception) {
            call.respond(HttpStatusCode.InternalServerError, SERVER_ERROR_MESSAGE)
        }
    }
}
