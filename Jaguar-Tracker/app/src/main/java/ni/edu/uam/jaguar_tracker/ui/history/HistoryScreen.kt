package ni.edu.uam.jaguar_tracker.ui.history

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.KeyboardArrowDown
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
import ni.edu.uam.jaguar_tracker.ui.home.JaguarBottomNavigation
import ni.edu.uam.jaguar_tracker.ui.theme.*
import androidx.lifecycle.viewmodel.compose.viewModel
import java.util.Locale

@Composable
fun HistoryScreen(
    modifier: Modifier = Modifier,
    onHomeClick: () -> Unit = {},
    onRankingClick: () -> Unit = {},
    onProfileClick: () -> Unit = {},
    historyViewModel: HistoryViewModel = viewModel()
) {
    val state by historyViewModel.uiState.collectAsState()
    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = JaguarBlack,
        bottomBar = {
            JaguarBottomNavigation(
                selectedTabIndex = 1,
                onHomeClick = onHomeClick,
                onHistoryClick = {}, // Ya estamos en historial
                onRankingClick = onRankingClick,
                onProfileClick = onProfileClick
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            // Header
            Column {
                Text(
                    text = stringResource(R.string.history_title),
                    style = MaterialTheme.typography.headlineMedium,
                    color = JaguarWhite,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = stringResource(R.string.history_subtitle),
                    style = MaterialTheme.typography.bodyMedium,
                    color = JaguarGray
                )
            }

            // Exercise Selector
            when {
                state.isLoading -> {
                    Text(
                        text = "Cargando historial...",
                        color = JaguarGray,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                state.error != null -> {
                    Text(
                        text = state.error ?: "",
                        color = JaguarRed,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                state.points.isEmpty() -> {
                    Text(
                        text = "Todavía no hay entrenamientos completados.",
                        color = JaguarGray,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                else -> {
                    ExerciseSelector(
                        label = stringResource(R.string.exercise_label),
                        selectedExercise = state.selectedExercise
                    )

                    TimeFilterTabs()

                    ProgressChartCard(
                        points = state.points,
                        progressKg = state.bestWeight
                    )

                    StatsRow(
                        estimatedOneRm = state.estimatedOneRm,
                        progressPercent = state.progressPercent,
                        microcycles = state.totalSessions
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun ExerciseSelector(
    label: String,
    selectedExercise: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(JaguarSurface, RoundedCornerShape(12.dp))
            .border(1.dp, JaguarBorder, RoundedCornerShape(12.dp))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = JaguarGray
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = selectedExercise,
                style = MaterialTheme.typography.titleMedium,
                color = JaguarWhite,
                fontWeight = FontWeight.Medium
            )
            Icon(
                imageVector = Icons.Default.KeyboardArrowDown,
                contentDescription = null,
                tint = JaguarGray
            )
        }
    }
}

@Composable
fun TimeFilterTabs(modifier: Modifier = Modifier) {
    var selectedIndex by remember { mutableIntStateOf(1) } // Default "3 meses"
    val options = listOf(
        stringResource(R.string.one_month),
        stringResource(R.string.three_months),
        stringResource(R.string.six_months)
    )

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        options.forEachIndexed { index, option ->
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(44.dp)
                    .background(
                        color = if (selectedIndex == index) JaguarTeal else JaguarSurface,
                        shape = RoundedCornerShape(8.dp)
                    )
                    .clickable { selectedIndex = index },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = option,
                    style = MaterialTheme.typography.titleSmall,
                    color = if (selectedIndex == index) JaguarBlack else JaguarWhite,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun ProgressChartCard(
    points: List<HistoryPoint>,
    progressKg: Double,
    modifier: Modifier = Modifier
) {
    val maxWeight = points.maxOfOrNull { it.weight } ?: 1.0

    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = JaguarSurface),
        shape = RoundedCornerShape(16.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, JaguarBorder)
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.progress_curve_title),
                    style = MaterialTheme.typography.titleLarge,
                    color = JaguarWhite,
                    fontWeight = FontWeight.Bold
                )

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.TrendingUp,
                        contentDescription = null,
                        tint = JaguarTeal,
                        modifier = Modifier.size(20.dp)
                    )

                    Spacer(modifier = Modifier.width(4.dp))

                    Text(
                        text = "${formatHistoryNumber(progressKg)} kg",
                        style = MaterialTheme.typography.titleMedium,
                        color = JaguarTeal,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = 28.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Bottom
                ) {
                    points.forEach { point ->
                        val factor = (point.weight / maxWeight).toFloat().coerceIn(0.15f, 1f)

                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                text = "${formatHistoryNumber(point.weight)}kg",
                                color = JaguarGray,
                                style = MaterialTheme.typography.labelSmall,
                                textAlign = TextAlign.Center
                            )

                            Spacer(modifier = Modifier.height(6.dp))

                            Box(
                                modifier = Modifier
                                    .fillMaxHeight(factor)
                                    .width(36.dp)
                                    .background(
                                        brush = Brush.verticalGradient(
                                            colors = listOf(
                                                Color(0xFF00B2FF),
                                                JaguarTeal
                                            )
                                        ),
                                        shape = RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp)
                                    )
                            )
                        }
                    }
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    points.forEach { point ->
                        Text(
                            text = point.dateLabel,
                            style = MaterialTheme.typography.labelSmall,
                            color = JaguarGray,
                            modifier = Modifier.weight(1f),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun YAxisLabel(weight: String, reps: String) {
    Column(horizontalAlignment = Alignment.End, modifier = Modifier.width(40.dp)) {
        Text(text = weight, style = MaterialTheme.typography.labelSmall, color = JaguarGray, fontSize = 9.sp)
        Text(text = "/", style = MaterialTheme.typography.labelSmall, color = JaguarBorder, fontSize = 8.sp)
        Text(text = reps, style = MaterialTheme.typography.labelSmall, color = JaguarGray, fontSize = 9.sp)
    }
}

@Composable
fun StatsRow(
    estimatedOneRm: Double,
    progressPercent: Double,
    microcycles: Int,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        StatCard(
            label = stringResource(R.string.estimated_1rm_label),
            value = "${formatHistoryNumber(estimatedOneRm)} kg",
            modifier = Modifier.weight(1f)
        )

        StatCard(
            label = stringResource(R.string.progress_label),
            value = "${if (progressPercent >= 0) "+" else ""}${formatHistoryNumber(progressPercent)}%",
            valueColor = JaguarTeal,
            modifier = Modifier.weight(1f)
        )

        StatCard(
            label = stringResource(R.string.microcycles_label),
            value = microcycles.toString(),
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
fun StatCard(
    label: String,
    value: String,
    valueColor: Color = JaguarWhite,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.height(90.dp),
        colors = CardDefaults.cardColors(containerColor = JaguarSurface),
        shape = RoundedCornerShape(12.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, JaguarBorder)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = JaguarGray,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.headlineSmall,
                color = valueColor,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
        }
    }
}

fun formatHistoryNumber(value: Double): String {
    return if (value % 1.0 == 0.0) {
        value.toInt().toString()
    } else {
        String.format(Locale.US, "%.1f", value)
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0A0A0A)
@Composable
fun HistoryScreenPreview() {
    JaguarTrackerTheme {
        HistoryScreen()
    }
}
