package main.services

import data.Purchase
import data.User
import main.domain.TicketElite
import main.domain.TicketPro
import main.domain.TicketUltimateEvent
import main.domain.Trade
import repositories.EventRepository
import repositories.PurchaseRepository
import java.time.LocalTime

object PurchaseService {
    fun buyTicket(user: User, eventId: Long, ticketType: Int) {
        val event = EventRepository.getById(eventId) ?: throw NoSuchElementException("Event not found")

        val trade: Trade = when (ticketType) {
            1 -> TicketPro()
            2 -> TicketElite()
            3 -> TicketUltimateEvent()
            else -> throw IllegalArgumentException("Invalid ticket type")
        }

        val cost = trade.tradeTicket(event)
        if (user.money < cost) throw InsufficientMoneyException("Insufficient balance for this purchase.")

        user.money -= cost
        println("Purchase successful. Remaining balance: $${user.money}")

        val seat = generateUniqueSeat(event.id)
        val purchase = Purchase(
            id = (PurchaseRepository.get().size + 1).toLong(),
            userId = user.id,
            eventId = event.id,
            amount = cost,
            createdDate = LocalTime.now().toString(),
            seat = seat ?: "N/A",
            hour = LocalTime.now()
        )
        PurchaseRepository.add(purchase)
    }

    private fun generateUniqueSeat(eventId: Long): String {
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
    }

    fun listPurchasesByUserId(user: User) {
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

    private class InsufficientMoneyException(message: String) : Exception(message)  // Excepción personalizada privada para el servicio de compras
}

