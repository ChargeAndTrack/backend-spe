package application.user

import common.InBoundPort
import domain.user.User

@InBoundPort
interface UserService {
    suspend fun login(username: String, password: String): User
    suspend fun getUser(userId: String): User
}