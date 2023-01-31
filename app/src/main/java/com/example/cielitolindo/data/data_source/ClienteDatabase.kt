package com.example.cielitolindo.data.data_source

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.cielitolindo.data.util.TypesConverter
import com.example.cielitolindo.domain.model.Cliente

@Database(entities = [Cliente::class], version = 2, exportSchema = false)
@TypeConverters(TypesConverter::class)
abstract class ClienteDatabase : RoomDatabase() {
    abstract val clienteDao: ClienteDao
    companion object {
        const val DATABASE_NAME = "cliente_database"
    }
}