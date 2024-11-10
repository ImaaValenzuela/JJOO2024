package main.services

import data.Event
import repositories.EventRepository

object EventService {
    fun listEvents() {
        val events = EventRepository.get()
        if (events.isEmpty()) {
            println("No events available.")
            return
        }

        events.forEach { event ->
            println("""
                | Event ID   : ${event.id}
                | Date       : ${event.date} (${event.day})
                | Time       : ${event.hour}
                | Location   : ${event.place}
                | Price      : $${event.price}
                | Sport      : ${event.sport.name}
            """.trimMargin())
        }
    }

    fun addEvent(newEvent: Event) {
        EventRepository.add(newEvent)
        println("Event added successfully.")
    }
}