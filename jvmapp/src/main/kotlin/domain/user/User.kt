package domain.user

interface User {
    val id: String
    val username: String
    val password: String
    val role: Role
    val cars: Collection<Car>
    fun updateCar(carId: String, updateCarInput: UpdateCarInput): Car
}

enum class Role(val displayName: String) {
    BASE_USER("BaseUser"), ADMIN("Admin")
}