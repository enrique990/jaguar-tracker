package ni.edu.uam.jaguar_tracker.ui.session

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import ni.edu.uam.jaguar_tracker.data.model.ExerciseModel
import ni.edu.uam.jaguar_tracker.data.repository.RoutineRepository

data class WorkoutSessionUiState(
    val routineName: String = "Entrenamiento",
    val exercises: List<ExerciseSession> = emptyList()
)

class WorkoutSessionViewModel : ViewModel() {

    val uiState: StateFlow<WorkoutSessionUiState> =
        RoutineRepository.routines
            .map { routines ->
                val selectedRoutine = routines.firstOrNull { it.isSelected }

                val exercises =
                    if (selectedRoutine != null && selectedRoutine.exercises.isNotEmpty()) {
                        selectedRoutine.exercises.map { exercise ->
                            exercise.toExerciseSession()
                        }
                    } else {
                        defaultExerciseSessions()
                    }

                WorkoutSessionUiState(
                    routineName = selectedRoutine?.name ?: "Entrenamiento",
                    exercises = exercises
                )
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = WorkoutSessionUiState(
                    routineName = "Entrenamiento",
                    exercises = defaultExerciseSessions()
                )
            )

    private fun ExerciseModel.toExerciseSession(): ExerciseSession {
        return ExerciseSession(
            name = name,
            sets = (1..sets).map { setNumber ->
                SetSession(
                    number = setNumber,
                    previousWeight = 0,
                    weight = "",
                    reps = reps.toString(),
                    rir = "2"
                )
            },
            restSeconds = 90
        )
    }

    private fun defaultExerciseSessions(): List<ExerciseSession> {
        return listOf(
            ExerciseSession(
                name = "Press de Banca",
                sets = listOf(
                    SetSession(1, 100, "100", "10", "2"),
                    SetSession(2, 100, "100", "10", "2"),
                    SetSession(3, 100, "100", "10", "2"),
                    SetSession(4, 100, "100", "10", "2")
                )
            ),
            ExerciseSession(
                name = "Press Inclinado",
                sets = listOf(
                    SetSession(1, 80, "80", "12", "2"),
                    SetSession(2, 80, "80", "12", "2"),
                    SetSession(3, 80, "80", "12", "2")
                )
            )
        )
    }
}
