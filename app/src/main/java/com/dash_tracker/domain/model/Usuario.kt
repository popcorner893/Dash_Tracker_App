package com.dash_tracker.domain.model

import java.util.Date

data class Usuario(
    val id: String = "", // Usamos String porque Firebase Auth suele devolver IDs alfanuméricos
    val nombre: String,
    val email: String,
    val fotoPerfil: String? = null, // Puede ser nulo si no sube foto
    val fechaRegistro: Date = Date()
)