package main.services

import data.DayOfWeekEnum
import data.Event
import main.services.AuthService.register
import main.services.EventService.listEvents
import repositories.EventRepository
import repositories.SportRepository
import repositories.UserRepository
import java.time.LocalTime

object AdminService {
    fun manageUsers() {
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

    fun manageEvents() {
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
}