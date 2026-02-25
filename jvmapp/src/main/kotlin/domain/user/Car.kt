package domain.user

interface Car {
    val id: String
    val plate: String
    val maxBattery: Int
    val currentBattery: Int?
    fun update(updateCarInput: UpdateCarInput): Car
}