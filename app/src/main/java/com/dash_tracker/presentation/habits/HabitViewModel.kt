package com.dash_tracker.presentation.habits

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dash_tracker.domain.model.*
import com.dash_tracker.data.repository.HabitoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.datetime.*
import kotlinx.datetime.TimeZone
import java.util.Date
import javax.inject.Inject
import androidx.core.graphics.toColorInt

@HiltViewModel
class HabitViewModel @Inject constructor(
    private val habitoRepository: HabitoRepository
) : ViewModel() {

    val habitos: StateFlow<List<Habito>> = habitoRepository.getHabitosActivos()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // NUEVO: Escuchamos todo el historial de checks en tiempo real
    val registros: StateFlow<List<RegistroHabito>> = habitoRepository.getAllRegistros()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Combina Hábitos + Registros para la pantalla HabitListScreen
    val habitosProgress: StateFlow<List<HabitProgressUI>> = combine(habitos, registros) { listaHabitos: List<Habito>, listaRegistros: List<RegistroHabito> ->
        listaHabitos.map { habito ->
            val registrosDelHabito = listaRegistros.filter { it.habitoId == habito.id }

            // Calculamos los últimos 7 días
            val historialDias = (6 downTo 0).map { daysAgo ->
                val date = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date.minus(daysAgo, DateTimeUnit.DAY)
                val millis = date.atStartOfDayIn(TimeZone.currentSystemDefault()).toEpochMilliseconds()

                val registroEseDia = registrosDelHabito.find { it.fecha.time == millis }
                val isHoy = daysAgo == 0

                val estado = when {
                    registroEseDia?.completado == true -> TipoEstadoDia.COMPLETADO
                    registroEseDia?.completado == false -> TipoEstadoDia.FALLIDO // <-- AÑADIMOS LA EQUIS
                    isHoy && registroEseDia == null -> TipoEstadoDia.HOY // Vacío hoy (Verde)
                    else -> TipoEstadoDia.PENDIENTE // Vacío en el pasado (Gris)
                }

                DayStatus(
                    nombreDia = date.dayOfWeek.name.take(3).lowercase().replaceFirstChar { it.uppercase() },
                    numeroDia = date.dayOfMonth.toString(),
                    estado = estado
                )
            }

            val completados = registrosDelHabito.count { it.completado }
            val porcentaje = if (registrosDelHabito.isEmpty()) 0 else (completados * 100) / registrosDelHabito.size

            HabitProgressUI(
                id = habito.id,
                titulo = habito.titulo,
                frecuencia = habito.frecuencia.name,
                colorHex = try {
                    habito.color.toColorInt().toLong() } catch (_: Exception) { 0xFF4A89F3 },
                racha = completados, // Racha básica por ahora
                porcentaje = porcentaje,
                historialDias = historialDias
            )
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun crearHabito(titulo: String, categoria: CategoriaHabito, frecuencia: TipoFrecuencia, color: String, fecha: Date = Date()) {
        viewModelScope.launch { habitoRepository.crearHabito(Habito(0, titulo, categoria, frecuencia, color, true, fecha)) }
    }

    fun actualizarEstadoHabito(habitoId: Int, estado: Int, fecha: Date) {
        viewModelScope.launch {
            when (estado) {
                1 -> habitoRepository.hacerCheckIn(habitoId, fecha, completado = true)
                2 -> habitoRepository.hacerCheckIn(habitoId, fecha, completado = false)
                0 -> habitoRepository.eliminarCheckIn(habitoId, fecha)
            }
        }
    }
}
