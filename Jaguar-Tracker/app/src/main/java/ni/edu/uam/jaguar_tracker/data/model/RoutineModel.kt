package ni.edu.uam.jaguar_tracker.data.model
data class ExerciseModel(
    val id: Int,
    val name: String,
    val sets: Int = 3,
    val reps: String = "12",
    val rir: String = "2",
    val restSeconds: Int = 90
)

data class WeeklyPlanModel(
    val weekNumber: Int,
    val intensity: String = "Media",
    val volume: String = "Normal"
)

data class RoutineModel(
    val id: Int,
    val name: String,
    val weeks: Int = 4,
    val trainingDays: Int = 3,
    val selectedDays: List<String> = emptyList(),
    val weeklyPlans: List<WeeklyPlanModel> = emptyList(),
    val exercises: List<ExerciseModel> = emptyList(),
    val isSelected: Boolean = false,
    val hasEmoji: Boolean = false,
    val createdAt: String? = null,
    val skippedWorkouts: Set<String> = emptySet(),
    val completedWorkouts: Set<String> = emptySet()
)
