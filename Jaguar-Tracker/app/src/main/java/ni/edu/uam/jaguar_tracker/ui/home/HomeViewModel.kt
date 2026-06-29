package ni.edu.uam.jaguar_tracker.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ni.edu.uam.jaguar_tracker.data.model.EjercicioDto
import ni.edu.uam.jaguar_tracker.data.model.RoutineModel
import ni.edu.uam.jaguar_tracker.data.repository.EjercicioRepository
import ni.edu.uam.jaguar_tracker.data.repository.RoutineRepository

class HomeViewModel : ViewModel() {

    val routines: StateFlow<List<RoutineModel>> = RoutineRepository.routines

    private val ejercicioRepository = EjercicioRepository()

    private val _ejercicios = MutableStateFlow<List<EjercicioDto>>(emptyList())
    val ejercicios: StateFlow<List<EjercicioDto>> = _ejercicios

    private val _isLoadingEjercicios = MutableStateFlow(false)
    val isLoadingEjercicios: StateFlow<Boolean> = _isLoadingEjercicios

    private val _errorEjercicios = MutableStateFlow<String?>(null)
    val errorEjercicios: StateFlow<String?> = _errorEjercicios

    private val _isLoadingRutinas = MutableStateFlow(false)
    val isLoadingRutinas: StateFlow<Boolean> = _isLoadingRutinas

    private val _errorRutinas = MutableStateFlow<String?>(null)
    val errorRutinas: StateFlow<String?> = _errorRutinas

    init {
        cargarRutinas()
        cargarEjercicios()
    }

    fun selectRoutine(routineId: Int) {
        RoutineRepository.selectRoutine(routineId)
    }

    fun eliminarRutina(routineId: Int) {
        viewModelScope.launch {
            try {
                RoutineRepository.eliminarRutinaBackend(routineId)
            } catch (e: Exception) {
                _errorRutinas.value = "No se pudo eliminar la rutina: ${e.message}"
            }
        }
    }

    fun saltarEntrenamiento(routineId: Int, weekNumber: Int, day: String) {
        RoutineRepository.skipWorkout(routineId, weekNumber, day)
    }

    fun cargarRutinas() {
        viewModelScope.launch {
            _isLoadingRutinas.value = true
            _errorRutinas.value = null

            try {
                RoutineRepository.cargarRutinasDesdeBackend()
            } catch (e: Exception) {
                _errorRutinas.value = e.message ?: "No se pudieron cargar las rutinas"
            } finally {
                _isLoadingRutinas.value = false
            }
        }
    }

    fun cargarEjercicios() {
        viewModelScope.launch {
            _isLoadingEjercicios.value = true
            _errorEjercicios.value = null

            try {
                _ejercicios.value = ejercicioRepository.obtenerEjercicios()
            } catch (e: Exception) {
                _errorEjercicios.value = e.message ?: "No se pudo conectar con el backend"
            } finally {
                _isLoadingEjercicios.value = false
            }
        }
    }
}