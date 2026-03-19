package infrastructure.charging_station

import application.charging_station.ActiveRechargeRepositoryImpl
import application.charging_station.ChargingStationServiceImpl
import application.charging_station.RechargeServiceImpl
import application.user.CarServiceImpl
import infrastructure.Socket
import infrastructure.user.MongoDbUserRepository
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.ApplicationCall
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.principal
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import kotlin.random.Random

class RechargeController {

    private val chargingStationService = ChargingStationServiceImpl(MongoDbChargingStationRepository())
    private val carService = CarServiceImpl(MongoDbUserRepository())
    val rechargeService = RechargeServiceImpl(
        MongoDbRechargeRepository(),
        chargingStationService,
        carService,
        SocketIORechargeEventObserver(Socket.server),
        ActiveRechargeRepositoryImpl()
    )

    suspend fun startRecharge(call: ApplicationCall) = call.handleRechargeRequest { chargingStationId ->
        println("Start recharge")
        val request = call.receive<StartRechargeDTO>()
        val chargingStation = chargingStationService.getChargingStation(chargingStationId)
        chargingStation.validateRecharge()
        val car = carService.getCar(call.getUserId(), request.carId)
        call.respond(
            HttpStatusCode.OK,
            rechargeService.startRecharge(
                call.getUserId(),
                request.toDomainEntity(),
                StartRechargeLogicDTO(chargingStation.power, car.maxBattery, Random.nextInt(100)).toDomainEntity(),
                chargingStationId
            )
        )
    }

    suspend fun stopRecharge(call: ApplicationCall) = call.handleRechargeRequest { chargingStationId ->
        println("Stop recharge")
        call.respond(
            HttpStatusCode.OK,
            rechargeService.stopRecharge(call.receive<StopRechargeDTO>().toDomainEntity(), chargingStationId)
        )
    }

    private fun ApplicationCall.getUserId() = principal<JWTPrincipal>()!!.payload.getClaim("userId").asString()

    private suspend fun ApplicationCall.handleRechargeRequest(
        handler: suspend (chargingStationId: String) -> Unit
    ) = parameters["id"]?.let { handler(it) } ?: respond(HttpStatusCode.BadRequest)
}
