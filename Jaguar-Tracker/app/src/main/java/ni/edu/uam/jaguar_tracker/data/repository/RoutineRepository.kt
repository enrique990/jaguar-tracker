package ni.edu.uam.jaguar_tracker.data.repository

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import ni.edu.uam.jaguar_tracker.data.model.ExerciseModel
import ni.edu.uam.jaguar_tracker.data.model.RoutineModel
import ni.edu.uam.jaguar_tracker.data.model.RutinaRequestDto
import ni.edu.uam.jaguar_tracker.data.model.RutinaResponseDto
import ni.edu.uam.jaguar_tracker.data.model.UsuarioRefDto
import ni.edu.uam.jaguar_tracker.data.model.WeeklyPlanModel
import ni.edu.uam.jaguar_tracker.data.remote.RetrofitClient
import ni.edu.uam.jaguar_tracker.data.model.EjercicioRefDto
import ni.edu.uam.jaguar_tracker.data.model.RutinaDiaRequestDto
import ni.edu.uam.jaguar_tracker.data.model.RutinaEjercicioRequestDto
import ni.edu.uam.jaguar_tracker.data.model.RutinaRefDto

object RoutineRepository {

    private val _routines = MutableStateFlow<List<RoutineModel>>(emptyList())

    val routines: StateFlow<List<RoutineModel>> = _routines.asStateFlow()

    suspend fun crearRutinaBackend(
        idUsuario: Int,
        nombre: String,
        usaMicrociclos: Boolean,
        cantidadMicrociclos: Int,
        fechaCreacion: String
    ): RutinaResponseDto {
        val request = RutinaRequestDto(
            usuario = UsuarioRefDto(idUsuario = idUsuario),
            nombre = nombre,
            usaMicrociclos = usaMicrociclos,
            cantidadMicrociclos = cantidadMicrociclos,
            fechaCreacion = fechaCreacion
        )

        return RetrofitClient.apiService.crearRutina(request)
    }

    fun addRoutine(
        name: String,
        exercises: List<ExerciseModel>,
        weeks: Int = 4,
        trainingDays: Int = 3,
        selectedDays: List<String> = emptyList(),
        weeklyPlans: List<WeeklyPlanModel> = emptyList()
    ) {
        _routines.update { currentRoutines ->

            val nextId = (currentRoutines.maxOfOrNull { it.id } ?: 0) + 1

            val updatedRoutines = currentRoutines.map {
                it.copy(
                    isSelected = false,
                    hasEmoji = false
                )
            }

            updatedRoutines + RoutineModel(
                id = nextId,
                name = name.trim(),
                weeks = weeks,
                trainingDays = trainingDays,
                selectedDays = selectedDays,
                weeklyPlans = weeklyPlans,
                exercises = exercises,
                isSelected = true,
                hasEmoji = true
            )
        }
    }

    fun selectRoutine(routineId: Int) {
        _routines.update { currentRoutines ->
            currentRoutines.map { routine ->
                routine.copy(
                    isSelected = routine.id == routineId,
                    hasEmoji = routine.id == routineId
                )
            }
        }
    }
    suspend fun crearRutinaDiaBackend(
        idRutina: Int,
        diaSemana: String
    ) {
        val request = RutinaDiaRequestDto(
            rutina = RutinaRefDto(idRutina = idRutina),
            diaSemana = diaSemana
        )

        RetrofitClient.apiService.crearRutinaDia(request)
    }

    suspend fun crearRutinaEjercicioBackend(
        idRutina: Int,
        idEjercicio: Int,
        orden: Int
    ) {
        val request = RutinaEjercicioRequestDto(
            rutina = RutinaRefDto(idRutina = idRutina),
            ejercicio = EjercicioRefDto(idEjercicio = idEjercicio),
            orden = orden
        )

        RetrofitClient.apiService.crearRutinaEjercicio(request)
    }
}