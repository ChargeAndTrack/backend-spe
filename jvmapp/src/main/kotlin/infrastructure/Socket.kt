package infrastructure

import com.corundumstudio.socketio.Configuration
import com.corundumstudio.socketio.SocketIOServer

object Socket {
    private val config = Configuration().apply {
        hostname = Config.Deployment.host ?: "0.0.0.0"
        port = 3001
        origin = null
    }
    lateinit var server: SocketIOServer

    init {
        setupServer()
    }

    private fun setupServer() {
        server = SocketIOServer(config)
        server.run {
            addConnectListener { println("Client connected: ${it.sessionId}") }
            addEventListener("join-charging-stations", Array<String>::class.java) { client, chargingStationIds, _ ->
                chargingStationIds.forEach { client.joinRoom("chargingStation:$it") }
            }
            addEventListener("leave-charging-stations", Array<String>::class.java) { client, chargingStationIds, _ ->
                chargingStationIds.forEach { client.leaveRoom("chargingStation:$it") }
            }
            addEventListener("start-recharge", String::class.java) { client, carId, _ ->
                client.joinRoom("car:$carId")
            }
            addEventListener("stop-recharge", String::class.java) { client, carId, _ ->
                client.leaveRoom("car:$carId")
            }
            addDisconnectListener { println("Client disconnected") }
            start()
        }
    }
}
