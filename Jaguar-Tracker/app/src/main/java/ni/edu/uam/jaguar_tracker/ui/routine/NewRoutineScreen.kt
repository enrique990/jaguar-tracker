package ni.edu.uam.jaguar_tracker.ui.routine

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.ui.tooling.preview.Preview
import ni.edu.uam.jaguar_tracker.ui.theme.JaguarTrackerTheme
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun NewRoutineScreen(
    viewModel: NewRoutineViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
    onNavigateBack: () -> Unit = {}
) {
    // Observamos el estado reactivo del ViewModel
    val uiState by viewModel.uiState.collectAsState()

    NewRoutineContent(
        uiState = uiState,
        onNavigateBack = onNavigateBack,
        onUpdateRoutineName = { viewModel.updateRoutineName(it) },
        onToggleSheetVisibility = { viewModel.toggleSheetVisibility(it) },
        onToggleExerciseSelection = { viewModel.toggleExerciseSelection(it) },
        onSaveRoutine = { viewModel.saveRoutine() }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewRoutineContent(
    uiState: NewRoutineUiState,
    onNavigateBack: () -> Unit,
    onUpdateRoutineName: (String) -> Unit,
    onToggleSheetVisibility: (Boolean) -> Unit,
    onToggleExerciseSelection: (Int) -> Unit,
    onSaveRoutine: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Nueva Rutina", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Regresar")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        bottomBar = {
            // Ley UX: Prevención de errores. Deshabilita el botón si no hay nombre o ejercicios.
            val isSaveEnabled = uiState.routineName.isNotBlank() && uiState.selectedExercises.isNotEmpty()

            Surface(shadowElevation = 8.dp) {
                PaddingValues(16.dp)
                Button(
                    onClick = onSaveRoutine,
                    enabled = isSaveEnabled,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .height(50.dp)
                ) {
                    Text("Guardar Rutina")
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            OutlinedTextField(
                value = uiState.routineName,
                onValueChange = onUpdateRoutineName,
                label = { Text("Nombre de la rutina") },
                placeholder = { Text("Ej: Día de pierna") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Ejercicios", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                TextButton(onClick = { onToggleSheetVisibility(true) }) {
                    Icon(Icons.Default.Add, contentDescription = "Añadir")
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Añadir Ejercicio")
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            if (uiState.selectedExercises.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No hay ejercicios en esta rutina.", color = Color.Gray)
                }
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(uiState.selectedExercises) { exercise ->
                        ExerciseListItem(exercise = exercise)
                    }
                }
            }
        }
    }

    // --- Panel Inferior (Bottom Sheet) para selección de ejercicios ---
    if (uiState.isSheetOpen) {
        ModalBottomSheet(
            onDismissRequest = { onToggleSheetVisibility(false) },
            sheetState = sheetState
        ) {
            ExerciseSelectionSheet(
                availableExercises = uiState.availableExercises,
                onToggleSelection = onToggleExerciseSelection,
                onClose = { onToggleSheetVisibility(false) }
            )
        }
    }
}

// --- POO: Composición de UI (Módulos independientes) ---

@Composable
fun ExerciseListItem(exercise: Exercise) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = exercise.name, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold)
            Text(text = "${exercise.sets} Series x ${exercise.reps} Reps", style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
        }
    }
}

@Composable
fun ExerciseSelectionSheet(
    availableExercises: List<Exercise>,
    onToggleSelection: (Int) -> Unit,
    onClose: () -> Unit
) {
    Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
        Text("Catálogo de Ejercicios", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            modifier = Modifier.weight(1f, fill = false),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(availableExercises) { exercise ->
                Card(
                    onClick = { onToggleSelection(exercise.id) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = if (exercise.isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = exercise.name,
                            modifier = Modifier.weight(1f),
                            fontWeight = if (exercise.isSelected) FontWeight.Bold else FontWeight.Normal
                        )
                        if (exercise.isSelected) {
                            Icon(Icons.Default.Check, contentDescription = "Seleccionado", tint = MaterialTheme.colorScheme.primary)
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = onClose,
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
        ) {
            Text("Listo")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun NewRoutineScreenPreview() {
    JaguarTrackerTheme {
        NewRoutineContent(
            uiState = NewRoutineUiState(
                routineName = "Día de pierna",
                selectedExercises = listOf(
                    Exercise(1, "Press de banca", 3, 12),
                    Exercise(2, "Sentadillas", 4, 10)
                ),
                availableExercises = listOf(
                    Exercise(1, "Press de banca", isSelected = true),
                    Exercise(2, "Sentadillas", isSelected = true),
                    Exercise(3, "Peso muerto"),
                    Exercise(4, "Dominadas"),
                    Exercise(5, "Curl de bíceps")
                )
            ),
            onNavigateBack = {},
            onUpdateRoutineName = {},
            onToggleSheetVisibility = {},
            onToggleExerciseSelection = {},
            onSaveRoutine = {}
        )
    }
}
