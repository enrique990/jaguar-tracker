package ni.edu.uam.jaguar_tracker.ui.rankings

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Upload
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ni.edu.uam.jaguar_tracker.R
import ni.edu.uam.jaguar_tracker.ui.home.JaguarBottomNavigation
import ni.edu.uam.jaguar_tracker.ui.theme.*
import androidx.compose.ui.platform.LocalLocale

data class RankingUser(
    val rank: Int,
    val userId: String,
    val score: Double,
    val hasVideo: Boolean = false
)

@Composable
fun RankingScreen(
    modifier: Modifier = Modifier,
    onHomeClick: () -> Unit = {},
    onHistoryClick: () -> Unit = {},
    onProfileClick: () -> Unit = {},
    onRankingClick: () -> Unit = {}
) {
    val rankingUsers = listOf(
        RankingUser(1, "2021-1234", 2.10, true),
        RankingUser(2, "2020-5678", 1.95, true),
        RankingUser(3, "2022-9012", 1.88, false),
        RankingUser(4, "2021-3456", 1.82, true),
        RankingUser(5, "2019-7890", 1.75, false),
        RankingUser(6, "2022-2345", 1.68, false),
        RankingUser(7, "2021-6789", 1.62, true)
    )

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = JaguarBlack,
        bottomBar = {
            JaguarBottomNavigation(
                selectedTabIndex = 2,
                onHomeClick = onHomeClick,
                onHistoryClick = onHistoryClick,
                onProfileClick = onProfileClick,
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

            // Header
            item {
                RankingHeader()
            }

            // Upload Button
            item {
                UploadRecordButton()
            }

            // Ranking List Section
            item {
                RankingListCard(rankingUsers)
            }

            // User Position Card
            item {
                UserPositionCard(
                    position = 12,
                    relativeStrength = 1.45,
                    pointsToTop10 = 0.23
                )
            }

            item { Spacer(modifier = Modifier.height(16.dp)) }
        }
    }
}

@Composable
fun RankingHeader(modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Default.EmojiEvents,
                contentDescription = null,
                tint = JaguarTeal,
                modifier = Modifier.size(32.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = stringResource(R.string.ranking_title),
                style = MaterialTheme.typography.headlineMedium,
                color = JaguarWhite
            )
        }
        Text(
            text = stringResource(R.string.ranking_subtitle),
            style = MaterialTheme.typography.bodyMedium,
            color = JaguarGray,
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}

@Composable
fun UploadRecordButton(modifier: Modifier = Modifier) {
    Button(
        onClick = { /* TODO */ },
        modifier = modifier
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
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Upload,
                    contentDescription = null,
                    tint = JaguarBlack,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = stringResource(R.string.subir_record_button),
                    color = JaguarBlack,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleSmall
                )
            }
        }
    }
}

@Composable
fun RankingListCard(users: List<RankingUser>, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = JaguarCard)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = stringResource(R.string.clasificacion_fuerza_title),
                style = MaterialTheme.typography.titleMedium,
                color = JaguarWhite
            )
            Text(
                text = stringResource(R.string.clasificacion_fuerza_subtitle),
                style = MaterialTheme.typography.labelSmall,
                color = JaguarGray
            )
            Spacer(modifier = Modifier.height(16.dp))
            users.forEachIndexed { index, user ->
                RankingItem(user = user)
                if (index < users.size - 1) {
                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 8.dp),
                        thickness = 0.5.dp,
                        color = JaguarBorder
                    )
                }
            }
        }
    }
}

@Composable
fun RankingItem(user: RankingUser, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RankBadge(rank = user.rank)
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = user.userId,
                style = MaterialTheme.typography.titleMedium,
                color = JaguarWhite,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = stringResource(R.string.puntuacion_label, user.score),
                style = MaterialTheme.typography.bodyMedium,
                color = JaguarGray
            )
        }
        if (user.hasVideo) {
            IconButton(
                onClick = { /* TODO */ },
                modifier = Modifier
                    .size(36.dp)
                    .border(1.dp, JaguarBorder, RoundedCornerShape(8.dp))
            ) {
                Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = null,
                    tint = JaguarTeal,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

@Composable
fun RankBadge(rank: Int, modifier: Modifier = Modifier) {
    val backgroundColor = when (rank) {
        1 -> Color(0xFFFFB800)
        2 -> Color(0xFF9E9E9E)
        3 -> Color(0xFFFF5722)
        else -> JaguarSurface
    }
    
    Box(
        modifier = modifier
            .size(36.dp)
            .background(backgroundColor, CircleShape),
        contentAlignment = Alignment.Center
    ) {
        if (rank <= 3) {
            Icon(
                imageVector = Icons.Default.EmojiEvents,
                contentDescription = null,
                tint = JaguarWhite,
                modifier = Modifier.size(20.dp)
            )
        } else {
            Text(
                text = rank.toString(),
                color = JaguarWhite,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
fun UserPositionCard(
    position: Int,
    relativeStrength: Double,
    pointsToTop10: Double,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = JaguarCard)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.tu_posicion_title),
                    style = MaterialTheme.typography.titleLarge,
                    color = JaguarWhite
                )
                Badge(
                    containerColor = JaguarTeal,
                    contentColor = JaguarBlack,
                    modifier = Modifier.size(width = 44.dp, height = 24.dp)
                ) {
                    Text(
                        text = "#$position",
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.labelLarge
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = stringResource(R.string.fuerza_relativa_label),
                    style = MaterialTheme.typography.bodyMedium,
                    color = JaguarGray
                )
                Text(
                    text = String.format(LocalLocale.current.platformLocale, "%.2f", relativeStrength),
                    style = MaterialTheme.typography.bodyMedium,
                    color = JaguarWhite,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = stringResource(R.string.posiciones_top_10_label),
                    style = MaterialTheme.typography.bodyMedium,
                    color = JaguarGray
                )
                Text(
                    text = String.format(LocalLocale.current.platformLocale, "+%.2f", pointsToTop10),
                    style = MaterialTheme.typography.bodyMedium,
                    color = JaguarTeal,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0A0A0A)
@Composable
fun UserPositionCardPreview() {
    JaguarTrackerTheme {
        Box(modifier = Modifier.padding(16.dp)) {
            UserPositionCard(
                position = 12,
                relativeStrength = 1.45,
                pointsToTop10 = 0.23
            )
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
