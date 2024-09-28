package main.domain

import data.Event

class TicketPro : Trade() {

    override fun tradeTicket(event: Event): Double {
        val commission = 0.02
        val result = event.price * (1 + commission)
        println("The final price for Ticket Pro is: $result")
        return result
    }
}