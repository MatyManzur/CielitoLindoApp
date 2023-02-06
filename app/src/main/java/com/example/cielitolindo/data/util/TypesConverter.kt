package com.example.cielitolindo.data.util

import androidx.room.TypeConverter
import com.example.cielitolindo.domain.model.Casa
import com.example.cielitolindo.domain.model.Moneda
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class TypesConverter {

    val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    @TypeConverter
    fun dateFromString(value: String?): LocalDate? {
        return value?.let { LocalDate.parse(it, formatter) }
    }

    @TypeConverter
    fun dateToString(date: LocalDate?): String? {
        return date?.format(formatter)
    }

    @TypeConverter
    fun casaFromString(value: String?): Casa? {
        return value?.let { Casa.valueOf(it) }
    }

    @TypeConverter
    fun casaToString(casa: Casa?): String? {
        return casa?.name
    }

    @TypeConverter
    fun monedaFromString(value: String?): Moneda? {
        return value?.let { Moneda.valueOf(it) }
    }

    @TypeConverter
    fun monedaToString(moneda: Moneda?): String? {
        return moneda?.name
    }
}