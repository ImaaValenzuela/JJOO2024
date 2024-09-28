package main.domain

import data.Event
import java.time.LocalTime

open class Trade {

    open fun tradeTicketPro(event: Event): Double {
        val calculation: (Double) -> Double = { price -> price * 1.02 }
        val result = calculation(event.price)
        println("The final price is: $result")
        return result // Devolvemos el precio final
    }

    open fun tradeElite(event: Event): Double {
        val startRange = LocalTime.of(20, 0)   // 20:00 hs
        val endRange = LocalTime.of(23, 59)    // 23:59 hs
        val cost = if (!event.hour.isBefore(startRange) && !event.hour.isAfter(endRange)) {
            event.price * 1.01
        } else {
            event.price * 1.03
        }
        println("The final price is: $cost")
        return cost // Devolvemos el precio final
    }

    open fun tradeUltimateEvent(event: Event): Double {
        val cost = if (event.day == "sabado" || event.day == "domingo") {
            event.price * 1.03
        } else {
            event.price * 1.0075
        }
        println("The final price is: $cost")
        return cost // Devolvemos el precio final
    }
}