package main.domain

import data.Event

class TicketUltimateEvent : Trade() {

    override fun tradeTicket(event: Event): Double {
        val commissionRate = if (event.day.toString() == "sabado" || event.day.toString() == "domingo") {
            CommissionRates.WEEKEND_COMMISSION
        } else {
            CommissionRates.WEEKDAY_COMMISSION
        }
        val cost = event.price.times(1.plus(commissionRate)) //* (1 + commissionRate)
        println("The final price for Ultimate Event is: $cost")
        return cost
    }
}