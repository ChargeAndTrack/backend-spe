package application.user

import domain.user.User

interface UserService {
    suspend fun login(username: String, password: String): User
    suspend fun getUser(userId: String): User
}