package com.dash_tracker.domain.model

data class Recordatorio(
    val id: Int = 0,
    val habitoId: Int,
    val horaMinuto: String, // Formato "HH:mm" (ej. "08:30")
    val diasActivos: List<Int>, // 1 = Lunes, 2 = Martes, etc.
    val esActivo: Boolean = true
)
