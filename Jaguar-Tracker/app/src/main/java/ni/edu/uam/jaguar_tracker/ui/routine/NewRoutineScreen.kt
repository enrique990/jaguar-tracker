package ni.edu.uam.jaguar_tracker.ui.routine

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import ni.edu.uam.jaguar_tracker.R
import ni.edu.uam.jaguar_tracker.ui.theme.JaguarTeal
import ni.edu.uam.jaguar_tracker.ui.theme.JaguarTrackerTheme

@Composable
fun NewRoutineScreen(
    viewModel: NewRoutineViewModel = viewModel(),
    onBack: () -> Unit = {}
) {
    val state by viewModel.uiState.collectAsState()

    LaunchedEffect(state.wasSaved) {
        if (state.wasSaved) {
            onBack()
        }
    }

    NewRoutineContent(
        state = state,
        onBack = onBack,
        onRoutineNameChange = viewModel::updateRoutineName,
        onCreateRoutine = viewModel::saveRoutine,
        onOpenExerciseSheet = { viewModel.toggleSheetVisibility(true) },
        onCloseExerciseSheet = { viewModel.toggleSheetVisibility(false) },
        onExerciseSelected = viewModel::toggleExerciseSelection,
        onRemoveExercise = viewModel::removeExercise
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewRoutineContent(
    state: NewRoutineUiState,
    onBack: () -> Unit,
    onRoutineNameChange: (String) -> Unit,
    onCreateRoutine: () -> Unit,
    onOpenExerciseSheet: () -> Unit,
    onCloseExerciseSheet: () -> Unit,
    onExerciseSelected: (Int) -> Unit,
    onRemoveExercise: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        bottomBar = {
            Button(
                onClick = onCreateRoutine,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = JaguarTeal,
                    contentColor = Color.Black
                )
            ) {
                Text(
                    text = stringResource(R.string.create_routine_button),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }
        },
        modifier = modifier
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 16.dp)
            ) {
                Surface(
                    onClick = onBack,
                    shape = RoundedCornerShape(8.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    modifier = Modifier.height(32.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp),
                            tint = Color.White
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = stringResource(R.string.back_button),
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.White
                        )
                    }
                }

                Spacer(modifier = Modifier.width(12.dp))

                Text(
                    text = stringResource(R.string.routine_title),
                    style = MaterialTheme.typography.headlineMedium,
                    color = Color.White
                )
            }

            Text(
                text = stringResource(R.string.plan_mesocycle_subtitle),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(24.dp))

            SectionLabel(stringResource(R.string.routine_name_label))

            RoutineTextField(
                value = state.routineName,
                onValueChange = onRoutineNameChange,
                placeholder = stringResource(R.string.routine_name_placeholder)
            )

            if (state.error != null) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = state.error,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            SectionLabel(stringResource(R.string.training_days_label))
            DaySelector()

            Spacer(modifier = Modifier.height(8.dp))

            NumericInput(value = 3)

            Spacer(modifier = Modifier.height(24.dp))

            SectionLabel(stringResource(R.string.day_name_label))

            RoutineTextField(
                value = "",
                onValueChange = {},
                placeholder = stringResource(R.string.day_name_placeholder)
            )

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.exercises_label),
                    style = MaterialTheme.typography.titleLarge,
                    color = Color.White
                )

                Text(
                    text = stringResource(R.string.add_exercise),
                    style = MaterialTheme.typography.labelLarge,
                    color = JaguarTeal,
                    modifier = Modifier.clickable {
                        onOpenExerciseSheet()
                    }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (state.selectedExercises.isEmpty()) {
                EmptyExercisesCard()
            } else {
                state.selectedExercises.forEach { exercise ->
                    SelectedExerciseCard(
                        exercise = exercise,
                        onRemoveExercise = {
                            onRemoveExercise(exercise.id)
                        }
                    )

                    Spacer(modifier = Modifier.height(12.dp))
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            LabeledSwitch(
                label = stringResource(R.string.advanced_options_label),
                checked = true,
                onCheckedChange = {}
            )

            Spacer(modifier = Modifier.height(16.dp))

            LabeledSwitch(
                label = stringResource(R.string.plan_mesocycle_switch_label),
                subtitle = stringResource(R.string.plan_mesocycle_switch_subtitle),
                checked = true,
                onCheckedChange = {}
            )

            Spacer(modifier = Modifier.height(24.dp))

            SectionLabel(stringResource(R.string.microcycles_number_label))

            NumericInputWithButtons(value = 4)

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = stringResource(R.string.weekly_planning_label),
                style = MaterialTheme.typography.titleMedium,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(16.dp))

            repeat(4) { index ->
                WeekPlanningCard(weekNumber = index + 1)
                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }

    if (state.isSheetOpen) {
        ModalBottomSheet(
            onDismissRequest = onCloseExerciseSheet,
            sheetState = sheetState,
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = Color.White
        ) {
            ExerciseSelectorSheet(
                exercises = state.availableExercises,
                onExerciseSelected = onExerciseSelected,
                onClose = onCloseExerciseSheet
            )
        }
    }
}

@Composable
fun ExerciseSelectorSheet(
    exercises: List<Exercise>,
    onExerciseSelected: (Int) -> Unit,
    onClose: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(max = 520.dp)
            .padding(16.dp)
    ) {
        Text(
            text = "Seleccionar ejercicios",
            style = MaterialTheme.typography.titleLarge,
            color = Color.White,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = "Elegí los ejercicios que tendrá esta rutina.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(top = 4.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.weight(1f, fill = false)
        ) {
            items(exercises) { exercise ->
                ExerciseSelectorItem(
                    exercise = exercise,
                    onClick = {
                        onExerciseSelected(exercise.id)
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onClose,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = JaguarTeal,
                contentColor = Color.Black
            )
        ) {
            Text(
                text = "Listo",
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun ExerciseSelectorItem(
    exercise: Exercise,
    onClick: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.surfaceVariant,
        border = BorderStroke(
            width = 1.dp,
            color = if (exercise.isSelected) JaguarTeal else MaterialTheme.colorScheme.outline
        ),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(26.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(
                        if (exercise.isSelected) JaguarTeal else Color.Black
                    )
                    .border(
                        width = 1.dp,
                        color = if (exercise.isSelected) JaguarTeal else MaterialTheme.colorScheme.outline,
                        shape = RoundedCornerShape(8.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                if (exercise.isSelected) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = null,
                        tint = Color.Black,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column {
                Text(
                    text = exercise.name,
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "${exercise.sets} series x ${exercise.reps} reps",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
fun EmptyExercisesCard() {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.surfaceVariant,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = null,
                tint = JaguarTeal,
                modifier = Modifier.size(32.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Todavía no agregaste ejercicios",
                color = Color.White,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "Presioná “Agregar ejercicio” para seleccionar ejercicios disponibles.",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

@Composable
fun SelectedExerciseCard(
    exercise: Exercise,
    onRemoveExercise: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.surfaceVariant,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = exercise.name,
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )

                IconButton(onClick = onRemoveExercise) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(modifier = Modifier.fillMaxWidth()) {
                ExerciseField(
                    label = stringResource(R.string.sets_label),
                    value = exercise.sets.toString(),
                    modifier = Modifier.weight(1f)
                )

                Spacer(modifier = Modifier.width(12.dp))

                ExerciseField(
                    label = stringResource(R.string.reps_label),
                    value = exercise.reps.toString(),
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(modifier = Modifier.fillMaxWidth()) {
                ExerciseField(
                    label = stringResource(R.string.rir_rpe_label),
                    value = "2-3",
                    modifier = Modifier.weight(1f)
                )

                Spacer(modifier = Modifier.width(12.dp))

                ExerciseField(
                    label = stringResource(R.string.rest_label),
                    value = "90",
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
fun SectionLabel(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.labelLarge,
        color = Color.White,
        modifier = Modifier.padding(bottom = 8.dp)
    )
}

@Composable
fun RoutineTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = {
            Text(
                text = placeholder,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
            )
        },
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
            unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
            focusedBorderColor = MaterialTheme.colorScheme.outline,
            unfocusedBorderColor = MaterialTheme.colorScheme.outline,
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White
        ),
        singleLine = true
    )
}

@Composable
fun DaySelector() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(4.dp)
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .height(40.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(JaguarTeal),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = stringResource(R.string.any_day),
                style = MaterialTheme.typography.labelLarge,
                color = Color.Black
            )
        }

        Box(
            modifier = Modifier
                .weight(1f)
                .height(40.dp)
                .clip(RoundedCornerShape(8.dp)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = stringResource(R.string.custom_day),
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun NumericInput(value: Int) {
    OutlinedTextField(
        value = value.toString(),
        onValueChange = {},
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
            unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
            focusedBorderColor = MaterialTheme.colorScheme.outline,
            unfocusedBorderColor = MaterialTheme.colorScheme.outline,
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White
        ),
        singleLine = true
    )
}

@Composable
fun ExerciseField(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(bottom = 4.dp)
        )

        OutlinedTextField(
            value = value,
            onValueChange = {},
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Color.Black,
                unfocusedContainerColor = Color.Black,
                focusedBorderColor = MaterialTheme.colorScheme.outline,
                unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White
            ),
            singleLine = true
        )
    }
}

@Composable
fun LabeledSwitch(
    label: String,
    subtitle: String? = null,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.surfaceVariant,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = label,
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.White
                )

                if (subtitle != null) {
                    Text(
                        text = subtitle,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Switch(
                checked = checked,
                onCheckedChange = onCheckedChange,
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Color.White,
                    checkedTrackColor = JaguarTeal,
                    uncheckedThumbColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    uncheckedTrackColor = Color.Black
                )
            )
        }
    }
}

@Composable
fun NumericInputWithButtons(value: Int) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Surface(
            shape = RoundedCornerShape(8.dp),
            color = MaterialTheme.colorScheme.surfaceVariant,
            modifier = Modifier.size(48.dp),
            onClick = {}
        ) {
            Box(contentAlignment = Alignment.Center) {
                Text("-", color = Color.White, fontSize = 24.sp)
            }
        }

        Spacer(modifier = Modifier.width(8.dp))

        Surface(
            shape = RoundedCornerShape(12.dp),
            color = Color.Black,
            modifier = Modifier
                .weight(1f)
                .height(48.dp)
                .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(12.dp))
        ) {
            Box(contentAlignment = Alignment.Center) {
                Text(
                    text = value.toString(),
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.width(8.dp))

        Surface(
            shape = RoundedCornerShape(8.dp),
            color = MaterialTheme.colorScheme.surfaceVariant,
            modifier = Modifier.size(48.dp),
            onClick = {}
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.padding(12.dp)
            )
        }
    }
}

@Composable
fun WeekPlanningCard(weekNumber: Int) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.surfaceVariant,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.week_number, weekNumber),
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = stringResource(R.string.config_button),
                    style = MaterialTheme.typography.labelLarge,
                    color = JaguarTeal,
                    modifier = Modifier.clickable { }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(modifier = Modifier.fillMaxWidth()) {
                DropdownField(
                    label = stringResource(R.string.intensity_label),
                    value = stringResource(R.string.low_intensity),
                    modifier = Modifier.weight(1f)
                )

                Spacer(modifier = Modifier.width(12.dp))

                DropdownField(
                    label = stringResource(R.string.volume_label),
                    value = stringResource(R.string.normal_volume),
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
fun DropdownField(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(bottom = 4.dp)
        )

        Surface(
            shape = RoundedCornerShape(8.dp),
            color = Color.Black,
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
            modifier = Modifier
                .fillMaxWidth()
                .height(40.dp)
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = value,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.White
                )

                Icon(
                    imageVector = Icons.Default.KeyboardArrowDown,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun NewRoutineScreenPreview() {
    JaguarTrackerTheme {
        NewRoutineContent(
            state = NewRoutineUiState(
                routineName = "Mesociclo Fuerza",
                selectedExercises = listOf(
                    Exercise(1, "Press de banca", sets = 4, reps = 10),
                    Exercise(2, "Press inclinado", sets = 3, reps = 12)
                ),
                availableExercises = listOf(
                    Exercise(1, "Press de banca", sets = 4, reps = 10, isSelected = true),
                    Exercise(2, "Press inclinado", sets = 3, reps = 12, isSelected = true),
                    Exercise(3, "Sentadillas", sets = 4, reps = 8)
                )
            ),
            onBack = {},
            onRoutineNameChange = {},
            onCreateRoutine = {},
            onOpenExerciseSheet = {},
            onCloseExerciseSheet = {},
            onExerciseSelected = {},
            onRemoveExercise = {}
        )
    }
}