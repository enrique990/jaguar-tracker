package ni.edu.uam.jaguar_tracker.ui.routine

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

// --- POO: Encapsulamiento del Modelo de Dominio ---
data class Exercise(
    val id: Int,
    val name: String,
    val sets: Int = 3,
    val reps: Int = 12,
    val isSelected: Boolean = false
)

// --- POO: Encapsulamiento del Estado de la Vista (State) ---
data class NewRoutineUiState(
    val routineName: String = "",
    val selectedExercises: List<Exercise> = emptyList(),
    val availableExercises: List<Exercise> = emptyList(),
    val isSheetOpen: Boolean = false
)

class NewRoutineViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(NewRoutineUiState())
    val uiState: StateFlow<NewRoutineUiState> = _uiState.asStateFlow()

    init {
        loadAvailableExercises()
    }

    // Simulamos la carga de datos que Harry nos enviará desde el Backend (Spring Boot)
    private fun loadAvailableExercises() {
        val mockExercises = listOf(
            Exercise(1, "Press de banca"),
            Exercise(2, "Sentadillas"),
            Exercise(3, "Peso muerto"),
            Exercise(4, "Dominadas"),
            Exercise(5, "Curl de bíceps")
        )
        _uiState.update { it.copy(availableExercises = mockExercises) }
    }

    fun updateRoutineName(newName: String) {
        _uiState.update { it.copy(routineName = newName) }
    }

    fun toggleSheetVisibility(isOpen: Boolean) {
        _uiState.update { it.copy(isSheetOpen = isOpen) }
    }

    fun toggleExerciseSelection(exerciseId: Int) {
        _uiState.update { currentState ->
            // Actualizamos la lista de disponibles marcando o desmarcando
            val updatedAvailable = currentState.availableExercises.map {
                if (it.id == exerciseId) it.copy(isSelected = !it.isSelected) else it
            }

            // Filtramos los que están seleccionados para la lista de la rutina
            val selected = updatedAvailable.filter { it.isSelected }

            currentState.copy(
                availableExercises = updatedAvailable,
                selectedExercises = selected
            )
        }
    }

    fun saveRoutine() {
        // TODO: Andrés conectará esto con Retrofit para enviar el JSON a Harry
        val currentName = _uiState.value.routineName
        val currentExercises = _uiState.value.selectedExercises
        println("Guardando rutina: $currentName con ${currentExercises.size} ejercicios")
    }
}