package main.domain

import data.Event

class TicketUltimateEvent : Trade() {

    override fun tradeTicket(event: Event): Double {
        val commissionCalculator: (String) -> Double = { day ->
            if (day == "sabado" || day == "domingo") 0.03 else 0.0075
        }
        val cost = event.price * (1 + commissionCalculator(event.day.toString()))
        println("The final price for Ultimate Event is: $cost")
        return cost
    }
}