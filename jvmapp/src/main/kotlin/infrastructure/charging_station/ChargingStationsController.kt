package infrastructure.charging_station

import application.charging_station.ChargingStationServiceImpl
import domain.charging_station.ChargingStation
import domain.charging_station.ChargingStationImpl
import domain.charging_station.Location
import domain.charging_station.LocationImpl
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.ApplicationCall
import io.ktor.server.request.receive
import io.ktor.server.response.*

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
            val requestBody = call.receive<AddChargingStationDTO>()
            val chargingStationDTO = requestBody.toCompleteDTO()
            this.chargingStationService.addChargingStation(chargingStationDTO.toDomain()).also {
                if (it.isNotEmpty()) {
                    call.respond(HttpStatusCode.Created, chargingStationDTO.copy(_id = it))
                } else {
                    call.respond(HttpStatusCode.BadRequest, "Invalid request data")
                }
            }
        } catch (_: Exception) {
            call.respond(HttpStatusCode.InternalServerError, SERVER_ERROR_MESSAGE)
        }
    }

    private fun ChargingStation.toDTO(): ChargingStationDTO = ChargingStationDTO(
        _id = id,
        power = power,
        available = available,
        enabled = enabled,
        location = location.toDTO()
    )

    private fun Location.toDTO(): LocationDTO = LocationDTO(
        longitude = longitude,
        latitude = latitude
    )

    private fun ChargingStationDTO.toDomain(): ChargingStation = ChargingStationImpl(
        id = _id ?: "",
        power = power,
        available = available,
        enabled = enabled,
        location = location.toDomain()
    )

    private fun LocationDTO.toDomain(): Location = LocationImpl(
        longitude = longitude,
        latitude = latitude
    )

    private fun AddChargingStationDTO.toCompleteDTO(): ChargingStationDTO = ChargingStationDTO(
        _id = null,
        power = power,
        location = location
    )
}