package ni.edu.uam.jaguar_tracker.ui.routine

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import ni.edu.uam.jaguar_tracker.data.model.ExerciseModel
import ni.edu.uam.jaguar_tracker.data.model.WeeklyPlanModel
import ni.edu.uam.jaguar_tracker.data.repository.RoutineRepository

data class Exercise(
    val id: Int,
    val name: String,
    val sets: String = "3",
    val reps: String = "12",
    val rir: String = "2",
    val restSeconds: String = "90",
    val isSelected: Boolean = false
)

data class WeeklyPlanUi(
    val weekNumber: Int,
    val intensity: String = "Media",
    val volume: String = "Normal"
)

data class NewRoutineUiState(
    val routineName: String = "",
    val trainingDaysText: String = "3",
    val isCustomDay: Boolean = false,
    val selectedWeekDays: Set<String> = emptySet(),
    val selectedExercises: List<Exercise> = emptyList(),
    val availableExercises: List<Exercise> = emptyList(),
    val isSheetOpen: Boolean = false,
    val advancedOptionsEnabled: Boolean = true,
    val planMesocycleEnabled: Boolean = true,
    val microcycles: Int = 4,
    val weeklyPlans: List<WeeklyPlanUi> = List(4) { WeeklyPlanUi(it + 1) },
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
            Exercise(1, "Press de banca", sets = "4", reps = "10"),
            Exercise(2, "Sentadillas", sets = "4", reps = "8"),
            Exercise(3, "Peso muerto", sets = "3", reps = "6"),
            Exercise(4, "Dominadas", sets = "3", reps = "10"),
            Exercise(5, "Curl de bíceps", sets = "3", reps = "12"),
            Exercise(6, "Press militar", sets = "4", reps = "8"),
            Exercise(7, "Remo con barra", sets = "4", reps = "10")
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

    fun updateTrainingDays(value: String) {
        val digits = value.filter { it.isDigit() }

        val normalized = when {
            digits.isBlank() -> ""
            digits.length > 1 -> digits.last().toString()
            else -> digits
        }

        val number = normalized.toIntOrNull()

        if (normalized.isBlank() || number in 1..7) {
            _uiState.update {
                it.copy(
                    trainingDaysText = normalized,
                    error = null
                )
            }
        }
    }

    fun updateDayMode(isCustom: Boolean) {
        _uiState.update {
            it.copy(
                isCustomDay = isCustom,
                error = null
            )
        }
    }

    fun toggleWeekDay(day: String) {
        _uiState.update { state ->

            val updatedDays =
                if (state.selectedWeekDays.contains(day)) {
                    state.selectedWeekDays - day
                } else {
                    state.selectedWeekDays + day
                }

            state.copy(
                selectedWeekDays = updatedDays,
                trainingDaysText = if (state.isCustomDay && updatedDays.isNotEmpty()) {
                    updatedDays.size.toString()
                } else {
                    state.trainingDaysText
                },
                error = null
            )
        }
    }

    fun updateAdvancedOptions(enabled: Boolean) {
        _uiState.update {
            it.copy(advancedOptionsEnabled = enabled)
        }
    }

    fun updatePlanMesocycle(enabled: Boolean) {
        _uiState.update {
            it.copy(planMesocycleEnabled = enabled)
        }
    }

    fun increaseMicrocycles() {
        _uiState.update { state ->
            val newValue = (state.microcycles + 1).coerceAtMost(12)

            state.copy(
                microcycles = newValue,
                weeklyPlans = buildWeeklyPlans(newValue, state.weeklyPlans)
            )
        }
    }

    fun decreaseMicrocycles() {
        _uiState.update { state ->
            val newValue = (state.microcycles - 1).coerceAtLeast(1)

            state.copy(
                microcycles = newValue,
                weeklyPlans = buildWeeklyPlans(newValue, state.weeklyPlans)
            )
        }
    }

    private fun buildWeeklyPlans(
        count: Int,
        currentPlans: List<WeeklyPlanUi>
    ): List<WeeklyPlanUi> {
        return (1..count).map { week ->
            currentPlans.firstOrNull { it.weekNumber == week } ?: WeeklyPlanUi(week)
        }
    }

    fun updateWeeklyIntensity(weekNumber: Int, intensity: String) {
        _uiState.update { state ->
            state.copy(
                weeklyPlans = state.weeklyPlans.map {
                    if (it.weekNumber == weekNumber) it.copy(intensity = intensity) else it
                }
            )
        }
    }

    fun updateWeeklyVolume(weekNumber: Int, volume: String) {
        _uiState.update { state ->
            state.copy(
                weeklyPlans = state.weeklyPlans.map {
                    if (it.weekNumber == weekNumber) it.copy(volume = volume) else it
                }
            )
        }
    }

    fun toggleSheetVisibility(isOpen: Boolean) {
        _uiState.update {
            it.copy(isSheetOpen = isOpen)
        }
    }

    fun toggleExerciseSelection(exerciseId: Int) {
        _uiState.update { state ->

            val alreadySelected = state.selectedExercises.any { it.id == exerciseId }

            val updatedAvailable = state.availableExercises.map { exercise ->
                if (exercise.id == exerciseId) {
                    exercise.copy(isSelected = !alreadySelected)
                } else {
                    exercise
                }
            }

            val updatedSelected =
                if (alreadySelected) {
                    state.selectedExercises.filterNot { it.id == exerciseId }
                } else {
                    val exerciseToAdd = state.availableExercises
                        .firstOrNull { it.id == exerciseId }
                        ?.copy(isSelected = true)

                    if (exerciseToAdd != null) {
                        state.selectedExercises + exerciseToAdd
                    } else {
                        state.selectedExercises
                    }
                }

            state.copy(
                availableExercises = updatedAvailable,
                selectedExercises = updatedSelected,
                error = null,
                wasSaved = false
            )
        }
    }

    fun removeExercise(exerciseId: Int) {
        _uiState.update { state ->

            val updatedAvailable = state.availableExercises.map { exercise ->
                if (exercise.id == exerciseId) {
                    exercise.copy(isSelected = false)
                } else {
                    exercise
                }
            }

            state.copy(
                availableExercises = updatedAvailable,
                selectedExercises = state.selectedExercises.filterNot { it.id == exerciseId },
                error = null
            )
        }
    }

    fun updateExerciseSets(exerciseId: Int, value: String) {
        updateSelectedExercise(exerciseId) {
            it.copy(sets = value.filter { char -> char.isDigit() }.take(2))
        }
    }

    fun updateExerciseReps(exerciseId: Int, value: String) {
        updateSelectedExercise(exerciseId) {
            it.copy(reps = value.take(10))
        }
    }

    fun updateExerciseRir(exerciseId: Int, value: String) {
        updateSelectedExercise(exerciseId) {
            it.copy(rir = value.take(5))
        }
    }

    fun updateExerciseRest(exerciseId: Int, value: String) {
        updateSelectedExercise(exerciseId) {
            it.copy(restSeconds = value.filter { char -> char.isDigit() }.take(4))
        }
    }

    private fun updateSelectedExercise(
        exerciseId: Int,
        transform: (Exercise) -> Exercise
    ) {
        _uiState.update { state ->
            state.copy(
                selectedExercises = state.selectedExercises.map { exercise ->
                    if (exercise.id == exerciseId) transform(exercise) else exercise
                },
                error = null
            )
        }
    }

    fun saveRoutine() {
        val state = _uiState.value

        if (state.routineName.isBlank()) {
            _uiState.update {
                it.copy(error = "Debes escribir el nombre de la rutina")
            }
            return
        }

        val trainingDays =
            if (state.isCustomDay) {
                state.selectedWeekDays.size
            } else {
                state.trainingDaysText.toIntOrNull() ?: 0
            }

        if (trainingDays !in 1..7) {
            _uiState.update {
                it.copy(error = "La rutina debe tener entre 1 y 7 días de entrenamiento")
            }
            return
        }

        if (state.isCustomDay && state.selectedWeekDays.isEmpty()) {
            _uiState.update {
                it.copy(error = "Seleccioná al menos un día personalizado")
            }
            return
        }

        if (state.selectedExercises.isEmpty()) {
            _uiState.update {
                it.copy(error = "Debes agregar al menos un ejercicio")
            }
            return
        }

        val exercisesToSave = state.selectedExercises.map { exercise ->
            ExerciseModel(
                id = exercise.id,
                name = exercise.name,
                sets = exercise.sets.toIntOrNull() ?: 3,
                reps = exercise.reps.ifBlank { "12" },
                rir = exercise.rir.ifBlank { "2" },
                restSeconds = exercise.restSeconds.toIntOrNull() ?: 90
            )
        }

        val weeklyPlansToSave =
            if (state.planMesocycleEnabled) {
                state.weeklyPlans.map {
                    WeeklyPlanModel(
                        weekNumber = it.weekNumber,
                        intensity = it.intensity,
                        volume = it.volume
                    )
                }
            } else {
                emptyList()
            }

        RoutineRepository.addRoutine(
            name = state.routineName,
            exercises = exercisesToSave,
            weeks = state.microcycles,
            trainingDays = trainingDays,
            useCustomDays = state.isCustomDay,
            selectedDays = state.selectedWeekDays.toList(),
            weeklyPlans = weeklyPlansToSave
        )

        _uiState.update {
            it.copy(
                error = null,
                wasSaved = true
            )
        }
    }
}