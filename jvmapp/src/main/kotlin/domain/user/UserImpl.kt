package domain.user

data class UserImpl(
    override val id: String,
    override val username: String,
    override val password: String,
    override val role: Role,
    override var cars: Collection<Car> = emptySet()
) : User
