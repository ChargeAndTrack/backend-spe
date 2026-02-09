package domain

interface User {
    val id: String
    val username: String
    val password: String
    val role: Role
    var cars: Collection<Car>
}

enum class Role(val displayName: String) {
    BASE_USER("BaseUser"), ADMIN("Admin")
}