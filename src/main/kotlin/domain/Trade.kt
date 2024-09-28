package main.domain

import data.Event

open class Trade {

    open fun tradeTicket(event: Event): Double {
        return 0.0 // Este se implementará en la subclase. Herencia
    }

}