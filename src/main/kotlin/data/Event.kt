package data

import java.time.LocalTime

data class Event(
    val id: Long,
    val date: String,
    val hour: LocalTime,
    val place: String,
    val price: Double,
    val sport: Sport,
    val day: String
)
