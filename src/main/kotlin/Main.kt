package main

import data.*
import main.domain.*
import repositories.*
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

        if(user.rol == "admin"){
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
            1 -> buyTicket(user)
            2 -> listPurchasesByUserId(user)
            3 -> listMedalTable()
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

private fun manageUsers() {
    while (true) {
        println("User Management Menu:")
        println("1. List users")
        println("2. Add user")
        println("3. Remove user")
        println("0. Back to main menu")

        when (readlnOrNull()?.toInt()) {
            1 -> listUsers()
            2 -> register()
            3 -> removeUser()
            0 -> break
            else -> println("Invalid option, please try again.")
        }
    }
}

private fun manageEvents() {
    while (true) {
        println("Event Management Menu:")
        println("1. List events")
        println("2. Add event")
        println("3. Remove event")
        println("0. Back to main menu")

        when (readlnOrNull()?.toInt()) {
            1 -> listEvents()
            2 -> addNewEvent()
            3 -> removeExistingEvent()
            0 -> break
            else -> println("Invalid option, please try again.")
        }
    }
}

private fun removeUser(){
    println("Enter user ID to remove:")
    listUsers()
    val userId = readlnOrNull()?.toLongOrNull()

    if (userId == null || UserRepository.findById(userId) == null) {
        println("User not found.")
        return
    }

    UserRepository.remove(userId)
    println("User removed successfully.")
}

private fun addNewEvent() {
    println("Enter event date (format YYYY-MM-DD):")
    val date = readlnOrNull()

    println("Enter event time (format HH:MM):")
    val timeString = readlnOrNull()
    val time = try {
        LocalTime.parse(timeString)
    } catch (e: Exception) {
        println("Invalid time format. Please try again.")
        return
    }

    println("Enter event location:")
    val place = readlnOrNull() ?: run {
        println("Invalid location. Please try again.")
        return
    }

    println("Enter event price:")
    val price = readlnOrNull()?.toDoubleOrNull() ?: run {
        println("Invalid price. Please enter a valid number.")
        return
    }

    println("Enter event sport:")
    showSports()
    val sportName = readlnOrNull()?.toInt()

    val sport = when (sportName) {
        1 -> SportRepository.getFootball()
        2 -> SportRepository.getBasketball()
        3 -> SportRepository.getAthletics()
        4 -> SportRepository.getSwimming()
        5 -> SportRepository.getGymnastics()
        6 -> SportRepository.getCycling()
        7 -> SportRepository.getRowing()
        8 -> SportRepository.getFencing()
        else -> {
            println("Sport not recognized.")
            return
        }
    }

    println("Enter event day (e.g., Monday, Tuesday, etc.):")
    val dayInput = readlnOrNull()?.uppercase() ?: run {
        println("Invalid day. Please try again.")
        return
    }

    // Input a Enum
    val day = try {
        DayOfWeekEnum.valueOf(dayInput)
    } catch (e: IllegalArgumentException) {
        println("Invalid day format. Please enter a valid day of the week (e.g., MONDAY).")
        return
    }

    val eventId = (EventRepository.get().size + 1).toLong()

    val newEvent = Event(
        id = eventId,
        date = date ?: "",
        hour = time,
        place = place,
        price = price,
        sport = sport,
        day = day
    )

    EventRepository.add(newEvent)
    println("Event added successfully with ID: $eventId")
}

private fun removeExistingEvent() {
    println("Enter event ID to remove:")
    listEvents()
    val eventId = readlnOrNull()?.toLongOrNull()

    if (eventId != null) {
        EventRepository.removeEvent(eventId)
    } else {
        println("Invalid event ID")
    }
}

private fun showSports(){
    println("1. Football")
    println("2. Basketball ")
    println("3. Athletics")
    println("4. Swimming")
    println("5. Gymnastics")
    println("6. Cycling")
    println("7. Rowing")
    println("8. Fencing")
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

private fun listUsers(){
    val users = UserRepository.get()

    if (users.isEmpty()) {
        println("No users available.")
        return
    }

    println("Registered Users:\n")
    users.forEach { user ->
        println("""
            |----------------------------------------
            | UID          : ${user.id}
            | NickName     : ${user.nickName}
            | Password     : ${user.password}
            | Name         : ${user.name}
            | Surname      : ${user.surname}
            | Money        : $${user.money}
            | Rol          : ${user.rol}
            | Created Date : ${user.createdDate}
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