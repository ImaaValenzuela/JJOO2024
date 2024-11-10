package main
import main.services.AuthService

fun main(args: Array<String>) {
    while (true) {
        println("Menu:")
        println("1. Login")
        println("2. Register")
        println("0. Exit")

        when (readlnOrNull()?.toInt()) {
            1 -> {
                AuthService.login()
            }
            2 -> {
                AuthService.register()
            }
            0 -> {
                println("Exiting...")
                break
            }
            else -> {
                println("Invalid option, please try again.")
            }
        }
    }
}




