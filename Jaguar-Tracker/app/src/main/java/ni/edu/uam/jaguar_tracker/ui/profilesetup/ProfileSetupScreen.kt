package ni.edu.uam.jaguar_tracker.ui.profilesetup

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ni.edu.uam.jaguar_tracker.R
import ni.edu.uam.jaguar_tracker.ui.theme.*

@Composable
fun ProfileSetupScreen(
    modifier: Modifier = Modifier,
    onSave: (String, String) -> Unit = { _, _ -> },
    onSkip: () -> Unit = {}
) {
    var weight by remember { mutableStateOf("70") }
    var selectedGender by remember { mutableStateOf("Masculino") }

    Scaffold(
        containerColor = JaguarBlack,
        modifier = modifier.fillMaxSize()
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(24.dp)
        ) {
            Spacer(modifier = Modifier.height(32.dp))

            ProfileSetupHeader()

            Spacer(modifier = Modifier.height(32.dp))

            ProfileSetupLabel(text = stringResource(R.string.body_weight_label))

            Spacer(modifier = Modifier.height(12.dp))

            ProfileSetupTextField(
                value = weight,
                onValueChange = { weight = it }
            )

            Spacer(modifier = Modifier.height(24.dp))

            ProfileSetupLabel(text = stringResource(R.string.gender_label))

            Spacer(modifier = Modifier.height(12.dp))

            GenderSelector(
                selectedGender = selectedGender,
                onGenderSelected = { selectedGender = it }
            )

            Spacer(modifier = Modifier.height(24.dp))

            WeightWarning()

            Spacer(modifier = Modifier.weight(1f))

            ProfileSetupActions(
                onSave = { onSave(weight, selectedGender) },
                onSkip = onSkip
            )
        }
    }
}

@Composable
private fun ProfileSetupHeader() {
    Column {
        Text(
            text = stringResource(R.string.profile_setup_title),
            style = MaterialTheme.typography.headlineMedium,
            color = JaguarWhite,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = stringResource(R.string.profile_setup_subtitle),
            style = MaterialTheme.typography.bodyMedium,
            color = JaguarGray
        )
    }
}

@Composable
private fun ProfileSetupLabel(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.labelLarge,
        color = JaguarWhite,
        fontWeight = FontWeight.SemiBold
    )
}

@Composable
private fun ProfileSetupTextField(
    value: String,
    onValueChange: (String) -> Unit
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = JaguarSurface,
            unfocusedContainerColor = JaguarSurface,
            focusedBorderColor = JaguarBorder,
            unfocusedBorderColor = JaguarBorder,
            focusedTextColor = JaguarWhite,
            unfocusedTextColor = JaguarWhite,
            cursorColor = JaguarTeal
        ),
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
    )
}

@Composable
private fun GenderSelector(
    selectedGender: String,
    onGenderSelected: (String) -> Unit
) {
    val options = listOf(
        stringResource(R.string.gender_male),
        stringResource(R.string.gender_female)
    )

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        options.forEach { option ->
            val isSelected = selectedGender == option

            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(56.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(JaguarSurface)
                    .border(
                        width = 1.dp,
                        color = if (isSelected) JaguarBorder.copy(alpha = 0.8f) else JaguarBorder,
                        shape = RoundedCornerShape(12.dp)
                    )
                    .border(
                        width = if (isSelected) 1.dp else 0.dp,
                        color = if (isSelected) JaguarWhite.copy(alpha = 0.5f) else Color.Transparent,
                        shape = RoundedCornerShape(12.dp)
                    )
                    .clickable { onGenderSelected(option) },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = option,
                    style = MaterialTheme.typography.titleMedium,
                    color = JaguarWhite,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun WeightWarning() {
    Surface(
        color = JaguarSurface,
        shape = RoundedCornerShape(12.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, JaguarBorder),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier.size(24.dp),
                contentAlignment = Alignment.Center
            ) {
                Canvas(modifier = Modifier.size(20.dp)) {
                    val path = Path().apply {
                        moveTo(size.width / 2f, 2f)
                        lineTo(size.width - 2f, size.height - 2f)
                        lineTo(2f, size.height - 2f)
                        close()
                    }
                    drawPath(path, color = Color(0xFFEBB134))
                }
                Text(
                    text = "!",
                    color = JaguarBlack,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Black,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Text(
                text = stringResource(R.string.weight_expiry_warning),
                style = MaterialTheme.typography.bodySmall,
                color = JaguarGray,
                lineHeight = 18.sp
            )
        }
    }
}

@Composable
private fun ProfileSetupActions(
    onSave: () -> Unit,
    onSkip: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Button(
            onClick = onSkip,
            modifier = Modifier
                .weight(1f)
                .height(56.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent,
                contentColor = JaguarWhite
            ),
            border = androidx.compose.foundation.BorderStroke(1.dp, JaguarBorder)
        ) {
            Text(
                text = stringResource(R.string.skip_for_now),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
        }

        Button(
            onClick = onSave,
            modifier = Modifier
                .weight(1f)
                .height(56.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = JaguarTeal,
                contentColor = JaguarBlack
            )
        ) {
            Text(
                text = stringResource(R.string.save_button),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0A0A0A)
@Composable
fun ProfileSetupScreenPreview() {
    JaguarTrackerTheme {
        ProfileSetupScreen()
    }
}
