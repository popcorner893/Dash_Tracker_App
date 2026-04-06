package com.dash_tracker.domain.model

import java.util.Date

data class RegistroHabito(
    val id: Int = 0,
    val habitoId: Int, // Relación con el hábito (Foreign Key en concepto)
    val fecha: Date,
    val completado: Boolean,
    val nota: String? = null
)