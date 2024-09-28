package data

import java.time.LocalTime

data class Purchase(
    val id: Long,
    val userId: Long,
    val eventId: Long,
    val amount: Double,
    val createdDate: String,
    val seat: String,
    val hour: LocalTime
)