package infrastructure

import domain.InvalidInputException

interface QueryDTO<T> {
    fun validate()
    fun toDomainEntity(): T
}

abstract class AbstractQueryDTO<T> : QueryDTO<T> {
    final override fun validate() =
        runCatching {
            internalValidation()
        }.onFailure { throw InvalidInputException(it.message ?: "Invalid input") }.let {}

    protected abstract fun internalValidation()
}