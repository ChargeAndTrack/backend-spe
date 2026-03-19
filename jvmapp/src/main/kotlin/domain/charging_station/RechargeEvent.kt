package domain.charging_station

/**
 * Represents an event that occurs during a recharge process.
 */
interface RechargeEvent {
    /** Identifier of the user associated with this recharge event */
    val userId: String

    /** The recharge that triggered this event */
    val recharge: Recharge
}
