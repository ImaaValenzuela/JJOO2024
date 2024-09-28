package repositories

import data.Event
import java.time.LocalTime

object EventRepository {

    private val events = mutableListOf<Event>()

    init {
        //agregamos dias random
        events.add(
            Event(
                1L,
                "2024/07/02",
                LocalTime.parse("10:30"),
                "PSG Stadium",
                150000.00,
                SportRepository.getFootball(),
                "martes"
            )
        )

        events.add(
            Event(
                2L,
                "2024-07-12",
                LocalTime.parse("15:00"),
                "Olympic Arena",
                200500.75,
                SportRepository.getBasketball(),
                "lunes"
            )
        )

        events.add(
            Event(
                3L,
                "2024-07-25",
                LocalTime.parse("18:45"),
                "National Stadium",
                119000.99,
                SportRepository.getAthletics(),
                "domingo"
            )
        )

        events.add(
            Event(
                4L,
                "2024-07-30",
                LocalTime.parse("12:00"),
                "Main Arena",
                180999.20,
                SportRepository.getSwimming(),
                "sabado"
            )
        )

        events.add(
            Event(
                5L,
                "2024-08-01",
                LocalTime.parse("20:30"),
                "City Sports Complex",
                161500.45,
                SportRepository.getGymnastics(),
                "miercoles"
            )
        )

        events.add(
            Event(
                6L,
                "2024-08-07",
                LocalTime.parse("17:00"),
                "Regional Stadium",
                140250.10,
                SportRepository.getCycling(),
                "jueves"
            )
        )

        events.add(
            Event(
                7L,
                "2024-08-10",
                LocalTime.parse("14:00"),
                "Victory Stadium",
                130125.35,
                SportRepository.getRowing(),
                "domingo"
            )
        )

        events.add(
            Event(
                8L,
                "2024-08-15",
                LocalTime.parse("16:30"),
                "Championship Arena",
                190750.85,
                SportRepository.getFencing(),
                "sabado"
            )
        )

        events.add(
            Event(
                9L,
                "2024-08-18",
                LocalTime.parse("11:15"),
                "International Stadium",
                175300.99,
                SportRepository.getJudo(),
                "lunes"
            )
        )

        events.add(
            Event(
                10L,
                "2024-08-25",
                LocalTime.parse("13:45"),
                "Olympic Park",
                210000.70,
                SportRepository.getTennis(),
                "sabado"
            )
        )


    }

    fun get() : List<Event> {
        return events //TODO Implementar solucion para obtener los tickets
    }

    fun getById(id: Long) : Event {
        return events.firstOrNull { it.id == id }
            ?: throw NoSuchElementException("No se encontró el evento con id $id") //TODO Implementar solucion para obtener el ticket solicitado
    }

}