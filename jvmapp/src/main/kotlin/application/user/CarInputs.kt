package application.user

data class AddCarInput(
    val plate: String,
    val maxBattery: Int
)

data class UpdateCarInput(
    val plate: String?,
    val maxBattery: Int?,
    val currentBattery: Int?
)