package com.example.cielitolindo.data.data_source

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.cielitolindo.data.util.TypesConverter
import com.example.cielitolindo.domain.model.Reserva

@Database(entities = [Reserva::class], version = 2, exportSchema = false)
@TypeConverters(TypesConverter::class)
abstract class ReservaDatabase : RoomDatabase() {
    abstract val reservaDao: ReservaDao
    companion object {
        const val DATABASE_NAME = "reserva_database"
    }
}