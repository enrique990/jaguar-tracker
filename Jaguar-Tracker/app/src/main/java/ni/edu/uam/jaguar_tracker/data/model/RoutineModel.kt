package ni.edu.uam.jaguar_tracker.data.model

data class ExerciseModel(
    val id: Int,
    val name: String,
    val sets: Int = 3,
    val reps: Int = 12
)

data class RoutineModel(
    val id: Int,
    val name: String,
    val weeks: Int = 4,
    val exercises: List<ExerciseModel> = emptyList(),
    val isSelected: Boolean = false,
    val hasEmoji: Boolean = false
)