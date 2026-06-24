package ni.edu.uam.jaguar_tracker.ui.session

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ni.edu.uam.jaguar_tracker.data.model.ExerciseModel
import ni.edu.uam.jaguar_tracker.data.remote.RetrofitClient
import ni.edu.uam.jaguar_tracker.data.repository.RoutineRepository
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import ni.edu.uam.jaguar_tracker.data.model.EntrenamientoEjercicioRefDto
import ni.edu.uam.jaguar_tracker.data.model.EntrenamientoEjercicioRequestDto
import ni.edu.uam.jaguar_tracker.data.model.EntrenamientoRefDto
import ni.edu.uam.jaguar_tracker.data.model.EntrenamientoRequestDto
import ni.edu.uam.jaguar_tracker.data.model.MicrocicloEjercicioRefDto
import ni.edu.uam.jaguar_tracker.data.model.MicrocicloRefDto
import ni.edu.uam.jaguar_tracker.data.model.RutinaRefDto
import ni.edu.uam.jaguar_tracker.data.model.SerieRealizadaRequestDto
import ni.edu.uam.jaguar_tracker.data.model.UsuarioRefDto
import ni.edu.uam.jaguar_tracker.data.repository.UserSessionRepository

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
    val restSeconds: Int = 90,
    val microcicloEjercicioId: Int? = null
)

data class WorkoutSessionUiState(
    val routineId: Int? = null,
    val microcicloId: Int? = null,
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
            exercises = emptyList()
        )
    )

    val uiState: StateFlow<WorkoutSessionUiState> = _uiState.asStateFlow()

    init {
        loadSelectedRoutine()
    }

    private fun loadSelectedRoutine() {
        viewModelScope.launch {
            val selectedRoutine = RoutineRepository.routines.value.firstOrNull { it.isSelected }
                ?: RoutineRepository.routines.value.firstOrNull()

            if (selectedRoutine == null) {
                _uiState.update {
                    it.copy(
                        routineId = null,
                        microcicloId = null,
                        routineName = "Entrenamiento",
                        exercises = defaultExerciseSessions()
                    )
                }
                return@launch
            }

            _uiState.update {
                it.copy(
                    routineId = selectedRoutine.id,
                    routineName = selectedRoutine.name,
                    exercises = selectedRoutine.exercises.map { exercise ->
                        exercise.toExerciseSession()
                    }
                )
            }

            try {
                val rutinaEjerciciosBackend = RetrofitClient.apiService.obtenerRutinaEjercicios()
                    .filter { rutinaEjercicio ->
                        rutinaEjercicio.rutina?.idRutina == selectedRoutine.id
                    }
                    .sortedBy { rutinaEjercicio ->
                        rutinaEjercicio.orden ?: 999
                    }

                val microciclosBackend = RetrofitClient.apiService.obtenerMicrociclos()
                    .filter { microciclo ->
                        microciclo.rutina?.idRutina == selectedRoutine.id
                    }
                    .sortedBy { microciclo ->
                        microciclo.numeroMicrociclo ?: 999
                    }

                val microcicloBase = microciclosBackend.firstOrNull { microciclo ->
                    microciclo.numeroMicrociclo == 1
                } ?: microciclosBackend.firstOrNull()

                val microcicloEjerciciosBackend = RetrofitClient.apiService.obtenerMicrocicloEjercicios()

                val parametrosPorRutinaEjercicio = microcicloEjerciciosBackend
                    .filter { microcicloEjercicio ->
                        val idMicrocicloBase = microcicloBase?.idMicrociclo

                        if (idMicrocicloBase != null) {
                            microcicloEjercicio.microciclo?.idMicrociclo == idMicrocicloBase
                        } else {
                            microcicloEjercicio.microciclo?.rutina?.idRutina == selectedRoutine.id
                        }
                    }
                    .associateBy { microcicloEjercicio ->
                        microcicloEjercicio.rutinaEjercicio?.idRutinaEjercicio
                    }

                val exercisesFromBackend = rutinaEjerciciosBackend.mapNotNull { rutinaEjercicio ->
                    val ejercicio = rutinaEjercicio.ejercicio ?: return@mapNotNull null
                    val idEjercicio = ejercicio.idEjercicio ?: return@mapNotNull null

                    val parametros = parametrosPorRutinaEjercicio[
                        rutinaEjercicio.idRutinaEjercicio
                    ]

                    val localExercise = selectedRoutine.exercises.firstOrNull {
                        it.id == idEjercicio
                    }

                    val series = parametros?.series
                        ?: localExercise?.sets
                        ?: ejercicio.serieRecomendadas

                    val reps = parametros?.repeticiones?.toString()
                        ?: localExercise?.reps
                        ?: ejercicio.repeticionesRecomendadas.toString()

                    val rir = parametros?.rir?.toString()
                        ?: localExercise?.rir
                        ?: "2"

                    val restSeconds = parametros?.descansoSegundos
                        ?: localExercise?.restSeconds
                        ?: 90

                    ExerciseSession(
                        exerciseId = idEjercicio,
                        name = ejercicio.nombre,
                        sets = (1..series.coerceAtLeast(1)).map { setNumber ->
                            SetSession(
                                number = setNumber,
                                previousWeight = 0.0,
                                weight = "",
                                reps = reps,
                                rir = rir
                            )
                        },
                        restSeconds = restSeconds,
                        microcicloEjercicioId = parametros?.idMicrocicloEjercicio
                    )
                }

                if (exercisesFromBackend.isNotEmpty()) {
                    _uiState.update {
                        it.copy(
                            routineId = selectedRoutine.id,
                            microcicloId = microcicloBase?.idMicrociclo,
                            routineName = selectedRoutine.name,
                            exercises = exercisesFromBackend,
                            error = null
                        )
                    }
                } else if (selectedRoutine.exercises.isNotEmpty()) {
                    _uiState.update {
                        it.copy(
                            routineId = selectedRoutine.id,
                            microcicloId = microcicloBase?.idMicrociclo,
                            routineName = selectedRoutine.name,
                            exercises = selectedRoutine.exercises.map { exercise ->
                                exercise.toExerciseSession()
                            },
                            error = null
                        )
                    }
                } else {
                    _uiState.update {
                        it.copy(
                            routineId = selectedRoutine.id,
                            microcicloId = microcicloBase?.idMicrociclo,
                            routineName = selectedRoutine.name,
                            exercises = defaultExerciseSessions(),
                            error = "No se encontraron ejercicios para esta rutina."
                        )
                    }
                }

            } catch (e: Exception) {
                val fallbackExercises =
                    if (selectedRoutine.exercises.isNotEmpty()) {
                        selectedRoutine.exercises.map { exercise ->
                            exercise.toExerciseSession()
                        }
                    } else {
                        defaultExerciseSessions()
                    }

                _uiState.update {
                    it.copy(
                        routineId = selectedRoutine.id,
                        routineName = selectedRoutine.name,
                        exercises = fallbackExercises,
                        error = "No se pudieron cargar los parámetros del entrenamiento: ${e.message ?: "Error desconocido"}"
                    )
                }
            }
        }
    }

    private fun ExerciseModel.toExerciseSession(): ExerciseSession {
        return ExerciseSession(
            exerciseId = id,
            name = name,
            sets = (1..sets.coerceAtLeast(1)).map { setNumber ->
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

        val idRutina = state.routineId
        if (idRutina == null) {
            _uiState.update {
                it.copy(error = "No se encontró la rutina seleccionada.")
            }
            return
        }

        val idMicrociclo = state.microcicloId
        if (idMicrociclo == null) {
            _uiState.update {
                it.copy(error = "No se encontró el microciclo del entrenamiento.")
            }
            return
        }

        val exerciseWithoutRelation = state.exercises.firstOrNull {
            it.microcicloEjercicioId == null
        }

        if (exerciseWithoutRelation != null) {
            _uiState.update {
                it.copy(error = "No se encontró la relación de microciclo para ${exerciseWithoutRelation.name}.")
            }
            return
        }

        viewModelScope.launch {
            try {
                val idUsuario = UserSessionRepository.requerirIdUsuarioActual()
                val entrenamientoCreado = RetrofitClient.apiService.crearEntrenamiento(
                    EntrenamientoRequestDto(
                        usuario = UsuarioRefDto(idUsuario = idUsuario),
                        rutina = RutinaRefDto(idRutina = idRutina),
                        microciclo = MicrocicloRefDto(idMicrociclo = idMicrociclo),
                        fechaEntrenamiento = currentDateTimeForBackend(),
                        completado = true
                    )
                )

                val idEntrenamiento = entrenamientoCreado.idEntrenamiento
                    ?: throw Exception("El backend no devolvió idEntrenamiento")

                state.exercises.forEach { exercise ->
                    val idMicrocicloEjercicio = exercise.microcicloEjercicioId
                        ?: throw Exception("No se encontró idMicrocicloEjercicio para ${exercise.name}")

                    val entrenamientoEjercicioCreado =
                        RetrofitClient.apiService.crearEntrenamientoEjercicio(
                            EntrenamientoEjercicioRequestDto(
                                entrenamiento = EntrenamientoRefDto(
                                    idEntrenamiento = idEntrenamiento
                                ),
                                microcicloEjercicio = MicrocicloEjercicioRefDto(
                                    idMicrocicloEjercicio = idMicrocicloEjercicio
                                )
                            )
                        )

                    val idEntrenamientoEjercicio =
                        entrenamientoEjercicioCreado.idEntrenamientoEjercicio
                            ?: throw Exception("El backend no devolvió idEntrenamientoEjercicio para ${exercise.name}")

                    exercise.sets.forEach { set ->
                        RetrofitClient.apiService.crearSerieRealizada(
                            SerieRealizadaRequestDto(
                                entrenamientoEjercicio = EntrenamientoEjercicioRefDto(
                                    idEntrenamientoEjercicio = idEntrenamientoEjercicio
                                ),
                                numeroSerie = set.number,
                                peso = set.weight.toDoubleOrNull() ?: 0.0,
                                repeticiones = set.reps.toIntOrNull() ?: 0,
                                rir = set.rir.toIntOrNull() ?: 0,
                                unidadMedida = if (state.isKg) "kg" else "lbs"
                            )
                        )
                    }
                }

                _uiState.update {
                    it.copy(
                        error = null,
                        successMessage = "Entrenamiento guardado correctamente en el backend."
                    )
                }

            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        error = "No se pudo guardar el entrenamiento: ${e.message ?: "Error desconocido"}"
                    )
                }
            }
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
    private fun currentDateTimeForBackend(): String {
        val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
        return formatter.format(Date())
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
                    ),
                    restSeconds = 90
                ),
                ExerciseSession(
                    exerciseId = 2,
                    name = "Press Inclinado",
                    sets = listOf(
                        SetSession(1, 80.0, "", "12", "2"),
                        SetSession(2, 80.0, "", "12", "2"),
                        SetSession(3, 80.0, "", "12", "2")
                    ),
                    restSeconds = 90
                )
            )
        }
    }
}