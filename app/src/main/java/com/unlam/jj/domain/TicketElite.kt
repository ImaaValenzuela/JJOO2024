package main.domain

import data.Event
import java.time.LocalTime

class TicketElite : Trade() {

    override fun tradeTicket(event: Event): Double {
        val commissionRate = if (!event.hour.isBefore(LocalTime.of(20, 0)) && !event.hour.isAfter(LocalTime.of(23, 59))) {
            CommissionRates.PEAK_HOUR_COMMISSION
        } else {
            CommissionRates.OFF_PEAK_HOUR_COMMISSION
        }
        val cost = event.price.times(1.plus(commissionRate)) //* (1 + commissionRate)
        println("The final price for Elite is: $cost")
        return cost
    }
}