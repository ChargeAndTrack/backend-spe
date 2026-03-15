package domain.user

data class AddCarInput(
    val plate: String,
    val maxBattery: Int
)

data class UpdateCarInput(
    val plate: String? = null,
    val maxBattery: Int? = null,
    val currentBattery: Int? = null
)

data class IncrementCarBatteryInput(val batteryToAdd: Int)
