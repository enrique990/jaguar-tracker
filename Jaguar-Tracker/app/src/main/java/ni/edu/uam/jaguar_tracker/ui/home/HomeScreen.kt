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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ni.edu.uam.jaguar_tracker.R
import ni.edu.uam.jaguar_tracker.ui.theme.*
import androidx.lifecycle.viewmodel.compose.viewModel
import ni.edu.uam.jaguar_tracker.data.model.RoutineModel



data class Workout(
    val day: String,
    val name: String,
    val difficulty: String,
    val difficultyColor: Color,
    val duration: Int,
    val exercises: Int,
    val isLocked: Boolean = false,
    val isCurrent: Boolean = false
)

data class Week(
    val number: Int,
    val workouts: List<Workout>,
    val isExpanded: Boolean = false,
    val hasEmoji: Boolean = false
)

@Composable
fun HomeScreen(
    onNewRoutineClick: () -> Unit = {},
    onStartWorkoutClick: () -> Unit = {},
    onProfileClick: () -> Unit = {},
    homeViewModel: HomeViewModel = viewModel(),
    modifier: Modifier = Modifier
) {
    val routines by homeViewModel.routines.collectAsState()
    val selectedRoutine = routines.firstOrNull { it.isSelected }
    val weeks = buildWeeksFromSelectedRoutine(selectedRoutine)


    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = JaguarBlack,
        bottomBar = { JaguarBottomNavigation(onProfileClick = onProfileClick) }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item { Spacer(modifier = Modifier.height(8.dp)) }

            // Header
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = stringResource(R.string.mesocycle_title),
                            style = MaterialTheme.typography.titleLarge,
                            color = JaguarWhite
                        )
                        Text(
                            text = stringResource(R.string.mesocycle_subtitle),
                            style = MaterialTheme.typography.bodyMedium,
                            color = JaguarGreen
                        )
                    }
                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = stringResource(R.string.progress_percentage),
                            style = MaterialTheme.typography.headlineMedium,
                            color = JaguarWhite
                        )
                        Text(
                            text = stringResource(R.string.completed_label),
                            style = MaterialTheme.typography.labelSmall,
                            color = JaguarGray
                        )
                    }
                }
            }

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
                            }
                        )
                    }
                    }
                }
            }

            // Nueva Rutina Button
            item {
                Button(
                    onClick = onNewRoutineClick,
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
                        onStartWorkoutClick = onStartWorkoutClick
                    )
                }
            }

            // Agregar Semana Button
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .border(
                            width = 1.dp,
                            color = JaguarBorder,
                            shape = RoundedCornerShape(8.dp)
                        )
                        .clickable { /* TODO */ },
                    contentAlignment = Alignment.Center
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Add, contentDescription = null, tint = JaguarWhite, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = stringResource(R.string.add_week_button),
                            color = JaguarWhite,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }

            item { Spacer(modifier = Modifier.height(16.dp)) }
        }
    }
}

@Composable
fun RoutineCard(
    routine: RoutineModel,
    onClick: () -> Unit = {}
) {
    Card(
        modifier = Modifier
            .width(160.dp)
            .height(72.dp)
            .clickable { onClick() }
            .border(
                width = 1.dp,
                color = if (routine.isSelected) JaguarGreen else JaguarBorder,
                shape = RoundedCornerShape(12.dp)
            ),
        colors = CardDefaults.cardColors(containerColor = JaguarCard),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "${routine.id}: ${routine.name}${if (routine.hasEmoji) " 💪" else ""}",
                color = if (routine.isSelected) JaguarGreen else JaguarWhite,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.bodyMedium
            )

            Text(
                text = "${routine.weeks} semanas",
                color = JaguarGray,
                style = MaterialTheme.typography.labelSmall
            )
        }
    }
}

@Composable
fun WeekAccordion(
    week: Week,
    onStartWorkoutClick: () -> Unit = {}
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
                        onStartWorkoutClick = onStartWorkoutClick
                    )
                }
            }
        }
    }
}

@Composable
fun WorkoutCard(
    workout: Workout,
    onStartWorkoutClick: () -> Unit = {}
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = if (workout.isCurrent) JaguarGreen else JaguarBorder,
                shape = RoundedCornerShape(12.dp)
            ),
        colors = CardDefaults.cardColors(containerColor = JaguarCard),
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
                        color = if (workout.isCurrent) JaguarTeal else JaguarGray,
                        style = MaterialTheme.typography.labelLarge
                    )
                }
                Text(
                    text = workout.name,
                    color = JaguarWhite,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(6.dp)
                                .background(workout.difficultyColor, CircleShape)
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

            if (workout.isLocked) {
                Icon(Icons.Default.Lock, contentDescription = null, tint = JaguarGray, modifier = Modifier.size(24.dp))
            } else {
                IconButton(
                    onClick = onStartWorkoutClick,
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

@Composable
fun JaguarBottomNavigation(
    onProfileClick: () -> Unit = {},
    onHomeClick: () -> Unit = {}
) {
    NavigationBar(
        containerColor = JaguarBlack,
        tonalElevation = 0.dp,
        modifier = Modifier.height(80.dp)
    ) {
        val items = listOf(
            Triple(stringResource(R.string.nav_inicio), Icons.Default.Home, true),
            Triple(stringResource(R.string.nav_historial), Icons.Default.History, false),
            Triple(stringResource(R.string.nav_ranking), Icons.Default.EmojiEvents, false),
            Triple(stringResource(R.string.nav_perfil), Icons.Default.Person, false)
        )

        items.forEachIndexed { index, (label, icon, selected) ->
            NavigationBarItem(
                selected = selected,
                onClick = { 
                    if (index == 0) onHomeClick()
                    if (index == 3) onProfileClick()
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

    val defaultWorkouts = listOf(
        Workout("Lunes", "Pecho y Tríceps", "Medio", Color(0xFFFFA500), 65, 8, isCurrent = true),
        Workout("Martes", "Espalda y Bíceps", "Alto", Color(0xFFFF4500), 70, 9, isLocked = true),
        Workout("Jueves", "Piernas", "Alto", Color(0xFFFF4500), 75, 7, isLocked = true),
        Workout("Viernes", "Hombros y Abdomen", "Bajo", Color(0xFF32CD32), 50, 6, isLocked = true)
    )

    val workoutsFromRoutine =
        if (routine.exercises.isEmpty()) {
            defaultWorkouts
        } else {
            val days = listOf("Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado")

            routine.exercises.mapIndexed { index, exercise ->
                Workout(
                    day = days[index % days.size],
                    name = exercise.name,
                    difficulty = "Medio",
                    difficultyColor = Color(0xFFFFA500),
                    duration = 45,
                    exercises = 1,
                    isLocked = index != 0,
                    isCurrent = index == 0
                )
            }
        }

    return (1..routine.weeks).map { weekNumber ->
        Week(
            number = weekNumber,
            workouts = if (weekNumber == 1) workoutsFromRoutine else emptyList(),
            isExpanded = weekNumber == 1,
            hasEmoji = routine.isSelected && weekNumber == 1
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