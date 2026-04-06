package com.dash_tracker.domain.model

import java.util.Date

data class Habito(
    val id: Int = 0,
    val titulo: String,
    val frecuencia: TipoFrecuencia,
    val color: String, // Guardaremos el Hexadecimal ej. "#FF0000"
    val activo: Boolean = true,
    val fechaCreacion: Date = Date()
) {
    // Aquí podemos agregar la lógica de negocio pura según nuestro UML
    fun archivar(): Habito {
        return this.copy(activo = false)
    }
}