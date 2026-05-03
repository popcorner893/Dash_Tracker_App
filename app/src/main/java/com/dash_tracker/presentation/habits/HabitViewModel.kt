package com.dash_tracker.presentation.habits

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dash_tracker.domain.model.CategoriaHabito
import com.dash_tracker.domain.model.Habito
import com.dash_tracker.domain.model.TipoFrecuencia
import com.dash_tracker.domain.repository.HabitoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class HabitViewModel @Inject constructor(
    private val habitoRepository: HabitoRepository
) : ViewModel() {

    // Lee los hábitos desde la base de datos en tiempo real
    val habitos: StateFlow<List<Habito>> = habitoRepository.getHabitosActivos()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun crearHabito(
        titulo: String,
        categoria: CategoriaHabito,
        frecuencia: TipoFrecuencia,
        color: String,
        fechaCreacion: Date = Date()
    ) {
        viewModelScope.launch {
            val nuevoHabito = Habito(
                titulo = titulo,
                categoria = categoria,
                frecuencia = frecuencia,
                color = color,
                activo = true,
                fechaCreacion = fechaCreacion
            )
            habitoRepository.crearHabito(nuevoHabito)
        }
    }
}
