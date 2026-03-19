package infrastructure.charging_station

import application.charging_station.RechargeEventObserver
import com.corundumstudio.socketio.SocketIOServer
import common.Adapter
import domain.charging_station.Recharge

@Adapter
class SocketIORechargeEventObserver(val socketServer: SocketIOServer) : RechargeEventObserver {

    override suspend fun rechargeUpdate(recharge: Recharge, level: Int) =
        SocketIOEvent.RechargeUpdateEvent(recharge.carId, level, "car:${recharge.carId}").emit()

    override suspend fun chargingStationUpdated(recharge: Recharge) =
        SocketIOEvent.ChargingStationUpdatedEvent(
            recharge.chargingStationId,
            "chargingStation:${recharge.chargingStationId}"
        ).emit()

    override suspend fun rechargeCompleted(recharge: Recharge) =
        SocketIOEvent.RechargeCompletedEvent(recharge.carId, "car:${recharge.carId}").emit()

    private fun SocketIOEvent.emit() = socketServer.getRoomOperations(room).sendEvent(name, data)
}
