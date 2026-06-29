package ni.edu.uam.jaguar_tracker.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ni.edu.uam.jaguar_tracker.R
import ni.edu.uam.jaguar_tracker.ui.theme.*
import androidx.lifecycle.viewmodel.compose.viewModel
import ni.edu.uam.jaguar_tracker.data.model.RoutineModel
import java.time.LocalDate
import java.time.DayOfWeek



data class Workout(
    val day: String,
    val name: String,
    val difficulty: String,
    val difficultyColor: Color,
    val duration: Int,
    val exercises: Int,
    val isLocked: Boolean = false,
    val isCurrent: Boolean = false,
    val isDone: Boolean = false,
    val isSkipped: Boolean = false,
    val weekNumber: Int = 1
)

data class Week(
    val number: Int,
    val workouts: List<Workout>,
    val isExpanded: Boolean = false,
    val hasEmoji: Boolean = false
)

@Composable
fun HomeScreen(
    onNewRoutineClick: (Int?) -> Unit = {},
    onStartWorkoutClick: (Workout) -> Unit = {},
    onProfileClick: () -> Unit = {},
    onHistoryClick: () -> Unit = {},
    onRankingClick: () -> Unit = {},
    homeViewModel: HomeViewModel = viewModel(),
    modifier: Modifier = Modifier
) {
    val routines by homeViewModel.routines.collectAsState()
    val selectedRoutine = routines.firstOrNull { it.isSelected }
    val weeks = buildWeeksFromSelectedRoutine(selectedRoutine)


    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = JaguarBlack,
        bottomBar = {
            JaguarBottomNavigation(
                onProfileClick = onProfileClick,
                onHomeClick = {}, // Already at Home
                onHistoryClick = onHistoryClick,
                onRankingClick = onRankingClick
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item { Spacer(modifier = Modifier.height(8.dp)) }

            // Rutinas
            item {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = stringResource(R.string.routines_title),
                        style = MaterialTheme.typography.titleMedium,
                        color = JaguarWhite
                    )
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {items(routines) { routine ->
                        RoutineCard(
                            routine = routine,
                            onClick = {
                                homeViewModel.selectRoutine(routine.id)
                            },
                            onEditClick = {
                                onNewRoutineClick(routine.id)
                            },
                            onDeleteClick = {
                                homeViewModel.eliminarRutina(routine.id)
                            }
                        )
                    }
                    }
                }
            }

            // Nueva Rutina Button
            item {
                Button(
                    onClick = { onNewRoutineClick(null) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                    contentPadding = PaddingValues()
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                brush = Brush.linearGradient(
                                    colors = listOf(Color(0xFF00D1FF), Color(0xFF00E6B3))
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = stringResource(R.string.new_routine_button),
                            color = JaguarBlack,
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.titleSmall.copy(fontSize = 16.sp)
                        )
                    }
                }
            }


            // Weeks
            if (routines.isEmpty()) {
                item {
                    EmptyHomeRoutineCard()
                }
            } else {
                items(weeks) { week ->
                    WeekAccordion(
                        week = week,
                        onStartWorkoutClick = { workout ->
                            onStartWorkoutClick(workout)
                        },
                        onSkipWorkoutClick = { workout ->
                            selectedRoutine?.let {
                                homeViewModel.saltarEntrenamiento(it.id, workout.weekNumber, workout.day)
                            }
                        }
                    )
                }
            }

            item { Spacer(modifier = Modifier.height(16.dp)) }
        }
    }
}

@Composable
fun RoutineCard(
    routine: RoutineModel,
    onClick: () -> Unit = {},
    onEditClick: () -> Unit = {},
    onDeleteClick: () -> Unit = {}
) {
    var showMenu by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .width(160.dp)
            .height(84.dp)
            .clickable { onClick() }
            .border(
                width = 1.dp,
                color = if (routine.isSelected) JaguarGreen else JaguarBorder,
                shape = RoundedCornerShape(12.dp)
            ),
        colors = CardDefaults.cardColors(containerColor = JaguarCard),
        shape = RoundedCornerShape(12.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(12.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "${routine.name}${if (routine.hasEmoji) " 💪" else ""}",
                    color = if (routine.isSelected) JaguarGreen else JaguarWhite,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = "${routine.weeks} semanas",
                    color = JaguarGray,
                    style = MaterialTheme.typography.labelSmall
                )
            }

            IconButton(
                onClick = { showMenu = true },
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .size(32.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = "Opciones",
                    tint = JaguarGray,
                    modifier = Modifier.size(16.dp)
                )
            }

            DropdownMenu(
                expanded = showMenu,
                onDismissRequest = { showMenu = false },
                modifier = Modifier.background(JaguarCard)
            ) {
                DropdownMenuItem(
                    text = { Text("Editar", color = Color.White) },
                    onClick = {
                        showMenu = false
                        onEditClick()
                    }
                )
                DropdownMenuItem(
                    text = { Text("Eliminar", color = Color.Red) },
                    onClick = {
                        showMenu = false
                        onDeleteClick()
                    }
                )
            }
        }
    }
}

@Composable
fun WeekAccordion(
    week: Week,
    onStartWorkoutClick: (Workout) -> Unit = {},
    onSkipWorkoutClick: (Workout) -> Unit = {}
) {
    var expanded by remember { mutableStateOf(week.isExpanded) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(JaguarSurface, RoundedCornerShape(12.dp))
            .padding(vertical = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = !expanded }
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.week_label, week.number) + if (week.hasEmoji) " 💪" else "",
                color = JaguarWhite,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleMedium
            )
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Icon(Icons.Outlined.Info, contentDescription = null, tint = JaguarGreen, modifier = Modifier.size(20.dp))
                Icon(
                    if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = null,
                    tint = JaguarGray,
                    modifier = Modifier.size(20.dp)
                )
                Icon(Icons.Default.Remove, contentDescription = null, tint = JaguarRed, modifier = Modifier.size(20.dp))
            }
        }

        if (expanded && week.workouts.isNotEmpty()) {
            Column(
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // "A SEGUIR" Label if any workout is current
                if (week.workouts.any { it.isCurrent }) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(JaguarBorder, RoundedCornerShape(4.dp))
                            .padding(vertical = 4.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = stringResource(R.string.to_follow),
                            color = JaguarGray,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                week.workouts.forEach { workout ->
                    WorkoutCard(
                        workout = workout,
                        onStartWorkoutClick = { onStartWorkoutClick(workout) },
                        onSkipClick = { onSkipWorkoutClick(workout) }
                    )
                }
            }
        }
    }
}

@Composable
fun WorkoutCard(
    workout: Workout,
    onStartWorkoutClick: (Workout) -> Unit = {},
    onSkipClick: () -> Unit = {}
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = when {
                    workout.isDone -> JaguarGreen
                    workout.isSkipped -> JaguarGray
                    workout.isCurrent -> JaguarGreen
                    else -> JaguarBorder
                },
                shape = RoundedCornerShape(12.dp)
            ),
        colors = CardDefaults.cardColors(
            containerColor = if (workout.isSkipped) JaguarSurface else JaguarCard
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (workout.isCurrent) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .background(JaguarTeal, CircleShape)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                    }
                    Text(
                        text = workout.day,
                        color = when {
                            workout.isDone -> JaguarGreen
                            workout.isSkipped -> JaguarGray
                            workout.isCurrent -> JaguarTeal
                            else -> JaguarGray
                        },
                        style = MaterialTheme.typography.labelLarge
                    )
                    
                    if (workout.isDone) {
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "(Hecho)",
                            color = JaguarGreen,
                            style = MaterialTheme.typography.labelSmall
                        )
                    }
                    if (workout.isSkipped) {
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "(Saltado)",
                            color = JaguarGray,
                            style = MaterialTheme.typography.labelSmall
                        )
                    }
                }
                Text(
                    text = workout.name,
                    color = if (workout.isSkipped) JaguarGray else JaguarWhite,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(6.dp)
                                .background(if (workout.isSkipped) JaguarGray else workout.difficultyColor, CircleShape)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = workout.difficulty,
                            color = JaguarGray,
                            style = MaterialTheme.typography.labelSmall
                        )
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Schedule, contentDescription = null, tint = JaguarGray, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = stringResource(R.string.duration_format, workout.duration),
                            color = JaguarGray,
                            style = MaterialTheme.typography.labelSmall
                        )
                    }
                    Text(
                        text = stringResource(R.string.exercises_count, workout.exercises),
                        color = JaguarGray,
                        style = MaterialTheme.typography.labelSmall
                    )
                }
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                if (workout.isCurrent && !workout.isDone) {
                    TextButton(
                        onClick = onSkipClick,
                        colors = ButtonDefaults.textButtonColors(contentColor = JaguarRed)
                    ) {
                        Text("Saltar")
                    }
                    
                    Spacer(modifier = Modifier.width(8.dp))
                }

                if (workout.isLocked) {
                    Icon(Icons.Default.Lock, contentDescription = null, tint = JaguarGray, modifier = Modifier.size(24.dp))
                } else if (workout.isDone) {
                    Icon(Icons.Default.CheckCircle, contentDescription = null, tint = JaguarGreen, modifier = Modifier.size(28.dp))
                } else if (workout.isSkipped) {
                    Icon(Icons.Default.Block, contentDescription = null, tint = JaguarGray, modifier = Modifier.size(24.dp))
                } else {
                    IconButton(
                        onClick = { onStartWorkoutClick(workout) },
                        modifier = Modifier
                            .size(40.dp)
                            .background(JaguarTeal, CircleShape)
                    ) {
                        Icon(Icons.Default.PlayArrow, contentDescription = "Play", tint = JaguarBlack)
                    }
                }
            }
        }
    }
}

@Composable
fun JaguarBottomNavigation(
    selectedTabIndex: Int = 0,
    onProfileClick: () -> Unit = {},
    onHomeClick: () -> Unit = {},
    onHistoryClick: () -> Unit = {},
    onRankingClick: () -> Unit = {}
) {
    NavigationBar(
        containerColor = JaguarBlack,
        tonalElevation = 0.dp,
        modifier = Modifier.height(80.dp)
    ) {
        val items = listOf(
            Triple(stringResource(R.string.nav_inicio), Icons.Default.Home, 0),
            Triple(stringResource(R.string.nav_historial), Icons.Default.History, 1),
            Triple(stringResource(R.string.nav_ranking), Icons.Default.EmojiEvents, 2),
            Triple(stringResource(R.string.nav_perfil), Icons.Default.Person, 3)
        )

        items.forEachIndexed { index, (label, icon, itemIndex) ->
            val selected = selectedTabIndex == itemIndex
            NavigationBarItem(
                selected = selected,
                onClick = { 
                    when (index) {
                        0 -> onHomeClick()
                        1 -> onHistoryClick()
                        2 -> onRankingClick()
                        3 -> onProfileClick()
                    }
                },
                icon = {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            icon,
                            contentDescription = label,
                            tint = if (selected) JaguarTeal else JaguarGray,
                            modifier = Modifier.size(26.dp)
                        )
                        if (selected) {
                            Spacer(modifier = Modifier.height(4.dp))
                            Box(
                                modifier = Modifier
                                    .width(20.dp)
                                    .height(3.dp)
                                    .background(JaguarGreen, RoundedCornerShape(2.dp))
                            )
                        }
                    }
                },
                label = {
                    Text(
                        text = label,
                        color = if (selected) JaguarTeal else JaguarGray,
                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp),
                        modifier = Modifier.padding(top = 4.dp)
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = Color.Transparent,
                    selectedIconColor = JaguarTeal,
                    unselectedIconColor = JaguarGray,
                    selectedTextColor = JaguarTeal,
                    unselectedTextColor = JaguarGray
                )
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0A0A0A)
@Composable
fun HomeScreenPreview() {
    JaguarTrackerTheme {
        HomeScreen()
    }
}
fun buildWeeksFromSelectedRoutine(routine: RoutineModel?): List<Week> {
    if (routine == null) {
        return emptyList()
    }

    val orderedDays = listOf(
        "Lunes",
        "Martes",
        "Miércoles",
        "Jueves",
        "Viernes",
        "Sábado",
        "Domingo"
    )

    // Parse createdAt date to know when the routine started
    val creationDate = try {
        LocalDate.parse(routine.createdAt?.substringBefore('T'))
    } catch (_: Exception) {
        LocalDate.now()
    }

    val creationDayOfWeek = creationDate.dayOfWeek
    val creationDayIndex = when (creationDayOfWeek) {
        DayOfWeek.MONDAY -> 0
        DayOfWeek.TUESDAY -> 1
        DayOfWeek.WEDNESDAY -> 2
        DayOfWeek.THURSDAY -> 3
        DayOfWeek.FRIDAY -> 4
        DayOfWeek.SATURDAY -> 5
        DayOfWeek.SUNDAY -> 6
    }

    val selectedDays =
        if (routine.selectedDays.isNotEmpty()) {
            orderedDays.filter { day -> routine.selectedDays.contains(day) }
        } else {
            orderedDays.take(routine.trainingDays)
        }

    val exercisesCount = routine.exercises.size.coerceAtLeast(1)

    // Decidir si la Semana 1 debe filtrar días o empezar completa
    // Si hoy es Domingo y NO elegí Domingo, la Semana 1 filtrada quedaría vacía.
    // En ese caso, mejor NO filtrar y que la Semana 1 empiece "la próxima semana" completa.
    val hasAvailableDaysInWeek1 = selectedDays.any { day ->
        orderedDays.indexOf(day) >= creationDayIndex
    }

    // Encontrar el primer entrenamiento pendiente (ni hecho ni saltado)
    var foundCurrent = false

    return (1..routine.weeks).map { weekNumber ->
        // ... (resto del mapeo)

        val weeklyPlan = routine.weeklyPlans.firstOrNull {
            it.weekNumber == weekNumber
        }

        val difficulty = weeklyPlan?.intensity ?: "Media"

        val difficultyColor = when (difficulty) {
            "Baja" -> Color(0xFF32CD32)
            "Media" -> Color(0xFFFFA500)
            "Alta" -> Color(0xFFFF4500)
            "Descarga" -> Color(0xFF00D1FF)
            else -> Color(0xFFFFA500)
        }

        val workouts = selectedDays.mapNotNull { day ->
            val dayIndexInOrdered = orderedDays.indexOf(day)

            // Solo filtramos si hay al menos un día disponible esta semana.
            // Si no hay ninguno (ej. hoy domingo y no entreno hoy), mostramos la semana 1 completa para el futuro.
            if (weekNumber == 1 && hasAvailableDaysInWeek1 && dayIndexInOrdered < creationDayIndex) {
                return@mapNotNull null
            }

            val workoutId = "$weekNumber-$day"
            val isDone = routine.completedWorkouts.contains(workoutId)
            val isSkipped = routine.skippedWorkouts.contains(workoutId)
            
            val isCurrent = if (!foundCurrent && !isDone && !isSkipped) {
                foundCurrent = true
                true
            } else {
                false
            }

            Workout(
                day = day,
                name = routine.name,
                difficulty = difficulty,
                difficultyColor = difficultyColor,
                duration = (exercisesCount * 12) + 15,
                exercises = exercisesCount,
                isLocked = false, // El usuario pidió que no se bloqueen
                isCurrent = isCurrent,
                isDone = isDone,
                isSkipped = isSkipped,
                weekNumber = weekNumber
            )
        }

        Week(
            number = weekNumber,
            workouts = workouts,
            isExpanded = workouts.any { it.isCurrent } || (weekNumber == 1 && workouts.isNotEmpty()),
            hasEmoji = workouts.any { it.isCurrent }
        )
    }
}
@Composable
fun EmptyHomeRoutineCard() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(JaguarSurface, RoundedCornerShape(12.dp))
            .border(1.dp, JaguarBorder, RoundedCornerShape(12.dp))
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "No hay rutinas creadas",
                color = JaguarWhite,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Presioná “Nueva Rutina” para crear tu primer entrenamiento.",
                color = JaguarGray,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}
