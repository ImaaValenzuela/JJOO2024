package main

import repositories.EventRepository
import repositories.MedalTableRepository
import repositories.PurchaseRepository
import repositories.SportRepository
import repositories.UserRepository
import data.Event
import data.Purchase
import data.User
import data.Country
import data.Sport

fun main(args: Array<String>) {
    while (true) {
        println("Menu:")
        println("1. List Events")
        println("2. Get Event by ID")
        println("3. List Countries")
        println("4. Add Purchase")
        println("5. List Purchases")
        println("6. Login")
        println("0. Exit")

        when (readLine()?.toInt()) {
            1 -> listEvents()
            2 -> getEventById()
            3 -> listCountries()
            4 -> addPurchase()
            5 -> listPurchases()
            6 -> login()
            0 -> {
                println("Exiting...")
                break
            }
            else -> println("Invalid option, please try again.")
        }
    }
}

private fun listEvents() {
    val events = EventRepository.get()
    events.forEach { println(it) }
}

private fun getEventById() {
    println("Enter event ID:")
    val id = readLine()?.toLongOrNull()
    if (id != null) {
        try {
            val event = EventRepository.getById(id)
            println(event)
        } catch (e: NoSuchElementException) {
            println(e.message)
        }
    } else {
        println("Invalid ID")
    }
}

private fun listCountries() {
    val countries = MedalTableRepository.get()
    countries.forEach { println(it) }
}

private fun addPurchase() {
    println("Enter purchase details:")
    println("User ID:")
    val userId = readLine()?.toLongOrNull()
    println("Event ID:")
    val eventId = readLine()?.toLongOrNull()
    println("Amount:")
    val amount = readLine()?.toDoubleOrNull()
    println("Date (YYYY-MM-DD):")
    val date = readLine()
    println("Seat:")
    val seat = readLine()

    if (userId != null && eventId != null && amount != null && date != null && seat != null) {
        val purchase = Purchase(
            id = (PurchaseRepository.get().size + 1).toLong(),
            userId = userId,
            eventId = eventId,
            amount = amount,
            createdDate =  date,
            seat = seat
        )
        PurchaseRepository.add(purchase)
        println("Purchase added successfully")
    } else {
        println("Invalid input")
    }
}

private fun listPurchases() {
    val purchases = PurchaseRepository.get()
    purchases.forEach { println(it) }
}

private fun login() {
    println("Enter nickname:")
    val nickname = readLine()
    println("Enter password:")
    val password = readLine()

    if (nickname != null && password != null) {
        val user = UserRepository.login(nickname, password)
        if (user != null) {
            println("Login successful: $user")
        } else {
            println("Invalid credentials")
        }
    } else {
        println("Invalid input")
    }
}