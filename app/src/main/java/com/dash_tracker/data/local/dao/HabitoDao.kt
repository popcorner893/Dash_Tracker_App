package com.dash_tracker.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.dash_tracker.data.local.entity.HabitoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface HabitoDao {
    // Devuelve la lista de hábitos activos. Flow emite actualizaciones en tiempo real.
    @Query("SELECT * FROM habitos WHERE activo = 1 ORDER BY fechaCreacion DESC")
    fun getHabitosActivos(): Flow<List<HabitoEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHabito(habito: HabitoEntity)

    @Update
    suspend fun updateHabito(habito: HabitoEntity)

    // Nuestro borrado lógico (Soft Delete) del requerimiento REQ-008
    @Query("UPDATE habitos SET activo = 0 WHERE id = :id")
    suspend fun archivarHabito(id: Int)
}

