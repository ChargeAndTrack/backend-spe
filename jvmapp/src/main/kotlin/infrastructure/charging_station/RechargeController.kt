package infrastructure.charging_station

import application.charging_station.ChargingStationServiceImpl
import application.charging_station.RechargeServiceImpl
import application.user.CarServiceImpl
import infrastructure.user.MongoDbUserRepository
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.ApplicationCall
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.principal
import io.ktor.server.request.receive
import io.ktor.server.response.respond

object RechargeController {

    private val chargingStationService = ChargingStationServiceImpl(MongoDbChargingStationRepository())
    private val carService = CarServiceImpl(MongoDbUserRepository())
    private val rechargeService = RechargeServiceImpl(MongoDbRechargeRepository(), chargingStationService, carService)

    suspend fun startRecharge(call: ApplicationCall) = call.handleRechargeRequest { chargingStationId ->
        println("Start recharge")
        val request = call.receive<StartRechargeDTO>()
        val chargingStation = chargingStationService.getChargingStation(chargingStationId)
        chargingStation.validateRecharge()
        call.respond(
            HttpStatusCode.OK,
            rechargeService.startRecharge(
                call.getUserId(),
                request.toInput(),
                StartRechargeLogicDTO(
                    chargingStation.power,
                    carService.getCar(call.getUserId(), request.carId).maxBattery
                ).toInput(),
                chargingStationId
            )
        )
    }

    suspend fun stopRecharge(call: ApplicationCall) = call.handleRechargeRequest { chargingStationId ->
        println("Stop recharge")
        call.respond(
            HttpStatusCode.OK,
            rechargeService.stopRecharge(call.receive<StopRechargeDTO>().toInput(), chargingStationId)
        )
    }

    private fun ApplicationCall.getUserId() = principal<JWTPrincipal>()!!.payload.getClaim("userId").asString()

    private suspend fun ApplicationCall.handleRechargeRequest(
        handler: suspend (chargingStationId: String) -> Unit
    ) = parameters["id"]?.let { handler(it) } ?: respond(HttpStatusCode.BadRequest)
}
