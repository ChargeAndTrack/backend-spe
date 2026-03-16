package infrastructure.charging_station

import application.charging_station.RechargeEventObserver
import com.corundumstudio.socketio.SocketIOServer
import domain.charging_station.Recharge

class SocketIORechargeEventObserver(val socketServer: SocketIOServer) : RechargeEventObserver {

    override suspend fun rechargeUpdate(recharge: Recharge, level: Int) {
        SocketIOEvent.RechargeUpdateEvent(recharge.carId, level, "car:${recharge.carId}").emit()
        SocketIOEvent.ChargingStationUpdateEvent(
            recharge.chargingStationId,
            "charging-station:${recharge.chargingStationId}"
        ).emit()
    }

    override suspend fun rechargeCompleted(recharge: Recharge) {
        SocketIOEvent.RechargeCompletedEvent(recharge.carId, "car:${recharge.carId}").emit()
        SocketIOEvent.RechargeCompletedEvent(
            recharge.chargingStationId,
            "chargingStation:${recharge.chargingStationId}"
        ).emit()
    }

    private fun SocketIOEvent.emit() = socketServer.getRoomOperations(room).sendEvent(name, data)
}
