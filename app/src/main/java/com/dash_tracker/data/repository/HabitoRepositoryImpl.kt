package com.dash_tracker.data.repository

import com.dash_tracker.data.local.dao.HabitoDao
import com.dash_tracker.data.mapper.toDomain
import com.dash_tracker.data.mapper.toEntity
import com.dash_tracker.domain.model.Habito
import com.dash_tracker.domain.repository.HabitoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// Esta clase hereda de la interfaz del dominio
class HabitoRepositoryImpl(
    private val habitoDao: HabitoDao
    // Más adelante agregaremos aquí a FirebaseFirestore
) : HabitoRepository {

    override fun getHabitosActivos(): Flow<List<Habito>> {
        // Leemos de SQLite (rápido, funciona sin internet) y lo mapeamos al Dominio
        return habitoDao.getHabitosActivos().map { listaEntities ->
            listaEntities.map { entity -> entity.toDomain() }
        }
    }

    override suspend fun crearHabito(habito: Habito) {
        // 1. Lo guardamos en SQLite local (La interfaz se actualiza al instante)
        habitoDao.insertHabito(habito.toEntity())

        // 2. TODO: Aquí agregaremos el código para enviarlo a Firebase (Nube)
    }

    override suspend fun archivarHabito(id: Int) {
        // 1. Lo ocultamos en la BD local
        habitoDao.archivarHabito(id)

        // 2. TODO: Aquí agregaremos el código para actualizar el estado en Firebase
    }

    override suspend fun sincronizarConNube() {
        // TODO: Lógica para comparar qué hay en SQLite y qué hay en Firebase
    }
}