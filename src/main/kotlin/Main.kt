package main

import repositories.EventRepository
import repositories.MedalTableRepository
import repositories.PurchaseRepository
import repositories.UserRepository
import data.Purchase
import data.User
import main.domain.Trade
import main.domain.TicketPro
import main.domain.TicketElite
import main.domain.TicketUltimateEvent
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


private fun login() {
    println("Enter nickname:")
    val nickname = readlnOrNull() ?: return println("Invalid input")

    println("Enter password:")
    val password = readlnOrNull() ?: return println("Invalid input")

    UserRepository.login(nickname, password)?.let { user ->
        println("Login successful")
        loginMain(user)
    } ?: println("Invalid credentials")
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
                println("Logging out...")
                break
            }
            else -> println("Invalid option, please try again.")
        }
    }
}

private fun register() {
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



private fun buyTicket(user: User) {
    println("Enter event ID:")

    listEvents()

    val eventId = readlnOrNull()?.toLongOrNull()

    if (eventId != null) {
        try {
            val event = EventRepository.getById(eventId)

            while (true) {
                println("Ticket Type:")
                println("1. Ticket Pro ")
                println("2. Elite ")
                println("3. Ultimate Event ")

                val trade: Trade = when (readlnOrNull()?.toInt()) {
                    1 -> TicketPro()
                    2 -> TicketElite()
                    3 -> TicketUltimateEvent()
                    else -> {
                        println("Invalid option, please try again.")
                        return
                    }
                }

                val cost = trade.tradeTicket(event) // Polimorfismo

                if (user.money >= cost) {
                    user.money -= cost
                    println("Purchase successful. Remaining balance: $${user.money}")

                    val seat = generateUniqueSeat(event.id) ?: "Unable to generate a unique seat number."

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
                    println("Purchase added successfully. Seat: $seat")
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

private fun listEvents() {
    val events = EventRepository.get()

    if (events.isEmpty()) {
        println("No events available.")
        return
    }

    println("Upcoming Events:\n")
    events.forEach { event ->
        println("""
            |----------------------------------------
            | Event ID   : ${event.id}
            | Date       : ${event.date} (${event.day})
            | Time       : ${event.hour}
            | Location   : ${event.place}
            | Price      : $${event.price}
            | Sport      : ${event.sport.name}
            |----------------------------------------
        """.trimMargin())
    }
}

private fun listMedalTable() {
    val countries = MedalTableRepository.get()

    if (countries.isEmpty()) {
        println("No medal table data available.")
        return
    }

    println("Medal Table:\n")
    countries.forEach { country ->
        println("""
            |----------------------------------------
            | Country      : ${country.name}
            | Gold Medals  : 🥇 ${country.goldMedals}
            | Silver Medals: 🥈 ${country.silverMedals}
            | Bronze Medals: 🥉 ${country.bronzeMedals}
            | Total Medals : ${country.goldMedals + country.silverMedals + country.bronzeMedals}
            |----------------------------------------
        """.trimMargin())
    }
}

private fun listPurchasesByUserId(user: User) {
    val purchases = PurchaseRepository.get().filter { it.userId == user.id }

    if (purchases.isNotEmpty()) {
        println("Purchases for user: ${user.nickName}\n")
        purchases.forEach { purchase ->
            println("""
                |----------------------------------------
                | Purchase ID   : ${purchase.id}
                | Event ID      : ${purchase.eventId}
                | Amount        : $${purchase.amount}
                | Seat Number   : ${purchase.seat}
                | Purchase Date : ${purchase.createdDate}
                | Purchase Time : ${purchase.hour}
                |----------------------------------------
            """.trimMargin())
        }
    } else {
        println("No purchases found for user: ${user.nickName}")
    }
}

private fun generateUniqueSeat(eventId: Long): String? {
    val existingSeats = PurchaseRepository.get()
        .filter { it.eventId == eventId }
        .map { it.seat }
        .toSet()

    val random = java.util.Random()

    while (true) {
        val seat = "${random.nextInt(10)}${random.nextInt(10)}${('A'..'Z').random()}"
        if (seat !in existingSeats) {
            return seat
        }
    }
    return null
}