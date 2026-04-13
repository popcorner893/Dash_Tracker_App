package com.dash_tracker.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.dash_tracker.data.local.dao.HabitoDao
import com.dash_tracker.data.local.dao.RegistroHabitoDao
import com.dash_tracker.data.local.entity.HabitoEntity
import com.dash_tracker.data.local.entity.RegistroHabitoEntity

@Database(
    entities =[HabitoEntity::class, RegistroHabitoEntity::class],
    version = 1, // Si en el futuro agregamos columnas, cambiamos esto a 2
    exportSchema = false
)
@TypeConverters(Converters::class) // Le decimos a Room que use nuestros convertidores
abstract class DashTrackerDatabase : RoomDatabase() {

    // Exponemos los DAOs
    abstract val habitoDao: HabitoDao
    abstract val registroHabitoDao: RegistroHabitoDao

    // Nota: No necesitamos patrón Singleton porque usaremos Hilt (Inyección de dependencias)
    // para proveer esta base de datos a toda la app.
}