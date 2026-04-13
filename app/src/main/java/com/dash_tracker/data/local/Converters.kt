package com.dash_tracker.data.local

import androidx.room.TypeConverter
import com.dash_tracker.domain.model.TipoFrecuencia
import java.util.Date

class Converters {
    // Convierte de Date a Long (Milisegundos) para guardar en la BD
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    // Convierte de Long a Date al leer de la BD
    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }

    // Convierte el Enum a String ("DIARIA", "SEMANAL")
    @TypeConverter
    fun fromTipoFrecuencia(frecuencia: TipoFrecuencia): String {
        return frecuencia.name
    }

    // Convierte el String de vuelta al Enum
    @TypeConverter
    fun toTipoFrecuencia(frecuencia: String): TipoFrecuencia {
        return TipoFrecuencia.valueOf(frecuencia)
    }
}