package domain.user

data class CarImpl(
    override val id: String,
    override var plate: String,
    override var maxBattery: Int,
    override var currentBattery: Int?
) : Car