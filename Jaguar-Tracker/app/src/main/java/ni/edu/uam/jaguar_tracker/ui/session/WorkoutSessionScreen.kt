package ni.edu.uam.jaguar_tracker.ui.session

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ni.edu.uam.jaguar_tracker.R
import ni.edu.uam.jaguar_tracker.ui.theme.*

data class SetSession(
    val number: Int,
    val previousWeight: Int,
    val weight: String = "",
    val reps: String = "",
    val rir: String = "",
)

data class ExerciseSession(
    val name: String,
    val sets: List<SetSession>,
    val restSeconds: Int = 90
)

@Composable
fun WorkoutSessionScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {}
) {
    var isKg by remember { mutableStateOf(value = true) }
    
    val exercises = remember {
        listOf(
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

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = JaguarBlack,
        bottomBar = {
            Button(
                onClick = { /* TODO */ },
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
                    isKg = isKg,
                    onUnitToggle = { isKg = it }
                )
            }

            items(exercises) { exercise ->
                ExerciseSessionCard(exercise = exercise)
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
fun WorkoutSessionHeader(
    onBackClick: () -> Unit,
    isKg: Boolean,
    onUnitToggle: (Boolean) -> Unit
) {
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
                    .clickable { onBackClick() }
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
            text = stringResource(R.string.workout_title_format, "Pecho"),
            style = MaterialTheme.typography.displayLarge.copy(
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = (-1).sp
            ),
            color = JaguarWhite
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(top = 4.dp)
        ) {
            IconPlaceholder(modifier = Modifier.size(16.dp), color = JaguarGray)
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Lunes, 20 de Abril 2026",
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
fun ExerciseSessionCard(exercise: ExerciseSession) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = JaguarCard),
        shape = RoundedCornerShape(12.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, JaguarBorder)
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
                IconPlaceholder(modifier = Modifier.size(20.dp), color = JaguarGray)
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Table Header
            Row(modifier = Modifier.fillMaxWidth()) {
                TableHeaderItem(text = stringResource(R.string.header_serie), modifier = Modifier.weight(1f))
                TableHeaderItem(text = stringResource(R.string.header_peso), modifier = Modifier.weight(2f))
                TableHeaderItem(text = stringResource(R.string.header_reps), modifier = Modifier.weight(1.5f))
                TableHeaderItem(text = stringResource(R.string.header_rir), modifier = Modifier.weight(1.5f))
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Sets
            exercise.sets.forEach { set ->
                SetRow(set = set)
                Spacer(modifier = Modifier.height(8.dp))
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Visual Weight Selector
            VisualWeightSelector(weight = 100)

            Spacer(modifier = Modifier.height(16.dp))

            // Rest Button
            Button(
                onClick = { /* TODO */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = JaguarSurface),
                border = androidx.compose.foundation.BorderStroke(1.dp, JaguarBorder)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconPlaceholder(modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = stringResource(R.string.start_rest_button, exercise.restSeconds),
                        color = JaguarWhite,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
fun TableHeaderItem(text: String, modifier: Modifier = Modifier) {
    Text(
        text = text,
        modifier = modifier,
        textAlign = TextAlign.Center,
        style = MaterialTheme.typography.labelSmall,
        color = JaguarGray
    )
}

@Composable
fun SetRow(set: SetSession) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = set.number.toString(),
            modifier = Modifier.weight(1f),
            textAlign = TextAlign.Center,
            color = JaguarWhite,
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.titleMedium
        )

        SetInputField(
            value = "Ayer: ${set.previousWeight}",
            modifier = Modifier.weight(2f),
            isPlaceholder = true
        )

        Spacer(modifier = Modifier.width(8.dp))

        SetInputField(
            value = set.reps,
            modifier = Modifier.weight(1.5f)
        )

        Spacer(modifier = Modifier.width(8.dp))

        SetInputField(
            value = set.rir,
            modifier = Modifier.weight(1.5f)
        )
    }
}

@Composable
fun SetInputField(
    value: String,
    modifier: Modifier = Modifier,
    isPlaceholder: Boolean = false
) {
    Box(
        modifier = modifier
            .height(44.dp)
            .background(JaguarSurface, RoundedCornerShape(8.dp))
            .border(1.dp, JaguarBorder, RoundedCornerShape(8.dp)),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = value,
            color = if (isPlaceholder) JaguarGray else JaguarWhite,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
fun VisualWeightSelector(weight: Int) {
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

        // Barbell Drawing
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            // Bar end left
            Box(modifier = Modifier.width(16.dp).height(4.dp).background(JaguarGray, CircleShape))
            
            // Plates left
            WeightPlate(color = JaguarRed, height = 48.dp) // Red
            Spacer(modifier = Modifier.width(2.dp))
            WeightPlate(color = JaguarRed, height = 48.dp)
            Spacer(modifier = Modifier.width(2.dp))
            WeightPlate(color = Color(0xFF4285F4), height = 36.dp) // Blue
            Spacer(modifier = Modifier.width(2.dp))
            WeightPlate(color = Color(0xFF4285F4), height = 36.dp)
            
            // Bar middle
            Box(modifier = Modifier.width(70.dp).height(4.dp).background(JaguarGray))
            
            // Plates right
            WeightPlate(color = Color(0xFF4285F4), height = 36.dp)
            Spacer(modifier = Modifier.width(2.dp))
            WeightPlate(color = Color(0xFF4285F4), height = 36.dp)
            Spacer(modifier = Modifier.width(2.dp))
            WeightPlate(color = JaguarRed, height = 48.dp)
            Spacer(modifier = Modifier.width(2.dp))
            WeightPlate(color = JaguarRed, height = 48.dp)
            
            // Bar end right
            Box(modifier = Modifier.width(16.dp).height(4.dp).background(JaguarGray, CircleShape))
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = stringResource(R.string.weight_display_format, weight),
            color = JaguarWhite,
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.titleLarge
        )
    }
}

@Composable
fun WeightPlate(color: Color, height: androidx.compose.ui.unit.Dp) {
    Box(
        modifier = Modifier
            .width(14.dp)
            .height(height)
            .background(color, RoundedCornerShape(2.dp))
    )
}

@Composable
fun IconPlaceholder(modifier: Modifier = Modifier, color: Color = JaguarWhite) {
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

@Preview(showBackground = true, backgroundColor = 0xFF0A0A0A)
@Composable
fun WorkoutSessionScreenPreview() {
    JaguarTrackerTheme {
        WorkoutSessionScreen()
    }
}
