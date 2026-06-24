package ni.edu.uam.jaguar_tracker.ui.profilesetup

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ni.edu.uam.jaguar_tracker.R
import ni.edu.uam.jaguar_tracker.ui.home.JaguarBottomNavigation
import ni.edu.uam.jaguar_tracker.ui.theme.*

@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    onHomeClick: () -> Unit = {},
    onHistoryClick: () -> Unit = {},
    onRankingClick: () -> Unit = {},
    onProfileClick: () -> Unit = {}
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = JaguarBlack,
        bottomBar = {
            JaguarBottomNavigation(
                selectedTabIndex = 3,
                onHomeClick = onHomeClick,
                onHistoryClick = onHistoryClick,
                onRankingClick = onRankingClick,
                onProfileClick = onProfileClick
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
            item { ProfileHeader() }
            item { UserProfileSection() }
            item { StrengthCalculatorSection() }
            item { BodyWeightSection() }
            item { FeedbackSection() }
            item { ProfileNotificationSection() }
            item { GeneralStatsSection() }
            item { Spacer(modifier = Modifier.height(16.dp)) }
        }
    }
}

@Composable
fun ProfileHeader(modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        Text(
            text = stringResource(R.string.profile_title),
            style = MaterialTheme.typography.headlineMedium,
            color = JaguarWhite
        )
        Text(
            text = stringResource(R.string.profile_subtitle),
            style = MaterialTheme.typography.bodyMedium,
            color = JaguarGray
        )
    }
}

@Composable
fun UserProfileSection(modifier: Modifier = Modifier) {
    SectionCard(modifier = modifier) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(12.dp)
        ) {
            IconBox(
                icon = Icons.Default.Person,
                backgroundColor = JaguarTeal
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = stringResource(R.string.user_profile_label),
                    style = MaterialTheme.typography.titleMedium,
                    color = JaguarWhite
                )

            }
        }
    }
}

@Composable
fun StrengthCalculatorSection(modifier: Modifier = Modifier) {
    SectionCard(modifier = modifier) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconBox(
                    icon = Icons.Default.Calculate,
                    backgroundColor = Color(0xFF00D1FF)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(
                        text = stringResource(R.string.strength_calculator_title),
                        style = MaterialTheme.typography.titleMedium,
                        color = JaguarWhite
                    )
                    Text(
                        text = stringResource(R.string.strength_calculator_subtitle),
                        style = MaterialTheme.typography.labelSmall,
                        color = JaguarGray
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = stringResource(R.string.exercise_label),
                style = MaterialTheme.typography.labelSmall,
                color = JaguarGray,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            // Exercise Dropdown Placeholder
            var expanded by remember { mutableStateOf(false) }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .background(JaguarBlack, RoundedCornerShape(8.dp))
                    .border(1.dp, JaguarBorder, RoundedCornerShape(8.dp))
                    .clickable { expanded = !expanded }
                    .padding(horizontal = 12.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "Press de Banca", color = JaguarWhite, style = MaterialTheme.typography.bodyMedium)
                    Icon(Icons.Default.KeyboardArrowDown, contentDescription = null, tint = JaguarGray)
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "Valores por defecto de tu última sesión",
                style = MaterialTheme.typography.labelSmall,
                color = JaguarGray.copy(alpha = 0.6f),
                fontSize = 10.sp
            )
            Spacer(modifier = Modifier.height(8.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                CalculatorInputField(label = "Peso (kg)", value = "100", modifier = Modifier.weight(1f))
                CalculatorInputField(label = "Reps", value = "8", modifier = Modifier.weight(1f))
                CalculatorInputField(label = "Peso Corp.", value = "75", modifier = Modifier.weight(1f))
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { /* TODO */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = JaguarTeal)
            ) {
                Text(
                    text = stringResource(R.string.calculate_button),
                    color = JaguarBlack,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleSmall
                )
            }
        }
    }
}

@Composable
fun BodyWeightSection(modifier: Modifier = Modifier) {
    SectionCard(modifier = modifier) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconBox(
                        icon = Icons.Default.Balance,
                        backgroundColor = Color(0xFF2196F3)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text(
                            text = stringResource(R.string.body_weight_title),
                            style = MaterialTheme.typography.titleMedium,
                            color = JaguarWhite
                        )
                        Text(
                            text = stringResource(R.string.body_weight_subtitle),
                            style = MaterialTheme.typography.labelSmall,
                            color = JaguarGray
                        )
                    }
                }
                
                Surface(
                    color = JaguarRed,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.height(24.dp)
                ) {
                    Text(
                        text = stringResource(R.string.expired_tag),
                        color = JaguarWhite,
                        style = MaterialTheme.typography.labelSmall,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                DataRow(label = stringResource(R.string.current_weight_label), value = "75 kg")
                HorizontalDivider(color = JaguarBorder, thickness = 1.dp)
                DataRow(label = stringResource(R.string.last_update_label), value = "13/04/2026")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { /* TODO */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = JaguarTeal)
            ) {
                Text(
                    text = stringResource(R.string.update_weight_button),
                    color = JaguarBlack,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleSmall
                )
            }
        }
    }
}

@Composable
fun FeedbackSection(modifier: Modifier = Modifier) {
    SectionCard(modifier = modifier) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconBox(
                    icon = Icons.Default.ChatBubble,
                    backgroundColor = Color(0xFF9C27B0)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = stringResource(R.string.feedback_title),
                    style = MaterialTheme.typography.titleMedium,
                    color = JaguarWhite
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = "",
                onValueChange = {},
                placeholder = {
                    Text(
                        text = stringResource(R.string.feedback_placeholder),
                        color = JaguarGray,
                        style = MaterialTheme.typography.bodySmall
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp),
                shape = RoundedCornerShape(8.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = JaguarBlack,
                    unfocusedContainerColor = JaguarBlack,
                    focusedBorderColor = JaguarBorder,
                    unfocusedBorderColor = JaguarBorder
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { /* TODO */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = JaguarTeal)
            ) {
                Text(
                    text = stringResource(R.string.send_button),
                    color = JaguarBlack,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleSmall
                )
            }
        }
    }
}

@Composable
fun ProfileNotificationSection(modifier: Modifier = Modifier) {
    SectionCard(modifier = modifier) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconBox(
                    icon = Icons.Default.Notifications,
                    backgroundColor = Color(0xFFFF9800)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(
                        text = stringResource(R.string.notifications_title),
                        style = MaterialTheme.typography.titleMedium,
                        color = JaguarWhite
                    )
                    Text(
                        text = stringResource(R.string.notifications_subtitle),
                        style = MaterialTheme.typography.labelSmall,
                        color = JaguarGray
                    )
                }
            }

            var checked by remember { mutableStateOf(true) }
            Switch(
                checked = checked,
                onCheckedChange = { checked = it },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = JaguarWhite,
                    checkedTrackColor = JaguarTeal,
                    uncheckedThumbColor = JaguarGray,
                    uncheckedTrackColor = JaguarBlack
                )
            )
        }
    }
}

@Composable
fun GeneralStatsSection(modifier: Modifier = Modifier) {
    SectionCard(modifier = modifier) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = stringResource(R.string.general_stats_title),
                style = MaterialTheme.typography.titleMedium,
                color = JaguarWhite
            )

            Spacer(modifier = Modifier.height(16.dp))

            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                StatRow(label = stringResource(R.string.completed_workouts_label), value = "42")
                StatRow(label = stringResource(R.string.created_routines_label), value = "5")
                StatRow(label = stringResource(R.string.active_days_label), value = "89")
            }
        }
    }
}

@Composable
fun SectionCard(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        color = JaguarSurface,
        border = androidx.compose.foundation.BorderStroke(1.dp, JaguarBorder)
    ) {
        content()
    }
}

@Composable
fun IconBox(
    icon: ImageVector,
    backgroundColor: Color,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(40.dp)
            .background(backgroundColor, RoundedCornerShape(8.dp)),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = JaguarBlack,
            modifier = Modifier.size(24.dp)
        )
    }
}

@Composable
fun CalculatorInputField(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = JaguarGray,
            fontSize = 10.sp,
            modifier = Modifier.padding(bottom = 4.dp)
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(40.dp)
                .background(JaguarBlack, RoundedCornerShape(8.dp))
                .border(1.dp, JaguarBorder, RoundedCornerShape(8.dp)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = value,
                color = JaguarWhite,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun DataRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = label, color = JaguarGray, style = MaterialTheme.typography.bodySmall)
        Text(text = value, color = JaguarWhite, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun StatRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = label, color = JaguarGray, style = MaterialTheme.typography.bodySmall)
        Text(text = value, color = JaguarWhite, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0A0A0A)
@Composable
fun ProfileScreenPreview() {
    JaguarTrackerTheme {
        ProfileScreen()
    }
}
