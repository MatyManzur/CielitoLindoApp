package com.example.cielitolindo.data.data_source

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.cielitolindo.data.util.TypesConverter
import com.example.cielitolindo.domain.model.Gasto

@Database(entities = [Gasto::class], version = 1, exportSchema = false)
@TypeConverters(TypesConverter::class)
abstract class GastoDatabase : RoomDatabase() {
    abstract val gastoDao: GastoDao
    companion object {
        const val DATABASE_NAME = "gasto_database"
    }
}