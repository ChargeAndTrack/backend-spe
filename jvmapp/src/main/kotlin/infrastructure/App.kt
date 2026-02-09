package infrastructure

fun main() {
    Server(3000, Router.module).start()
}
