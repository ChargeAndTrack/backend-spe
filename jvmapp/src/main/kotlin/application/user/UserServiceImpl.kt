package application.user

import domain.user.User

class UserServiceImpl(private val userRepository: UserRepository) : UserService {

    override suspend fun login(username: String, password: String): User {
        return userRepository.findUser(username, password)
    }

    override suspend fun getUser(userId: String): User {
        TODO("Not yet implemented")
    }
}