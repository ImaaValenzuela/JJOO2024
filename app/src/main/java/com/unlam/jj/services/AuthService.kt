package main.services

import data.User
import main.*
import main.services.AdminService.manageEvents
import main.services.AdminService.manageUsers
import main.services.EventService.listEvents
import main.services.MedalService.listMedalTable
import main.services.PurchaseService.buyTicket
import main.services.PurchaseService.listPurchasesByUserId
import repositories.UserRepository
import java.time.LocalTime

object AuthService {

    fun login() {
        println("Enter nickname:")
        val nickname = readlnOrNull() ?: return println("Invalid input")

        println("Enter password:")
        val password = readlnOrNull() ?: return println("Invalid input")

        UserRepository.login(nickname, password)?.let { user ->
            println("Login successful")
            loginMain(user)
        } ?: println("Invalid credentials")
    }

    private fun loginMain(user: User) {
        while (true) {
            println("Menu:")
            println("Welcome ${user.nickName}")

            if (user.rol == "admin") {
                println("1. Buy ticket ")
                println("2. See purchases ")
                println("3. See medal table ")
                println("4. Manage users")
                println("5. Manage events")
                println("0. Exit ")
            } else {
                println("1. Buy ticket ")
                println("2. See purchases ")
                println("3. See medal table ")
                println("0. Exit ")
            }

            when (readlnOrNull()?.toInt()) {
                1 -> {
                    listEvents()
                    println("Enter the event ID:")
                    val eventId = readlnOrNull()?.toLongOrNull() ?: return println("Invalid event ID")

                    println("Select ticket type (1 for Pro, 2 for Elite, 3 for Ultimate):")
                    val ticketType = readlnOrNull()?.toIntOrNull() ?: return println("Invalid ticket type")

                    try {
                        buyTicket(user, eventId, ticketType)
                    } catch (e: Exception) {
                        println(e.message)
                    }
                }
                2 -> listPurchasesByUserId(user)
                3 -> {
                    listMedalTable()
                    println("Select the sorting criteria for the medal table:")
                    println("1. Alphabetical order")
                    println("2. Order by total number of medals")
                    print("Enter your choice: ")

                    val option = readlnOrNull()

                    when (option) {
                        "1" -> {
                            MedalService.listMedalTableAlphabetically()
                        }
                        "2" -> {
                            MedalService.listMedalTableByTotalMedals()
                        }
                        else -> {
                            println("Invalid option. Returning to the main menu.")
                        }
                    }
                }
                4 -> if (user.rol == "admin") manageUsers() else println("Invalid option")
                5 -> if (user.rol == "admin") manageEvents() else println("Invalid option")
                0 -> {
                    println("Logging out...")
                    break
                }
                else -> println("Invalid option, please try again.")
            }
        }
    }

     fun register() {
        println("Enter nickname:")
        val nickName = readlnOrNull() ?: return println("Invalid input")

        if (UserRepository.findByNickname(nickName) != null) {
            println("Nickname already exists. Please choose another.")
            return
        }

        println("Enter password:")
        val password = readlnOrNull() ?: return println("Invalid input")

        println("Enter first name:")
        val name = readlnOrNull() ?: return println("Invalid input")

        println("Enter last name:")
        val surname = readlnOrNull() ?: return println("Invalid input")

        val user = User(
            id = (UserRepository.get().size + 1).toLong(),
            nickName = nickName,
            password = password,
            name = name,
            surname = surname,
            createdDate = LocalTime.now().toString()
        )

        UserRepository.add(user)
        println("Registration successful. Welcome, $nickName!")
    }
}