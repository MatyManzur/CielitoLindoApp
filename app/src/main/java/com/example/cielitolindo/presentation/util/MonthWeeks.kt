package com.example.cielitolindo.presentation.util

import java.time.LocalDate
import java.time.Month

class MonthWeeks {
    companion object {
        fun getWeeksOfMonth(month: Month, year: Int): List<List<LocalDate>> {
            val firstDay = LocalDate.of(year, month, 1)
            val weekDayOfFirstDay = firstDay.dayOfWeek.value % 7
            var currentDay = firstDay.minusDays(weekDayOfFirstDay.toLong())
            val ans = mutableListOf<List<LocalDate>>()
            for (i in 1..6) {
                val week = mutableListOf<LocalDate>()
                for (j in 1..7) {
                    week.add(currentDay)
                    currentDay = currentDay.plusDays(1)
                }
                ans.add(week)
            }
            return ans
        }
    }
}