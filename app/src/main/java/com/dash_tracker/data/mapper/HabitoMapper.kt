package com.dash_tracker.data.mapper

import com.dash_tracker.data.local.entity.HabitoEntity
import com.dash_tracker.domain.model.Habito

// Función de extensión para convertir Entity (Base de datos) a Modelo Puro (Dominio)
fun HabitoEntity.toDomain(): Habito {
    return Habito(
        id = id,
        titulo = titulo,
        frecuencia = frecuencia,
        color = color,
        activo = activo,
        fechaCreacion = fechaCreacion
    )
}

// Función de extensión para convertir Modelo Puro (Dominio) a Entity (Base de datos)
fun Habito.toEntity(): HabitoEntity {
    return HabitoEntity(
        id = id,
        titulo = titulo,
        frecuencia = frecuencia,
        color = color,
        activo = activo,
        fechaCreacion = fechaCreacion
    )
}