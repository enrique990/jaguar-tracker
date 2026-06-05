package ni.edu.uam.jaguar_tracker.ui.session

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import ni.edu.uam.jaguar_tracker.data.model.ExerciseModel
import ni.edu.uam.jaguar_tracker.data.repository.RoutineRepository
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class SetSession(
    val number: Int,
    val previousWeight: Double = 0.0,
    val weight: String = "",
    val reps: String = "",
    val rir: String = ""
)

data class ExerciseSession(
    val exerciseId: Int,
    val name: String,
    val sets: List<SetSession>,
    val restSeconds: Int = 90
)

data class WorkoutSessionUiState(
    val routineName: String = "Entrenamiento",
    val dateLabel: String = "",
    val isKg: Boolean = true,
    val exercises: List<ExerciseSession> = emptyList(),
    val error: String? = null,
    val successMessage: String? = null
)

class WorkoutSessionViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(
        WorkoutSessionUiState(
            dateLabel = currentDateLabel(),
            exercises = defaultExerciseSessions()
        )
    )

    val uiState: StateFlow<WorkoutSessionUiState> = _uiState.asStateFlow()

    init {
        loadSelectedRoutine()
    }

    private fun loadSelectedRoutine() {
        val selectedRoutine = RoutineRepository.routines.value.firstOrNull { it.isSelected }

        if (selectedRoutine == null || selectedRoutine.exercises.isEmpty()) {
            _uiState.update {
                it.copy(
                    routineName = "Entrenamiento",
                    exercises = defaultExerciseSessions()
                )
            }
            return
        }

        _uiState.update {
            it.copy(
                routineName = selectedRoutine.name,
                exercises = selectedRoutine.exercises.map { exercise ->
                    exercise.toExerciseSession()
                }
            )
        }
    }

    private fun ExerciseModel.toExerciseSession(): ExerciseSession {
        return ExerciseSession(
            exerciseId = id,
            name = name,
            sets = (1..sets).map { setNumber ->
                SetSession(
                    number = setNumber,
                    previousWeight = 0.0,
                    weight = "",
                    reps = reps,
                    rir = rir
                )
            },
            restSeconds = restSeconds
        )
    }

    fun updateWeight(exerciseId: Int, setNumber: Int, value: String) {
        updateSet(exerciseId, setNumber) {
            it.copy(weight = cleanDecimal(value).take(6))
        }
    }

    fun updateReps(exerciseId: Int, setNumber: Int, value: String) {
        updateSet(exerciseId, setNumber) {
            it.copy(reps = onlyDigits(value).take(4))
        }
    }

    fun updateRir(exerciseId: Int, setNumber: Int, value: String) {
        updateSet(exerciseId, setNumber) {
            it.copy(rir = onlyDigits(value).take(2))
        }
    }

    private fun updateSet(
        exerciseId: Int,
        setNumber: Int,
        transform: (SetSession) -> SetSession
    ) {
        _uiState.update { state ->
            state.copy(
                exercises = state.exercises.map { exercise ->
                    if (exercise.exerciseId == exerciseId) {
                        exercise.copy(
                            sets = exercise.sets.map { set ->
                                if (set.number == setNumber) transform(set) else set
                            }
                        )
                    } else {
                        exercise
                    }
                },
                error = null,
                successMessage = null
            )
        }
    }

    fun toggleUnit(useKg: Boolean) {
        val state = _uiState.value

        if (state.isKg == useKg) return

        _uiState.update { currentState ->
            currentState.copy(
                isKg = useKg,
                exercises = currentState.exercises.map { exercise ->
                    exercise.copy(
                        sets = exercise.sets.map { set ->
                            set.copy(
                                previousWeight = convertWeight(
                                    value = set.previousWeight,
                                    fromKg = currentState.isKg,
                                    toKg = useKg
                                ),
                                weight = convertWeightText(
                                    value = set.weight,
                                    fromKg = currentState.isKg,
                                    toKg = useKg
                                )
                            )
                        }
                    )
                }
            )
        }
    }

    fun completeWorkout() {
        val state = _uiState.value

        val invalidExercise = state.exercises.firstOrNull { exercise ->
            exercise.sets.any { set ->
                !isPositiveDecimal(set.weight) ||
                        !isPositiveNumber(set.reps) ||
                        !isValidRir(set.rir)
            }
        }

        if (invalidExercise != null) {
            _uiState.update {
                it.copy(
                    error = "Revisá los datos de ${invalidExercise.name}. Peso y repeticiones deben ser mayores que 0. RIR/RPE debe estar entre 0 y 10."
                )
            }
            return
        }

        _uiState.update {
            it.copy(
                error = null,
                successMessage = "Entrenamiento completado correctamente."
            )
        }
    }

    fun dismissMessage() {
        _uiState.update {
            it.copy(
                error = null,
                successMessage = null
            )
        }
    }

    private fun onlyDigits(value: String): String {
        return value.filter { it.isDigit() }
    }

    private fun cleanDecimal(value: String): String {
        var hasDot = false

        return value.filter { char ->
            when {
                char.isDigit() -> true
                char == '.' && !hasDot -> {
                    hasDot = true
                    true
                }
                else -> false
            }
        }
    }

    private fun isPositiveNumber(value: String): Boolean {
        val number = value.toLongOrNull()
        return number != null && number > 0
    }

    private fun isPositiveDecimal(value: String): Boolean {
        val number = value.toDoubleOrNull()
        return number != null && number > 0
    }

    private fun isValidRir(value: String): Boolean {
        val number = value.toIntOrNull()
        return number != null && number in 0..10
    }

    private fun convertWeight(
        value: Double,
        fromKg: Boolean,
        toKg: Boolean
    ): Double {
        if (value <= 0.0) return value

        return when {
            fromKg && !toKg -> value * 2.20462
            !fromKg && toKg -> value / 2.20462
            else -> value
        }
    }

    private fun convertWeightText(
        value: String,
        fromKg: Boolean,
        toKg: Boolean
    ): String {
        val number = value.toDoubleOrNull() ?: return value
        val converted = convertWeight(number, fromKg, toKg)
        return formatWeight(converted)
    }

    private fun formatWeight(value: Double): String {
        return if (value % 1.0 == 0.0) {
            value.toInt().toString()
        } else {
            String.format(Locale.US, "%.1f", value)
        }
    }

    private fun currentDateLabel(): String {
        val locale = Locale("es", "ES")
        val formatter = SimpleDateFormat("EEEE, dd 'de' MMMM yyyy", locale)
        val rawDate = formatter.format(Date())

        return rawDate.replaceFirstChar {
            if (it.isLowerCase()) it.titlecase(locale) else it.toString()
        }
    }

    companion object {
        private fun defaultExerciseSessions(): List<ExerciseSession> {
            return listOf(
                ExerciseSession(
                    exerciseId = 1,
                    name = "Press de Banca",
                    sets = listOf(
                        SetSession(1, 100.0, "", "10", "2"),
                        SetSession(2, 100.0, "", "10", "2"),
                        SetSession(3, 100.0, "", "10", "2"),
                        SetSession(4, 100.0, "", "10", "2")
                    )
                ),
                ExerciseSession(
                    exerciseId = 2,
                    name = "Press Inclinado",
                    sets = listOf(
                        SetSession(1, 80.0, "", "12", "2"),
                        SetSession(2, 80.0, "", "12", "2"),
                        SetSession(3, 80.0, "", "12", "2")
                    )
                )
            )
        }
    }
}