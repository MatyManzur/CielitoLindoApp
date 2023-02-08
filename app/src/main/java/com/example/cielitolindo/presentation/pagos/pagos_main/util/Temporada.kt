package com.example.cielitolindo.presentation.pagos.pagos_main.util

import java.time.LocalDate
import java.time.Month
import java.time.Year
import java.time.YearMonth
import java.time.format.DateTimeFormatter

data class Temporada(val firstYear: Year) {
    val secondYear = firstYear.plusYears(1)
    val firstDay = LocalDate.of(firstYear.value, Month.JULY, 1)
    val lastDay = YearMonth.of(secondYear.value, Month.JUNE).atEndOfMonth()

    fun toYearMonth(month: Month): YearMonth {
        return if (month.value >= Month.JULY.value)
            YearMonth.of(firstYear.value, month)
        else
            YearMonth.of(secondYear.value, month)
    }

    constructor(yearMonth: YearMonth) : this(
        if (yearMonth.month.value >= Month.JULY.value) Year.of(
            yearMonth.year
        ) else Year.of(yearMonth.plusYears(1).year)
    )

    override fun toString(): String {
        return "Temporada $firstYear-${secondYear.format(DateTimeFormatter.ofPattern("yy"))}"
    }
}