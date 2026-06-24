package ni.edu.uam.jaguar_tracker.ui.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ni.edu.uam.jaguar_tracker.data.remote.RetrofitClient
import java.text.SimpleDateFormat
import java.util.Locale

data class HistoryPoint(
    val exerciseName: String,
    val dateLabel: String,
    val weight: Double,
    val reps: Int
)

data class HistoryUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val selectedExercise: String = "Sin ejercicio",
    val points: List<HistoryPoint> = emptyList(),
    val bestWeight: Double = 0.0,
    val estimatedOneRm: Double = 0.0,
    val progressPercent: Double = 0.0,
    val totalSessions: Int = 0
)

class HistoryViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(HistoryUiState())
    val uiState: StateFlow<HistoryUiState> = _uiState.asStateFlow()

    init {
        cargarHistorial()
    }

    fun cargarHistorial() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            try {
                val series = RetrofitClient.apiService.obtenerSeriesRealizadas()

                val puntos = series.mapNotNull { serie ->
                    val entrenamientoEjercicio = serie.entrenamientoEjercicio
                    val entrenamiento = entrenamientoEjercicio?.entrenamiento
                    val microcicloEjercicio = entrenamientoEjercicio?.microcicloEjercicio
                    val ejercicio = microcicloEjercicio?.rutinaEjercicio?.ejercicio

                    val nombreEjercicio = ejercicio?.nombre ?: "Ejercicio"
                    val fecha = entrenamiento?.fechaEntrenamiento ?: return@mapNotNull null
                    val peso = serie.peso ?: return@mapNotNull null
                    val reps = serie.repeticiones ?: return@mapNotNull null

                    HistoryPoint(
                        exerciseName = nombreEjercicio,
                        dateLabel = formatDate(fecha),
                        weight = peso,
                        reps = reps
                    )
                }

                val selectedExercise = puntos.firstOrNull()?.exerciseName ?: "Sin ejercicio"

                val puntosEjercicio = puntos
                    .filter { it.exerciseName == selectedExercise }
                    .takeLast(6)

                val firstWeight = puntosEjercicio.firstOrNull()?.weight ?: 0.0
                val lastWeight = puntosEjercicio.lastOrNull()?.weight ?: 0.0
                val progress =
                    if (firstWeight > 0.0 && lastWeight > 0.0) {
                        ((lastWeight - firstWeight) / firstWeight) * 100
                    } else {
                        0.0
                    }

                val bestPoint = puntosEjercicio.maxByOrNull { it.weight }
                val bestWeight = bestPoint?.weight ?: 0.0
                val estimatedOneRm =
                    if (bestPoint != null) {
                        bestPoint.weight * (1 + bestPoint.reps / 30.0)
                    } else {
                        0.0
                    }

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = null,
                        selectedExercise = selectedExercise,
                        points = puntosEjercicio,
                        bestWeight = bestWeight,
                        estimatedOneRm = estimatedOneRm,
                        progressPercent = progress,
                        totalSessions = puntosEjercicio.size
                    )
                }

            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = "No se pudo cargar el historial: ${e.message ?: "Error desconocido"}"
                    )
                }
            }
        }
    }

    private fun formatDate(rawDate: String): String {
        return try {
            val input = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
            val output = SimpleDateFormat("dd/MM", Locale.getDefault())
            val date = input.parse(rawDate)
            if (date != null) output.format(date) else rawDate.take(5)
        } catch (e: Exception) {
            rawDate.take(5)
        }
    }
}