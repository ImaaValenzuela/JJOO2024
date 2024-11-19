package main.domain

import data.Event

class TicketPro : Trade() {

    override fun tradeTicket(event: Event): Double {
        val result = event.price.times(1.plus(CommissionRates.PRO_COMMISSION)) // * (1 + CommissionRates.PRO_COMMISSION)
        println("The final price for Ticket Pro is: $result")
        return result
    }
}