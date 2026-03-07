package domain

sealed class AppException(override val message: String) : RuntimeException(message)

class InvalidInputException(override val message: String) : AppException(message)

class NotFoundException(override val message: String) : AppException(message)

class InternalErrorException(
    override val message: String = "An unexpected error occurred while performing the operation"
) : AppException(message)