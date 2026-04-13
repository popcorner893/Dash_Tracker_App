package com.dash_tracker.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.dash_tracker.data.local.entity.RegistroHabitoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RegistroHabitoDao {
    // Obtiene el historial de un hábito en particular
    @Query("SELECT * FROM registros WHERE habitoId = :habitoId ORDER BY fecha DESC")
    fun getRegistrosPorHabito(habitoId: Int): Flow<List<RegistroHabitoEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRegistro(registro: RegistroHabitoEntity)

    @Query("DELETE FROM registros WHERE id = :registroId")
    suspend fun deleteRegistro(registroId: Int)
}