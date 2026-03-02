package infrastructure

interface QueryDTO<T> {
    fun validate()
    fun toInput(): T
}