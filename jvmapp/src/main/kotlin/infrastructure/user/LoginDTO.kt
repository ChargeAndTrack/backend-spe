package infrastructure.user

import kotlinx.serialization.Serializable

@Serializable
data class LoginRequestDTO(
    val username: String,
    val password: String
)

@Serializable
data class LoginResponseDTO(
    val role: String,
    val token: String
)