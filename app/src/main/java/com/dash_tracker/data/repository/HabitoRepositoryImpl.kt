package com.dash_tracker.data.repository

import com.dash_tracker.data.local.dao.HabitoDao
import com.dash_tracker.data.local.dao.RegistroHabitoDao // <--- NUEVO
import com.dash_tracker.data.local.entity.RegistroHabitoEntity // <--- NUEVO
import com.dash_tracker.data.mapper.toDomain
import com.dash_tracker.data.mapper.toEntity
import com.dash_tracker.domain.model.Habito
import com.dash_tracker.domain.model.RegistroHabito
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.Date // <--- NUEVO

// Agregamos RegistroHabitoDao al constructor
class HabitoRepositoryImpl(
    private val habitoDao: HabitoDao,
    private val registroHabitoDao: RegistroHabitoDao // <--- NUEVO
) : HabitoRepository {

    override fun getHabitosActivos(): Flow<List<Habito>> {
        return habitoDao.getHabitosActivos().map { listaEntities ->
            listaEntities.map { entity -> entity.toDomain() }
        }
    }

    override suspend fun crearHabito(habito: Habito) {
        habitoDao.insertHabito(habito.toEntity())
    }

    override suspend fun archivarHabito(id: Int) {
        habitoDao.archivarHabito(id)
    }

    override suspend fun sincronizarConNube() {
        // TODO: Lógica
    }

    // --- NUEVA FUNCIÓN IMPLEMENTADA PARA EL CHECK-IN ---
    override suspend fun hacerCheckIn(habitoId: Int, fecha: Date, completado: Boolean) {
        // 1. ELIMINAMOS cualquier registro duplicado de ese mismo día para evitar el bug del botón
        registroHabitoDao.deleteRegistroPorFecha(habitoId, fecha)

        // 2. GUARDAMOS el nuevo registro limpio
        val registro = RegistroHabitoEntity(
            habitoId = habitoId,
            fecha = fecha,
            completado = completado,
            nota = null
        )
        registroHabitoDao.insertRegistro(registro)
    }

    override suspend fun eliminarCheckIn(habitoId: Int, fecha: Date) {
        registroHabitoDao.deleteRegistroPorFecha(habitoId, fecha)
    }

    override fun getAllRegistros(): Flow<List<RegistroHabito>> {
        return registroHabitoDao.getAllRegistros().map { lista ->
            lista.map { it.toDomain() }
        }
    }

}