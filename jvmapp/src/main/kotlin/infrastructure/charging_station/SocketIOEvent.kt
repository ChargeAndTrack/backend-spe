package infrastructure.charging_station

sealed interface Event {
    val room: String
    val name: String
}

enum class EventType(val event: String) {
    RECHARGE_UPDATE("recharge-update"),
    CHARGING_STATION_UPDATE("charging-station-update"),
    RECHARGE_COMPLETED("recharge-completed")
}

sealed class SocketIOEvent(val eventType: EventType, val data: Map<String, Any> = emptyMap()) : Event {

    override val name get() = eventType.event

    data class RechargeUpdateEvent(
        val carId: String,
        val level: Int,
        override val room: String
    ) : SocketIOEvent(EventType.RECHARGE_UPDATE, mapOf("id" to carId, "level" to level))

    data class ChargingStationUpdateEvent(
        val chargingStationId: String,
        override val room: String
    ) : SocketIOEvent(EventType.CHARGING_STATION_UPDATE, mapOf("id" to chargingStationId))

    data class RechargeCompletedEvent(
        val id: String,
        override val room: String
    ) : SocketIOEvent(EventType.RECHARGE_COMPLETED, mapOf("id" to id))
}
