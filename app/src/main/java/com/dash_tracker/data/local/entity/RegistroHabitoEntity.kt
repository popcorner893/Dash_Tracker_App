package com.dash_tracker.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.Date

@Entity(
    tableName = "registros",
    // Esto asegura que si se borra un hábito físico, se borre su historial (Regla UML)
    foreignKeys = [
        ForeignKey(
            entity = HabitoEntity::class,
            parentColumns = ["id"],
            childColumns = ["habitoId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("habitoId")] // Mejora la velocidad de búsqueda
)
data class RegistroHabitoEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val habitoId: Int,
    val fecha: Date,
    val completado: Boolean,
    val nota: String?
)