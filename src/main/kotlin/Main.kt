package main

import repositories.EventRepository
import repositories.MedalTableRepository
import repositories.PurchaseRepository
import repositories.UserRepository
import data.Purchase
import data.User
import main.domain.Trade
import java.time.LocalTime

private class InsufficientMoneyException(message: String) : Exception(message)


fun main(args: Array<String>) {
    while (true) {
        println("Menu:")
        println("1. Login")
        println("2. Register")
        println("0. Exit")

        when (readlnOrNull()?.toInt()) {
            1 -> {
                login()
            }
            2 -> {
                register()
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

private fun listEvents() {
    val events = EventRepository.get()
    events.forEach { println(it) }
}

private fun listPurchasesByUserId(user : User) {
    val purchases = PurchaseRepository.get().filter { it.userId == user.id }
    if(purchases.isNotEmpty()){
        purchases.forEach { println(it) }
    } else println("No purchases found for user : ${user.nickName}")
}

private fun login() {
    println("Enter nickname:")
    val nickname = readlnOrNull()
    println("Enter password:")
    val password = readlnOrNull()

    if (nickname != null && password != null) {
        val user = UserRepository.login(nickname, password)
        if (user != null) {
            println("Login successful: $user")
            loginMain(user)
        } else {
            println("Invalid credentials")
        }
    } else {
        println("Invalid input")
    }
}

private fun loginMain(user : User){
    while (true) {
        println("Menu:")
        println ("Welcome ${user.nickName}")
        println("1. Buy ticket ")
        println("2. See purchases ")
        println("3. See medal table ")
        println("0. Exit ")
        when (readlnOrNull()?.toInt()) {
            1 -> buyTicket(user)
            2 -> listPurchasesByUserId(user)
            3 -> listMedalTable()
            0 -> {
                println("Exiting...")
                break
            }
            else -> println("Invalid option, please try again.")
        }
    }
}

private fun buyTicket(user: User) {
    println("Enter event ID:")

    listEvents()

    val eventId = readlnOrNull()?.toLongOrNull()

    if (eventId != null) {
        try {
            val event = EventRepository.getById(eventId)
            val trade = Trade()

            while (true) {
                println("Ticket Type:")
                println("1. Ticket Pro ")
                println("2. Elite ")
                println("3. Ultimate Event ")

                val cost = when (readlnOrNull()?.toInt()) {
                    1 -> trade.tradeTicketPro(event)
                    2 -> trade.tradeElite(event)
                    3 -> trade.tradeUltimateEvent(event)
                    else -> {
                        println("Invalid option, please try again.")
                        return
                    }
                }

                if (user.money >= cost) {
                    user.money -= cost
                    println("Purchase successful. Remaining balance: ${user.money}")

                    println("Enter seat number:")
                    val seat = readlnOrNull()

                    if (seat != null) {
                        val purchase = Purchase(
                            id = (PurchaseRepository.get().size + 1).toLong(),
                            userId = user.id,
                            eventId = event.id,
                            amount = cost,
                            createdDate = java.time.LocalDate.now().toString(),
                            seat = seat,
                            hour = LocalTime.now()
                        )
                        PurchaseRepository.add(purchase)
                        println("Purchase added successfully.")
                    } else {
                        println("Invalid seat number.")
                    }
                } else {
                    throw InsufficientMoneyException("Insufficient balance for this purchase.")
                }
                break
            }
        } catch (e: NoSuchElementException) {
            println("Event not found: ${e.message}")
        } catch (e: InsufficientMoneyException) {
            println(e.message)
        }
    } else {
        println("Invalid event ID.")
    }
}

private fun register(){
    println("Enter nickname: ")
    val nickName = readlnOrNull()

    if(nickName != null && UserRepository.findByNickname(nickName) != null){
        println("Nickname already exists. Please choose a different one")
        return
    }

    println("Enter password: ")
    val password = readlnOrNull()

    println("Enter name: ")
    val name = readlnOrNull()

    println("Enter surname: ")
    val surname = readlnOrNull()


    if (nickName != null && password != null && name != null && surname != null) {
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
    } else{
        println("Registration failed")
    }


}

private fun listMedalTable(){
    val countries = MedalTableRepository.get()
    countries.forEach { println("""
        Country : ${it.name}
        Gold Medals : ${it.goldMedals}
        Silver Medals : ${it.silverMedals}
        Bronze Medals : ${it.bronzeMedals}
        ----------------------------------------
    """.trimIndent()) }
}