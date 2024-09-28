package main.domain

import data.Event
import java.time.LocalTime

class TicketElite : Trade() {

    override fun tradeTicket(event: Event): Double {
        val commissionCalculator: (LocalTime) -> Double = { hour ->
            val startRange = LocalTime.of(20, 0)
            val endRange = LocalTime.of(23, 59)
            if (!hour.isBefore(startRange) && !hour.isAfter(endRange)) 0.01 else 0.03
        }
        val cost = event.price * (1 + commissionCalculator(event.hour))
        println("The final price for Elite is: $cost")
        return cost
    }
}