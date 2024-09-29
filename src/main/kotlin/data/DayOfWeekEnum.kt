package data

enum class DayOfWeekEnum(val dayNumber: Int) {
    MONDAY(1),
    TUESDAY(2),
    WEDNESDAY(3),
    THURSDAY(4),
    FRIDAY(5),
    SATURDAY(6),
    SUNDAY(7);

    companion object {
        fun fromDayNumber(dayNumber: Int): DayOfWeekEnum {
            return values().firstOrNull { it.dayNumber == dayNumber }
                ?: throw IllegalArgumentException("Invalid day number: $dayNumber")
        }
    }
}