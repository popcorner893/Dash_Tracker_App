package com.dash_tracker.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.dash_tracker.domain.model.TipoFrecuencia
import java.util.Date

@Entity(tableName = "habitos")
data class HabitoEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val titulo: String,
    val frecuencia: TipoFrecuencia, // Gracias a Converters.kt, Room ya sabe guardar esto
    val color: String,
    val activo: Boolean,
    val fechaCreacion: Date
)