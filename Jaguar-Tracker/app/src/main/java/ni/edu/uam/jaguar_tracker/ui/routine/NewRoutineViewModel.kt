package ni.edu.uam.jaguar_tracker.ui.routine

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import ni.edu.uam.jaguar_tracker.data.model.ExerciseModel
import ni.edu.uam.jaguar_tracker.data.repository.RoutineRepository

data class Exercise(
    val id: Int,
    val name: String,
    val sets: Int = 3,
    val reps: Int = 12,
    val isSelected: Boolean = false
)

data class NewRoutineUiState(
    val routineName: String = "",
    val selectedExercises: List<Exercise> = emptyList(),
    val availableExercises: List<Exercise> = emptyList(),
    val isSheetOpen: Boolean = false,
    val error: String? = null,
    val wasSaved: Boolean = false
)

class NewRoutineViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(NewRoutineUiState())
    val uiState: StateFlow<NewRoutineUiState> = _uiState.asStateFlow()

    init {
        loadAvailableExercises()
    }

    private fun loadAvailableExercises() {
        val mockExercises = listOf(
            Exercise(1, "Press de banca", sets = 4, reps = 10),
            Exercise(2, "Sentadillas", sets = 4, reps = 8),
            Exercise(3, "Peso muerto", sets = 3, reps = 6),
            Exercise(4, "Dominadas", sets = 3, reps = 10),
            Exercise(5, "Curl de bíceps", sets = 3, reps = 12),
            Exercise(6, "Press militar", sets = 4, reps = 8),
            Exercise(7, "Remo con barra", sets = 4, reps = 10)
        )

        _uiState.update {
            it.copy(availableExercises = mockExercises)
        }
    }

    fun updateRoutineName(newName: String) {
        _uiState.update {
            it.copy(
                routineName = newName,
                error = null,
                wasSaved = false
            )
        }
    }

    fun toggleSheetVisibility(isOpen: Boolean) {
        _uiState.update {
            it.copy(isSheetOpen = isOpen)
        }
    }

    fun toggleExerciseSelection(exerciseId: Int) {
        _uiState.update { currentState ->

            val updatedAvailable = currentState.availableExercises.map { exercise ->
                if (exercise.id == exerciseId) {
                    exercise.copy(isSelected = !exercise.isSelected)
                } else {
                    exercise
                }
            }

            val selected = updatedAvailable.filter { it.isSelected }

            currentState.copy(
                availableExercises = updatedAvailable,
                selectedExercises = selected,
                error = null,
                wasSaved = false
            )
        }
    }

    fun removeExercise(exerciseId: Int) {
        _uiState.update { currentState ->

            val updatedAvailable = currentState.availableExercises.map { exercise ->
                if (exercise.id == exerciseId) {
                    exercise.copy(isSelected = false)
                } else {
                    exercise
                }
            }

            currentState.copy(
                availableExercises = updatedAvailable,
                selectedExercises = updatedAvailable.filter { it.isSelected },
                error = null
            )
        }
    }

    fun saveRoutine() {
        val currentState = _uiState.value

        if (currentState.routineName.isBlank()) {
            _uiState.update {
                it.copy(error = "Debes escribir el nombre de la rutina")
            }
            return
        }

        if (currentState.selectedExercises.isEmpty()) {
            _uiState.update {
                it.copy(error = "Debes agregar al menos un ejercicio")
            }
            return
        }

        val exercisesToSave = currentState.selectedExercises.map { exercise ->
            ExerciseModel(
                id = exercise.id,
                name = exercise.name,
                sets = exercise.sets,
                reps = exercise.reps
            )
        }

        RoutineRepository.addRoutine(
            name = currentState.routineName,
            exercises = exercisesToSave,
            weeks = 4
        )

        _uiState.update {
            it.copy(
                error = null,
                wasSaved = true
            )
        }
    }
}