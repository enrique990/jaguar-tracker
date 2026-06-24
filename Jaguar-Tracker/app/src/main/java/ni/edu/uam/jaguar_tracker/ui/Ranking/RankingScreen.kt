package ni.edu.uam.jaguar_tracker.ui.ranking

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ni.edu.uam.jaguar_tracker.R
import ni.edu.uam.jaguar_tracker.ui.home.JaguarBottomNavigation
import ni.edu.uam.jaguar_tracker.ui.theme.*

data class RankingUser(
    val rank: Int,
    val userId: String,
    val score: Double,
)

@Composable
fun RankingScreen(
    modifier: Modifier = Modifier,
    onHomeClick: () -> Unit = {},
    onHistoryClick: () -> Unit = {},
    onProfileClick: () -> Unit = {},
) {
    val rankingUsers = listOf(
        RankingUser(1, "2021-1234", 2.10),
        RankingUser(2, "2020-5678", 1.95),
        RankingUser(3, "2022-9012", 1.88),
        RankingUser(4, "2021-3456", 1.82),
        RankingUser(5, "2019-7890", 1.75),
        RankingUser(6, "2022-2345", 1.68),
        RankingUser(7, "2021-6789", 1.62)
    )

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = JaguarBlack,
        bottomBar = {
            JaguarBottomNavigation(
                selectedTabIndex = 2,
                onHomeClick = onHomeClick,
                onHistoryClick = onHistoryClick,
                onProfileClick = onProfileClick
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item { Spacer(modifier = Modifier.height(8.dp)) }

            // Header
            item {
                RankingHeader()
            }

            // Subir Récord Button
            item {
                SubirRecordButton()
            }

            // Classification Section
            item {
                ClassificationCard(rankingUsers)
            }

            // Tu Posición Section
            item {
                UserPositionCard(
                    rank = 12,
                    fuerzaRelativa = 1.45,
                    posicionesParaTop10 = 0.23
                )
            }

            item { Spacer(modifier = Modifier.height(24.dp)) }
        }
    }
}

@Composable
fun RankingHeader(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.EmojiEvents,
            contentDescription = null,
            tint = JaguarTeal,
            modifier = Modifier.size(32.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(
                text = stringResource(R.string.ranking_title),
                style = MaterialTheme.typography.headlineMedium,
                color = JaguarWhite
            )
            Text(
                text = stringResource(R.string.ranking_subtitle),
                style = MaterialTheme.typography.bodyMedium,
                color = JaguarGray
            )
        }
    }
}

@Composable
fun SubirRecordButton(modifier: Modifier = Modifier) {
    Button(
        onClick = { /* TODO */ },
        modifier = modifier
            .fillMaxWidth()
            .height(52.dp),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = JaguarTeal,
            contentColor = JaguarBlack
        )
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Default.FileUpload,
                contentDescription = null,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = stringResource(R.string.subir_record_button),
                fontWeight = FontWeight.ExtraBold,
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}

@Composable
fun ClassificationCard(
    users: List<RankingUser>,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = JaguarSurface),
        shape = RoundedCornerShape(16.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, JaguarBorder)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Text(
                text = stringResource(R.string.clasificacion_fuerza_title),
                style = MaterialTheme.typography.titleSmall,
                color = JaguarWhite,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = stringResource(R.string.clasificacion_fuerza_subtitle),
                style = MaterialTheme.typography.labelSmall,
                color = JaguarGray
            )
            
            Spacer(modifier = Modifier.height(20.dp))

            users.forEachIndexed { index, user ->
                RankingItem(user = user)
                if (index < (users.size - 1)) {
                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 14.dp),
                        color = JaguarBorder,
                        thickness = 0.5.dp
                    )
                }
            }
        }
    }
}

@Composable
fun RankingItem(user: RankingUser, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        val rankColor = when (user.rank) {
            1 -> Color(0xFFFFB800) // Gold
            2 -> Color(0xFFB0B0B0) // Silver
            3 -> Color(0xFFFF6B00) // Bronze/Orange
            else -> Color(0xFF242424) // Dark Gray for others
        }

        Box(
            modifier = Modifier
                .size(44.dp)
                .background(rankColor, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            if (user.rank <= 3) {
                Icon(
                    imageVector = Icons.Default.EmojiEvents,
                    contentDescription = null,
                    tint = JaguarWhite,
                    modifier = Modifier.size(24.dp)
                )
            } else {
                Text(
                    text = user.rank.toString(),
                    color = JaguarGray,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        Spacer(modifier = Modifier.width(16.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = user.userId,
                color = JaguarWhite,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = stringResource(R.string.puntuacion_label, user.score),
                color = JaguarGray,
                style = MaterialTheme.typography.bodyMedium
            )
        }

        IconButton(
            onClick = { /* TODO */ },
            modifier = Modifier
                .size(36.dp)
                .border(1.dp, Color(0x3300E6B3), CircleShape) // Faint teal border
        ) {
            Icon(
                imageVector = Icons.Default.PlayArrow,
                contentDescription = null,
                tint = JaguarTeal,
                modifier = Modifier.size(18.dp)
            )
        }
    }
}

@Composable
fun UserPositionCard(
    rank: Int,
    fuerzaRelativa: Double,
    posicionesParaTop10: Double,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = JaguarSurface),
        shape = RoundedCornerShape(16.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, JaguarBorder)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.tu_posicion_title),
                    style = MaterialTheme.typography.headlineSmall,
                    color = JaguarWhite,
                    fontWeight = FontWeight.Bold
                )
                Surface(
                    color = JaguarTeal,
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text(
                        text = "#$rank",
                        color = JaguarBlack,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp),
                        style = MaterialTheme.typography.labelLarge
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = stringResource(R.string.fuerza_relativa_label),
                    color = JaguarGray,
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = stringResource(R.string.fuerza_relativa_value, fuerzaRelativa),
                    color = JaguarWhite,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.bodyLarge
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = stringResource(R.string.posiciones_top_10_label),
                    color = JaguarGray,
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = stringResource(R.string.posiciones_top_10_value, posicionesParaTop10),
                    color = JaguarGreen,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0A0A0A)
@Composable
fun RankingScreenPreview() {
    JaguarTrackerTheme {
        RankingScreen()
    }
}
