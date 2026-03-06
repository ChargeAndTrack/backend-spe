package infrastructure.charging_station

import application.charging_station.RechargeService
import application.charging_station.RechargeServiceImpl
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.ApplicationCall
import io.ktor.server.request.receive
import io.ktor.server.response.respond

object RechargeController {

    val rechargeService: RechargeService = RechargeServiceImpl(MongoDbRechargeRepository(
        ChargingStationsController.repository
    ))

    suspend fun startRecharge(call: ApplicationCall) = call.handleRechargeRequest { chargingStationId ->
        println("Start recharge")
        call.respond(
            HttpStatusCode.OK,
            rechargeService.startRecharge(call.receive<StartRechargeDTO>().toInput(), chargingStationId)
        )
    }

    suspend fun stopRecharge(call: ApplicationCall) = call.handleRechargeRequest { chargingStationId ->
        println("Stop recharge")
        call.respond(
            HttpStatusCode.OK,
            rechargeService.stopRecharge(call.receive<StopRechargeDTO>().toInput(), chargingStationId)
        )
    }

    private suspend fun ApplicationCall.handleRechargeRequest(
        handler: suspend (chargingStationId: String) -> Unit
    ) = parameters["id"]?.let { handler(it) } ?: respond(HttpStatusCode.BadRequest)
}
