package ni.edu.uam.jaguar_tracker.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import ni.edu.uam.jaguar_tracker.ui.theme.JaguarTrackerTheme

// --- POO: Encapsulamiento de Datos (Modelo del Atleta) ---
data class WorkoutActivity(
    val id: Int,
    val type: String,
    val duration: String,
    val date: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen() {
    // Datos simulados (Mock) para el diseño UI/UX del gimnasio
    val recentWorkouts = listOf(
        WorkoutActivity(1, "Pecho y Tríceps", "1h 15m", "Hoy"),
        WorkoutActivity(2, "Espalda y Bíceps", "1h 10m", "Ayer"),
        WorkoutActivity(3, "Pierna y Hombro", "1h 30m", "Hace 3 días")
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Jaguar Tracker", fontWeight = FontWeight.Bold) },
                actions = {
                    IconButton(onClick = { /* TODO: Búsqueda de máquinas o rutinas */ }) {
                        Icon(Icons.Default.Search, contentDescription = "Buscar")
                    }
                    IconButton(onClick = { /* TODO: Abrir perfil del estudiante/atleta */ }) {
                        Icon(Icons.Default.AccountCircle, contentDescription = "Perfil")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Home, contentDescription = "Inicio") },
                    label = { Text("Inicio") },
                    selected = true,
                    onClick = { /* TODO: Navegar a Inicio */ }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.FitnessCenter, contentDescription = "Rutinas") },
                    label = { Text("Rutinas") },
                    selected = false,
                    onClick = { /* TODO: Navegar a Rutinas */ }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Person, contentDescription = "Perfil") },
                    label = { Text("Perfil") },
                    selected = false,
                    onClick = { /* TODO: Navegar a Perfil */ }
                )
            }
        }
    ) { paddingValues ->
        // Usamos LazyColumn para eficiencia en listas largas de historial de entrenamiento
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Text(text = "Hola", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
                Text(text = "¿Listo para tu entrenamiento en la UAM?", style = MaterialTheme.typography.bodyLarge, color = Color.Gray)
            }

            item {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    ActionCard(modifier = Modifier.weight(1f), icon = Icons.Default.PlayArrow, title = "Iniciar\nEntrenamiento")
                    ActionCard(modifier = Modifier.weight(1f), icon = Icons.Default.FitnessCenter, title = "Mis\nRutinas")
                }
                Spacer(modifier = Modifier.height(16.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    ActionCard(modifier = Modifier.weight(1f), icon = Icons.Default.Timeline, title = "Mi\nProgreso")
                    ActionCard(modifier = Modifier.weight(1f), icon = Icons.Default.DateRange, title = "Reservas\n")
                }
            }

            item {
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "Entrenamientos Recientes", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            }

            items(recentWorkouts) { workout ->
                WorkoutItem(workout)
            }
        }
    }
}

// --- POO: Composición UI ---
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActionCard(modifier: Modifier = Modifier, icon: ImageVector, title: String) {
    Card(
        modifier = modifier.height(100.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer),
        onClick = { /* TODO: Asignar navegación */ }
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(icon, contentDescription = title, tint = MaterialTheme.colorScheme.onSecondaryContainer, modifier = Modifier.size(32.dp))
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSecondaryContainer,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun WorkoutItem(workout: WorkoutActivity) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(MaterialTheme.colorScheme.primaryContainer, shape = RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
            ) {
                // Icono representativo de completado
                Icon(Icons.Default.CheckCircle, contentDescription = null, tint = MaterialTheme.colorScheme.onPrimaryContainer)
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(text = workout.type, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodyLarge)
                Text(text = "Duración: ${workout.duration}", style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
                Text(text = workout.date, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.primary)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    JaguarTrackerTheme {
        HomeScreen()
    }
}
