package com.example.cielitolindo.data.data_source

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.cielitolindo.data.util.TypesConverter
import com.example.cielitolindo.domain.model.Cobro

@Database(entities = [Cobro::class], version = 1, exportSchema = false)
@TypeConverters(TypesConverter::class)
abstract class CobroDatabase : RoomDatabase() {
    abstract val cobroDao: CobroDao
    companion object {
        const val DATABASE_NAME = "cobro_database"
    }
}