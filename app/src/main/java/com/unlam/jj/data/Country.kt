package data

data class Country(
    val id: Long,
    val position: Int, // Cambio position por name, generaba error en Medal Table
    val name: String,
    val flag: String,
    val goldMedals: Int,
    val silverMedals: Int,
    val bronzeMedals: Int
)