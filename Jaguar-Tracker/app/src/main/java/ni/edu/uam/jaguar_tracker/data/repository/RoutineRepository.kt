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
import ni.edu.uam.jaguar_tracker.data.model.MicrocicloEjercicioRequestDto
import ni.edu.uam.jaguar_tracker.data.model.MicrocicloEjercicioResponseDto
import ni.edu.uam.jaguar_tracker.data.model.MicrocicloRefDto
import ni.edu.uam.jaguar_tracker.data.model.MicrocicloRequestDto
import ni.edu.uam.jaguar_tracker.data.model.MicrocicloResponseDto
import ni.edu.uam.jaguar_tracker.data.model.RutinaEjercicioRefDto
import ni.edu.uam.jaguar_tracker.data.model.RutinaEjercicioResponseDto

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

    suspend fun actualizarRutinaBackend(
        idRutina: Int,
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

        val response = RetrofitClient.apiService.actualizarRutina(idRutina, request)
        
        cargarRutinasDesdeBackend() // Recargamos todo para estar sincronizados
        
        return response
    }

    suspend fun eliminarRutinaBackend(idRutina: Int) {
        RetrofitClient.apiService.eliminarRutina(idRutina)
        
        _routines.update { currentRoutines ->
            currentRoutines.filterNot { it.id == idRutina }
        }
    }

    fun addRoutine(
        name: String,
        exercises: List<ExerciseModel>,
        weeks: Int = 4,
        trainingDays: Int = 3,
        selectedDays: List<String> = emptyList(),
        weeklyPlans: List<WeeklyPlanModel> = emptyList(),
        createdAt: String? = null
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
                hasEmoji = true,
                createdAt = createdAt,
                skippedWorkouts = emptySet(),
                completedWorkouts = emptySet()
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

    fun skipWorkout(routineId: Int, weekNumber: Int, day: String) {
        _routines.update { currentRoutines ->
            currentRoutines.map { routine ->
                if (routine.id == routineId) {
                    routine.copy(skippedWorkouts = routine.skippedWorkouts + "$weekNumber-$day")
                } else {
                    routine
                }
            }
        }
    }

    fun completeWorkout(routineId: Int, weekNumber: Int, day: String) {
        _routines.update { currentRoutines ->
            currentRoutines.map { routine ->
                if (routine.id == routineId) {
                    routine.copy(completedWorkouts = routine.completedWorkouts + "$weekNumber-$day")
                } else {
                    routine
                }
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
    ): RutinaEjercicioResponseDto {
        val request = RutinaEjercicioRequestDto(
            rutina = RutinaRefDto(idRutina = idRutina),
            ejercicio = EjercicioRefDto(idEjercicio = idEjercicio),
            orden = orden
        )

        return RetrofitClient.apiService.crearRutinaEjercicio(request)
    }

    suspend fun crearMicrocicloBackend(
        idRutina: Int,
        numeroMicrociclo: Int,
        intensidad: String,
        volumen: String
    ): MicrocicloResponseDto {
        val request = MicrocicloRequestDto(
            rutina = RutinaRefDto(idRutina = idRutina),
            numeroMicrociclo = numeroMicrociclo,
            intensidad = intensidad,
            volumen = volumen
        )

        return RetrofitClient.apiService.crearMicrociclo(request)
    }

    suspend fun crearMicrocicloEjercicioBackend(
        idMicrociclo: Int,
        idRutinaEjercicio: Int,
        series: Int,
        repeticiones: Int,
        rir: Int,
        descansoSegundos: Int
    ): MicrocicloEjercicioResponseDto {
        val request = MicrocicloEjercicioRequestDto(
            microciclo = MicrocicloRefDto(idMicrociclo = idMicrociclo),
            rutinaEjercicio = RutinaEjercicioRefDto(idRutinaEjercicio = idRutinaEjercicio),
            series = series,
            repeticiones = repeticiones,
            rir = rir,
            descansoSegundos = descansoSegundos
        )

        return RetrofitClient.apiService.crearMicrocicloEjercicio(request)
    }

    suspend fun cargarRutinasDesdeBackend() {
        val rutinasBackend = RetrofitClient.apiService.obtenerRutinas()

        val rutinaDiasBackend = try {
            RetrofitClient.apiService.obtenerRutinaDias()
        } catch (e: Exception) {
            emptyList()
        }

        val rutinaEjerciciosBackend = try {
            RetrofitClient.apiService.obtenerRutinaEjercicios()
        } catch (e: Exception) {
            emptyList()
        }

        val microciclosBackend = try {
            RetrofitClient.apiService.obtenerMicrociclos()
        } catch (e: Exception) {
            emptyList()
        }

        val microcicloEjerciciosBackend = try {
            RetrofitClient.apiService.obtenerMicrocicloEjercicios()
        } catch (e: Exception) {
            emptyList()
        }

        val entrenamientosBackend = try {
            RetrofitClient.apiService.obtenerEntrenamientos()
        } catch (e: Exception) {
            emptyList()
        }

        val completedMap = entrenamientosBackend
            .filter { it.completado == true }
            .groupBy { it.rutina?.idRutina }
            .mapValues { entry ->
                entry.value.mapNotNull { ent ->
                    val week = ent.microciclo?.numeroMicrociclo
                    // Note: Day name is not in EntrenamientoResponseDto usually.
                    // This is a limitation. For now we assume if a microcycle has a completed training, 
                    // it counts. But the user has multiple days per microcycle.
                    // We'll use local state for specific days if possible.
                    null // Can't easily map back to "Lunes" from backend without more info
                }
            }

        val diasPorRutina = rutinaDiasBackend.groupBy { item ->
            item.rutina?.idRutina
        }

        val ejerciciosPorRutina = rutinaEjerciciosBackend.groupBy { item ->
            item.rutina?.idRutina
        }

        val microciclosPorRutina = microciclosBackend.groupBy { item ->
            item.rutina?.idRutina
        }

        val parametrosPorRutinaEjercicio = microcicloEjerciciosBackend
            .groupBy { item ->
                item.rutinaEjercicio?.idRutinaEjercicio
            }
            .mapValues { entry ->
                val items = entry.value

                items.firstOrNull { item ->
                    item.microciclo?.numeroMicrociclo == 1
                } ?: items.firstOrNull()
            }

        val ordenDias = listOf(
            "Lunes",
            "Martes",
            "Miércoles",
            "Jueves",
            "Viernes",
            "Sábado",
            "Domingo"
        )

        val rutinasUi = rutinasBackend.mapIndexedNotNull { index, rutina ->
            val idRutina = rutina.idRutina ?: return@mapIndexedNotNull null

            val selectedDays = diasPorRutina[idRutina]
                .orEmpty()
                .mapNotNull { it.diaSemana }
                .distinct()
                .sortedBy { dia ->
                    ordenDias.indexOf(dia).takeIf { it >= 0 } ?: 99
                }

            val exercises = ejerciciosPorRutina[idRutina]
                .orEmpty()
                .sortedBy { it.orden ?: 999 }
                .mapNotNull { rutinaEjercicio ->
                    val ejercicio = rutinaEjercicio.ejercicio ?: return@mapNotNull null
                    val idEjercicio = ejercicio.idEjercicio ?: return@mapNotNull null

                    val parametros = parametrosPorRutinaEjercicio[
                        rutinaEjercicio.idRutinaEjercicio
                    ]

                    ExerciseModel(
                        id = idEjercicio,
                        name = ejercicio.nombre,
                        sets = parametros?.series ?: ejercicio.serieRecomendadas,
                        reps = (parametros?.repeticiones ?: ejercicio.repeticionesRecomendadas).toString(),
                        rir = (parametros?.rir ?: 2).toString(),
                        restSeconds = parametros?.descansoSegundos ?: 90
                    )
                }

            val weeklyPlans = microciclosPorRutina[idRutina]
                .orEmpty()
                .sortedBy { it.numeroMicrociclo ?: 999 }
                .mapNotNull { microciclo ->
                    val numero = microciclo.numeroMicrociclo ?: return@mapNotNull null

                    WeeklyPlanModel(
                        weekNumber = numero,
                        intensity = microciclo.intensidad ?: "Media",
                        volume = microciclo.volumen ?: "Normal"
                    )
                }

            RoutineModel(
                id = idRutina,
                name = rutina.nombre ?: "Rutina sin nombre",
                weeks = rutina.cantidadMicrociclos ?: weeklyPlans.size.takeIf { it > 0 } ?: 4,
                trainingDays = selectedDays.size.takeIf { it > 0 } ?: 3,
                selectedDays = selectedDays,
                weeklyPlans = weeklyPlans,
                exercises = exercises,
                isSelected = index == 0,
                hasEmoji = index == 0,
                createdAt = rutina.fechaCreacion,
                skippedWorkouts = emptySet(),
                completedWorkouts = emptySet()
            )
        }

        _routines.value = rutinasUi
    }
}