package domain

interface Car {
    val id: String
    var plate: String
    var maxBattery: Int
    var currentBattery: Int?
}