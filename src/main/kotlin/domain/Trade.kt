package main.domain

import data.Event

abstract class Trade {

    abstract fun tradeTicket(event: Event): Double // Este se implementará en la subclase. Herencia

}