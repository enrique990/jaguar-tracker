package ni.edu.uam.jaguar_tracker.ui.routine

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ni.edu.uam.jaguar_tracker.data.model.ExerciseModel
import ni.edu.uam.jaguar_tracker.data.model.WeeklyPlanModel
import ni.edu.uam.jaguar_tracker.data.repository.EjercicioRepository
import ni.edu.uam.jaguar_tracker.data.repository.RoutineRepository
import retrofit2.HttpException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import ni.edu.uam.jaguar_tracker.data.repository.UserSessionRepository

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
    val selectedWeekDays: Set<String> = emptySet(),
    val selectedExercises: List<Exercise> = emptyList(),
    val availableExercises: List<Exercise> = emptyList(),
    val isSheetOpen: Boolean = false,
    val planMesocycleEnabled: Boolean = true,
    val microcycles: Int = 4,
    val weeklyPlans: List<WeeklyPlanUi> = List(4) { WeeklyPlanUi(it + 1) },
    val error: String? = null,
    val wasSaved: Boolean = false,
    val isEditing: Boolean = false,
    val editingRoutineId: Int? = null
)

class NewRoutineViewModel(
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(NewRoutineUiState())
    val uiState: StateFlow<NewRoutineUiState> = _uiState.asStateFlow()

    private val ejercicioRepository = EjercicioRepository()

    init {
        val routineId: Int? = savedStateHandle.get<Int>("routineId")?.takeIf { it != -1 }
        
        if (routineId != null) {
            cargarRutinaParaEditar(routineId)
        } else {
            cargarEjerciciosDesdeBackend()
        }
    }

    private fun cargarRutinaParaEditar(routineId: Int) {
        viewModelScope.launch {
            try {
                // Primero cargamos los ejercicios para tener la lista completa disponible
                val ejerciciosBackend = ejercicioRepository.obtenerEjercicios()
                
                // Buscamos la rutina en el repositorio (ya debe estar cargada en Home)
                val rutina = RoutineRepository.routines.value.find { it.id == routineId }
                
                if (rutina != null) {
                    val ejerciciosUi = ejerciciosBackend.map { ejercicio ->
                        val id = ejercicio.idEjercicio ?: -1
                        val selectedExercise = rutina.exercises.find { it.id == id }
                        
                        Exercise(
                            id = id,
                            name = ejercicio.nombre,
                            sets = (selectedExercise?.sets ?: ejercicio.serieRecomendadas).toString(),
                            reps = (selectedExercise?.reps ?: ejercicio.repeticionesRecomendadas).toString(),
                            rir = selectedExercise?.rir ?: "2",
                            restSeconds = (selectedExercise?.restSeconds ?: 90).toString(),
                            isSelected = selectedExercise != null
                        )
                    }

                    val selectedExercises = ejerciciosUi.filter { it.isSelected }

                    _uiState.update { state ->
                        state.copy(
                            routineName = rutina.name,
                            selectedWeekDays = rutina.selectedDays.toSet(),
                            availableExercises = ejerciciosUi,
                            selectedExercises = selectedExercises,
                            planMesocycleEnabled = rutina.weeklyPlans.isNotEmpty(),
                            microcycles = rutina.weeks,
                            weeklyPlans = rutina.weeklyPlans.map { plan ->
                                WeeklyPlanUi(
                                    weekNumber = plan.weekNumber,
                                    intensity = plan.intensity,
                                    volume = plan.volume
                                )
                            }.ifEmpty { List(rutina.weeks) { WeeklyPlanUi(it + 1) } },
                            isEditing = true,
                            editingRoutineId = routineId
                        )
                    }
                } else {
                    cargarEjerciciosDesdeBackend()
                }
            } catch (e: Exception) {
                showError("No se pudo cargar la rutina para editar: ${e.message}")
                cargarEjerciciosDesdeBackend()
            }
        }
    }

    fun cargarEjerciciosDesdeBackend() {
        viewModelScope.launch {
            try {
                val ejerciciosBackend = ejercicioRepository.obtenerEjercicios()

                val ejerciciosUi = ejerciciosBackend.mapNotNull { ejercicio ->
                    val id = ejercicio.idEjercicio ?: return@mapNotNull null

                    Exercise(
                        id = id,
                        name = ejercicio.nombre,
                        sets = ejercicio.serieRecomendadas.toString(),
                        reps = ejercicio.repeticionesRecomendadas.toString(),
                        rir = "2",
                        restSeconds = "90",
                        isSelected = _uiState.value.selectedExercises.any { it.id == id }
                    )
                }

                _uiState.update { state ->
                    state.copy(
                        availableExercises = ejerciciosUi,
                        error = null
                    )
                }

            } catch (e: Exception) {
                _uiState.update { state ->
                    state.copy(
                        error = "No se pudieron cargar los ejercicios del backend: ${e.message ?: "Error desconocido"}"
                    )
                }
            }
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
                error = null,
                wasSaved = false
            )
        }
    }

    fun updatePlanMesocycle(enabled: Boolean) {
        _uiState.update {
            it.copy(
                planMesocycleEnabled = enabled,
                error = null
            )
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
                    if (it.weekNumber == weekNumber) {
                        it.copy(intensity = intensity)
                    } else {
                        it
                    }
                }
            )
        }
    }

    fun updateWeeklyVolume(weekNumber: Int, volume: String) {
        _uiState.update { state ->
            state.copy(
                weeklyPlans = state.weeklyPlans.map {
                    if (it.weekNumber == weekNumber) {
                        it.copy(volume = volume)
                    } else {
                        it
                    }
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
            it.copy(sets = onlyDigits(value).take(2))
        }
    }

    fun updateExerciseReps(exerciseId: Int, value: String) {
        updateSelectedExercise(exerciseId) {
            it.copy(reps = onlyDigits(value).take(4))
        }
    }

    fun updateExerciseRir(exerciseId: Int, value: String) {
        updateSelectedExercise(exerciseId) {
            it.copy(rir = onlyDigits(value).take(2))
        }
    }

    fun updateExerciseRest(exerciseId: Int, value: String) {
        updateSelectedExercise(exerciseId) {
            it.copy(restSeconds = onlyDigits(value).take(5))
        }
    }

    private fun onlyDigits(value: String): String {
        return value.filter { it.isDigit() }
    }

    private fun updateSelectedExercise(
        exerciseId: Int,
        transform: (Exercise) -> Exercise
    ) {
        _uiState.update { state ->
            state.copy(
                selectedExercises = state.selectedExercises.map { exercise ->
                    if (exercise.id == exerciseId) {
                        transform(exercise)
                    } else {
                        exercise
                    }
                },
                error = null
            )
        }
    }

    fun dismissError() {
        _uiState.update {
            it.copy(error = null)
        }
    }

    fun saveRoutine() {
        val state = _uiState.value

        if (state.routineName.isBlank()) {
            showError("Debes escribir el nombre de la rutina")
            return
        }

        if (state.selectedWeekDays.isEmpty()) {
            showError("Seleccioná al menos un día de entrenamiento")
            return
        }

        if (state.selectedExercises.isEmpty()) {
            showError("Debes agregar al menos un ejercicio")
            return
        }

        val invalidExercise = state.selectedExercises.firstOrNull { exercise ->
            !isPositiveNumber(exercise.sets) ||
                    !isPositiveNumber(exercise.reps) ||
                    !isValidRir(exercise.rir) ||
                    !isPositiveNumber(exercise.restSeconds)
        }

        if (invalidExercise != null) {
            showError(
                "Revisá los datos de ${invalidExercise.name}. " +
                        "Series, repeticiones y descanso deben ser mayores que 0. " +
                        "RIR/RPE debe estar entre 0 y 10."
            )
            return
        }

        viewModelScope.launch {
            try {
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

                val orderedDays = listOf(
                    "Lunes",
                    "Martes",
                    "Miércoles",
                    "Jueves",
                    "Viernes",
                    "Sábado",
                    "Domingo"
                ).filter { day ->
                    state.selectedWeekDays.contains(day)
                }

                val fechaCreacion = obtenerFechaActualParaBackend()

                val idUsuario = UserSessionRepository.requerirIdUsuarioActual()

                if (state.isEditing && state.editingRoutineId != null) {
                    RoutineRepository.actualizarRutinaBackend(
                        idRutina = state.editingRoutineId,
                        idUsuario = idUsuario,
                        nombre = state.routineName.trim(),
                        usaMicrociclos = state.planMesocycleEnabled,
                        cantidadMicrociclos = state.microcycles,
                        fechaCreacion = fechaCreacion
                    )
                    // Nota: Una edición completa requeriría actualizar también días, ejercicios y microciclos.
                    // Por simplicidad en este paso, actualizamos los datos principales.
                } else {
                    val rutinaCreada = RoutineRepository.crearRutinaBackend(
                        idUsuario = idUsuario,
                        nombre = state.routineName.trim(),
                        usaMicrociclos = state.planMesocycleEnabled,
                        cantidadMicrociclos = state.microcycles,
                        fechaCreacion = fechaCreacion
                    )
                    val idRutinaBackend = rutinaCreada.idRutina
                        ?: throw Exception("El backend no devolvió idRutina")

                    orderedDays.forEach { dia ->
                        RoutineRepository.crearRutinaDiaBackend(
                            idRutina = idRutinaBackend,
                            diaSemana = dia
                        )
                    }

                    val rutinaEjercicioIds: Map<Int, Int> =
                        state.selectedExercises.mapIndexed { index, exercise ->
                            val rutinaEjercicioCreada = RoutineRepository.crearRutinaEjercicioBackend(
                                idRutina = idRutinaBackend,
                                idEjercicio = exercise.id,
                                orden = index + 1
                            )

                            val idRutinaEjercicio = rutinaEjercicioCreada.idRutinaEjercicio
                                ?: throw Exception("El backend no devolvió idRutinaEjercicio para ${exercise.name}")

                            exercise.id to idRutinaEjercicio
                        }.toMap()

                    if (state.planMesocycleEnabled) {
                        state.weeklyPlans.forEach { weekPlan ->

                            val microcicloCreado = RoutineRepository.crearMicrocicloBackend(
                                idRutina = idRutinaBackend,
                                numeroMicrociclo = weekPlan.weekNumber,
                                intensidad = weekPlan.intensity,
                                volumen = normalizarVolumenParaBackend(weekPlan.volume)
                            )

                            val idMicrociclo = microcicloCreado.idMicrociclo
                                ?: throw Exception("El backend no devolvió idMicrociclo para la semana ${weekPlan.weekNumber}")

                            state.selectedExercises.forEach { exercise ->
                                val idRutinaEjercicio = rutinaEjercicioIds[exercise.id]
                                    ?: throw Exception("No se encontró idRutinaEjercicio para ${exercise.name}")

                                RoutineRepository.crearMicrocicloEjercicioBackend(
                                    idMicrociclo = idMicrociclo,
                                    idRutinaEjercicio = idRutinaEjercicio,
                                    series = exercise.sets.toIntOrNull() ?: 3,
                                    repeticiones = exercise.reps.toIntOrNull() ?: 12,
                                    rir = exercise.rir.toIntOrNull() ?: 2,
                                    descansoSegundos = exercise.restSeconds.toIntOrNull() ?: 90
                                )
                            }
                        }
                    }

                    RoutineRepository.addRoutine(
                        name = state.routineName,
                        exercises = exercisesToSave,
                        weeks = state.microcycles,
                        trainingDays = state.selectedWeekDays.size,
                        selectedDays = orderedDays,
                        weeklyPlans = weeklyPlansToSave
                    )
                }

                _uiState.update {
                    it.copy(
                        error = null,
                        wasSaved = true
                    )
                }

            } catch (e: HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                showError("Error backend ${e.code()}: ${errorBody ?: e.message ?: "Sin detalle"}")

            } catch (e: Exception) {
                showError("No se pudo guardar la rutina en el backend: ${e.message ?: "Error desconocido"}")
            }
        }
    }

    private fun obtenerFechaActualParaBackend(): String {
        val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
        return formatter.format(Date())
    }

    private fun showError(message: String) {
        _uiState.update {
            it.copy(error = message)
        }
    }

    private fun isPositiveNumber(value: String): Boolean {
        val number = value.toLongOrNull()
        return number != null && number > 0
    }

    private fun isValidRir(value: String): Boolean {
        val number = value.toLongOrNull()
        return number != null && number in 0..10
    }

    private fun normalizarVolumenParaBackend(volume: String): String {
        return when (volume) {
            "Normal" -> "Moderado"
            else -> volume
        }
    }
}