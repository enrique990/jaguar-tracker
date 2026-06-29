package ni.edu.uam.jaguar_tracker.ui.session

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import ni.edu.uam.jaguar_tracker.R
import ni.edu.uam.jaguar_tracker.ui.components.rememberSafeClick
import ni.edu.uam.jaguar_tracker.ui.theme.*

@Composable
fun WorkoutSessionScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {},
    sessionViewModel: WorkoutSessionViewModel = viewModel()
) {
    val state by sessionViewModel.uiState.collectAsState()

    val safeComplete = rememberSafeClick {
        sessionViewModel.completeWorkout()
    }

    Box(modifier = modifier.fillMaxSize()) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            containerColor = JaguarBlack,
            bottomBar = {
                Button(
                    onClick = safeComplete,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .height(56.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = JaguarTeal)
                ) {
                    Text(
                        text = stringResource(R.string.complete_workout_button),
                        color = JaguarBlack,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }
        ) { paddingValues ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    Spacer(modifier = Modifier.height(16.dp))

                    WorkoutSessionHeader(
                        onBackClick = onBackClick,
                        isKg = state.isKg,
                        onUnitToggle = sessionViewModel::toggleUnit,
                        routineName = state.routineName,
                        dateLabel = state.dateLabel,
                        weekNumber = state.weekNumber,
                        day = state.day
                    )
                }

                items(state.exercises) { exercise ->
                    val isResting = state.activeTimerExerciseId == exercise.exerciseId
                    ExerciseSessionCard(
                        exercise = exercise,
                        isKg = state.isKg,
                        onWeightChange = sessionViewModel::updateWeight,
                        onRepsChange = sessionViewModel::updateReps,
                        onRirChange = sessionViewModel::updateRir,
                        onStartRest = { sessionViewModel.startTimer(exercise.restSeconds, exercise.exerciseId) },
                        isResting = isResting,
                        timerSeconds = if (isResting) state.timerRemainingSeconds else null
                    )
                }

                item {
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }

        state.timerRemainingSeconds?.let { seconds ->
            Box(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 100.dp)
            ) {
                RestTimerOverlay(
                    seconds = seconds,
                    onClose = { sessionViewModel.stopTimer() }
                )
            }
        }
    }

    if (state.error != null) {
        AlertDialog(
            onDismissRequest = sessionViewModel::dismissMessage,
            title = {
                Text(text = "Revisá los datos")
            },
            text = {
                Text(text = state.error ?: "")
            },
            confirmButton = {
                TextButton(onClick = sessionViewModel::dismissMessage) {
                    Text(text = "Entendido")
                }
            },
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
            titleContentColor = JaguarWhite,
            textContentColor = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }

    if (state.successMessage != null) {
        AlertDialog(
            onDismissRequest = sessionViewModel::dismissMessage,
            title = {
                Text(text = "Entrenamiento completado")
            },
            text = {
                Text(text = state.successMessage ?: "")
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        sessionViewModel.dismissMessage()
                        onBackClick()
                    }
                ) {
                    Text(text = "Volver")
                }
            },
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
            titleContentColor = JaguarWhite,
            textContentColor = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
fun WorkoutSessionHeader(
    onBackClick: () -> Unit,
    isKg: Boolean,
    onUnitToggle: (Boolean) -> Unit,
    routineName: String,
    dateLabel: String,
    weekNumber: Int? = null,
    day: String? = null
) {
    val safeBackClick = rememberSafeClick {
        onBackClick()
    }

    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .background(JaguarSurface, RoundedCornerShape(8.dp))
                    .border(1.dp, JaguarBorder, RoundedCornerShape(8.dp))
                    .clickable { safeBackClick() }
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Text(
                    text = "←",
                    color = JaguarWhite,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.width(4.dp))

                Text(
                    text = stringResource(R.string.back_text),
                    color = JaguarWhite,
                    style = MaterialTheme.typography.labelLarge
                )
            }

            Row(
                modifier = Modifier
                    .background(JaguarSurface, RoundedCornerShape(8.dp))
                    .padding(4.dp)
            ) {
                UnitToggleItem(
                    text = stringResource(R.string.unit_kg),
                    isSelected = isKg,
                    onClick = { onUnitToggle(true) }
                )

                UnitToggleItem(
                    text = stringResource(R.string.unit_lbs),
                    isSelected = !isKg,
                    onClick = { onUnitToggle(false) }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = if (weekNumber != null && day != null) {
                "$routineName - Sem $weekNumber ($day)"
            } else {
                stringResource(R.string.workout_title_format, routineName)
            },
            style = MaterialTheme.typography.displayLarge.copy(
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = (-1).sp,
                fontSize = if (routineName.length > 15) 32.sp else 40.sp
            ),
            color = JaguarWhite
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(top = 4.dp)
        ) {
            IconPlaceholder(
                modifier = Modifier.size(16.dp),
                color = JaguarGray
            )

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = dateLabel,
                style = MaterialTheme.typography.bodyMedium,
                color = JaguarGray
            )
        }
    }
}

@Composable
fun UnitToggleItem(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .background(
                if (isSelected) JaguarTeal else Color.Transparent,
                RoundedCornerShape(6.dp)
            )
            .clickable { onClick() }
            .padding(horizontal = 12.dp, vertical = 4.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = if (isSelected) JaguarBlack else JaguarGray,
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.labelSmall
        )
    }
}

@Composable
fun ExerciseSessionCard(
    exercise: ExerciseSession,
    isKg: Boolean,
    onWeightChange: (Int, Int, String) -> Unit,
    onRepsChange: (Int, Int, String) -> Unit,
    onRirChange: (Int, Int, String) -> Unit,
    onStartRest: () -> Unit,
    isResting: Boolean = false,
    timerSeconds: Int? = null
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = JaguarCard),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, JaguarBorder)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = exercise.name,
                    style = MaterialTheme.typography.titleLarge,
                    color = JaguarWhite,
                    fontWeight = FontWeight.Bold
                )

                IconPlaceholder(
                    modifier = Modifier.size(20.dp),
                    color = JaguarGray
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(modifier = Modifier.fillMaxWidth()) {
                TableHeaderItem(
                    text = stringResource(R.string.header_serie),
                    modifier = Modifier.weight(0.8f)
                )

                TableHeaderItem(
                    text = stringResource(R.string.header_peso),
                    modifier = Modifier.weight(2f)
                )

                TableHeaderItem(
                    text = stringResource(R.string.header_reps),
                    modifier = Modifier.weight(1.3f)
                )

                TableHeaderItem(
                    text = stringResource(R.string.header_rir),
                    modifier = Modifier.weight(1.3f)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            exercise.sets.forEach { set ->
                SetRow(
                    exerciseId = exercise.exerciseId,
                    set = set,
                    isKg = isKg,
                    onWeightChange = onWeightChange,
                    onRepsChange = onRepsChange,
                    onRirChange = onRirChange
                )

                Spacer(modifier = Modifier.height(8.dp))
            }

            Spacer(modifier = Modifier.height(16.dp))

            VisualWeightSelector(
                weightText = exercise.sets.firstOrNull()?.weight.orEmpty(),
                isKg = isKg
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = onStartRest,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isResting) JaguarTeal else JaguarSurface
                ),
                border = BorderStroke(1.dp, if (isResting) JaguarTeal else JaguarBorder)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconPlaceholder(
                        modifier = Modifier.size(18.dp),
                        color = if (isResting) JaguarBlack else JaguarWhite
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = if (isResting && timerSeconds != null) {
                            "Descansando: ${formatTimer(timerSeconds)}"
                        } else {
                            stringResource(R.string.start_rest_button, exercise.restSeconds)
                        },
                        color = if (isResting) JaguarBlack else JaguarWhite,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
fun TableHeaderItem(
    text: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = text,
        modifier = modifier,
        textAlign = TextAlign.Center,
        style = MaterialTheme.typography.labelSmall,
        color = JaguarGray
    )
}

@Composable
fun SetRow(
    exerciseId: Int,
    set: SetSession,
    isKg: Boolean,
    onWeightChange: (Int, Int, String) -> Unit,
    onRepsChange: (Int, Int, String) -> Unit,
    onRirChange: (Int, Int, String) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = set.number.toString(),
            modifier = Modifier.weight(0.8f),
            textAlign = TextAlign.Center,
            color = JaguarWhite,
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.titleMedium
        )

        SetInputField(
            value = set.weight,
            placeholder = if (set.previousWeight > 0) {
                "Ayer: ${formatPreviousWeight(set.previousWeight)} ${if (isKg) "kg" else "lbs"}"
            } else {
                if (isKg) "kg" else "lbs"
            },
            onValueChange = { value ->
                onWeightChange(exerciseId, set.number, value)
            },
            modifier = Modifier.weight(2f),
            keyboardType = KeyboardType.Decimal
        )

        Spacer(modifier = Modifier.width(8.dp))

        SetInputField(
            value = set.reps,
            placeholder = "0",
            onValueChange = { value ->
                onRepsChange(exerciseId, set.number, value)
            },
            modifier = Modifier.weight(1.3f),
            keyboardType = KeyboardType.Number
        )

        Spacer(modifier = Modifier.width(8.dp))

        SetInputField(
            value = set.rir,
            placeholder = "0",
            onValueChange = { value ->
                onRirChange(exerciseId, set.number, value)
            },
            modifier = Modifier.weight(1.3f),
            keyboardType = KeyboardType.Number
        )
    }
}

@Composable
fun SetInputField(
    value: String,
    placeholder: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    keyboardType: KeyboardType = KeyboardType.Number
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = {
            Text(
                text = placeholder,
                color = JaguarGray,
                style = MaterialTheme.typography.labelSmall,
                textAlign = TextAlign.Center
            )
        },
        modifier = modifier.height(52.dp),
        shape = RoundedCornerShape(8.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = JaguarSurface,
            unfocusedContainerColor = JaguarSurface,
            focusedBorderColor = JaguarBorder,
            unfocusedBorderColor = JaguarBorder,
            focusedTextColor = JaguarWhite,
            unfocusedTextColor = JaguarWhite
        ),
        textStyle = MaterialTheme.typography.bodySmall.copy(
            color = JaguarWhite,
            textAlign = TextAlign.Center
        ),
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        singleLine = true
    )
}

@Composable
fun VisualWeightSelector(
    weightText: String,
    isKg: Boolean
) {
    val unit = if (isKg) "kg" else "lbs"
    val displayWeight = weightText.ifBlank { "0" }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(JaguarSurface, RoundedCornerShape(12.dp))
            .padding(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.weight_selector_title),
            style = MaterialTheme.typography.labelSmall,
            color = JaguarGray,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .width(16.dp)
                    .height(4.dp)
                    .background(JaguarGray, CircleShape)
            )

            WeightPlate(color = JaguarRed, height = 48.dp)
            Spacer(modifier = Modifier.width(2.dp))
            WeightPlate(color = JaguarRed, height = 48.dp)
            Spacer(modifier = Modifier.width(2.dp))
            WeightPlate(color = Color(0xFF4285F4), height = 36.dp)
            Spacer(modifier = Modifier.width(2.dp))
            WeightPlate(color = Color(0xFF4285F4), height = 36.dp)

            Box(
                modifier = Modifier
                    .width(70.dp)
                    .height(4.dp)
                    .background(JaguarGray)
            )

            WeightPlate(color = Color(0xFF4285F4), height = 36.dp)
            Spacer(modifier = Modifier.width(2.dp))
            WeightPlate(color = Color(0xFF4285F4), height = 36.dp)
            Spacer(modifier = Modifier.width(2.dp))
            WeightPlate(color = JaguarRed, height = 48.dp)
            Spacer(modifier = Modifier.width(2.dp))
            WeightPlate(color = JaguarRed, height = 48.dp)

            Box(
                modifier = Modifier
                    .width(16.dp)
                    .height(4.dp)
                    .background(JaguarGray, CircleShape)
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "$displayWeight $unit",
            color = JaguarWhite,
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.titleLarge
        )
    }
}

@Composable
fun WeightPlate(
    color: Color,
    height: androidx.compose.ui.unit.Dp
) {
    Box(
        modifier = Modifier
            .width(14.dp)
            .height(height)
            .background(color, RoundedCornerShape(2.dp))
    )
}

@Composable
fun IconPlaceholder(
    modifier: Modifier = Modifier,
    color: Color = JaguarWhite
) {
    Box(
        modifier = modifier.background(color.copy(alpha = 0.1f), CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(6.dp)
                .background(color, CircleShape)
        )
    }
}

fun formatPreviousWeight(value: Double): String {
    return if (value % 1.0 == 0.0) {
        value.toInt().toString()
    } else {
        "%.1f".format(value)
    }
}

fun formatTimer(seconds: Int): String {
    val minutes = seconds / 60
    val remainingSeconds = seconds % 60
    return "%02d:%02d".format(minutes, remainingSeconds)
}

@Composable
fun RestTimerOverlay(
    seconds: Int,
    onClose: () -> Unit
) {
    Card(
        modifier = Modifier
            .padding(16.dp)
            .width(200.dp),
        colors = CardDefaults.cardColors(containerColor = JaguarTeal),
        shape = RoundedCornerShape(50.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Descanso: ${formatTimer(seconds)}",
                color = JaguarBlack,
                fontWeight = FontWeight.ExtraBold,
                style = MaterialTheme.typography.titleMedium
            )

            Text(
                text = "✕",
                modifier = Modifier
                    .clickable { onClose() }
                    .padding(4.dp),
                color = JaguarBlack,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0A0A0A)
@Composable
fun WorkoutSessionScreenPreview() {
    JaguarTrackerTheme {
        WorkoutSessionScreen()
    }
}