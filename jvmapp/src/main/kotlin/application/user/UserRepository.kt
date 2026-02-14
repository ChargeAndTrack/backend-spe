package application.user

import domain.user.User

interface UserRepository {
    suspend fun findUser(username: String, password: String): User
    suspend fun getUser(userId: String): User
}