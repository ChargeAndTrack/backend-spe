package domain.charging_station

/**
 * Represents a charging station entity.
 */
interface ChargingStation {
    /** Identifier for the charging station */
    val id: String

    /** Power deliverable in kilowatts */
    val power: Int

    /** Indicates whether the charging station is currently available for use */
    val available: Boolean

    /** Indicates whether the charging station is enabled */
    val enabled: Boolean

    /** Geographic location of the charging station */
    val location: Location

    /**
     * Updates the charging station properties with new values.
     *
     * @param updateChargingStationInput the new values for the charging station properties.
     * @return the charging station updated.
     */
    fun update(updateChargingStationInput: UpdateChargingStationInput): ChargingStation
}
