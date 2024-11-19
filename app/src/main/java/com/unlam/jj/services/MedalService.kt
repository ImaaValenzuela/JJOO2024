package main.services

import repositories.MedalTableRepository

object MedalService {
    fun listMedalTable() {
        val countries = MedalTableRepository.get()
        if (countries.isEmpty()) {
            println("No medal table data available.")
            return
        }

        countries.forEach { country ->
            println("""
                | Country      : ${country.name}
                | Gold Medals  : 🥇 ${country.goldMedals}
                | Silver Medals: 🥈 ${country.silverMedals}
                | Bronze Medals: 🥉 ${country.bronzeMedals}
                | Total Medals : ${country.goldMedals + country.silverMedals + country.bronzeMedals}
            """.trimMargin())
        }


    }

    fun listMedalTableAlphabetically() {
        val countries = MedalTableRepository.get().sortedBy { it.name }
        if (countries.isEmpty()) {
            println("No medal table data available.")
            return
        }

        countries.forEach { country ->
            println("""
                | Country      : ${country.name}
                | Gold Medals  : 🥇 ${country.goldMedals}
                | Silver Medals: 🥈 ${country.silverMedals}
                | Bronze Medals: 🥉 ${country.bronzeMedals}
                | Total Medals : ${country.goldMedals + country.silverMedals + country.bronzeMedals}
            """.trimMargin())
        }
    }

    fun listMedalTableByTotalMedals() {
        val countries = MedalTableRepository.get().sortedByDescending {
            it.goldMedals + it.silverMedals + it.bronzeMedals
        }
        if (countries.isEmpty()) {
            println("No medal table data available.")
            return
        }

        countries.forEach { country ->
            println("""
                | Country      : ${country.name}
                | Gold Medals  : 🥇 ${country.goldMedals}
                | Silver Medals: 🥈 ${country.silverMedals}
                | Bronze Medals: 🥉 ${country.bronzeMedals}
                | Total Medals : ${country.goldMedals + country.silverMedals + country.bronzeMedals}
            """.trimMargin())
        }
    }
}