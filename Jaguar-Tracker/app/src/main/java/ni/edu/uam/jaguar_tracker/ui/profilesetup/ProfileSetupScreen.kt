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
import androidx.lifecycle.viewmodel.compose.viewModel
import ni.edu.uam.jaguar_tracker.R
import ni.edu.uam.jaguar_tracker.ui.theme.*

@Composable
fun ProfileSetupScreen(
    modifier: Modifier = Modifier,
    onSaveSuccess: () -> Unit = {},
    onSkip: () -> Unit = {},
    viewModel: ProfileSetupViewModel = viewModel()
) {
    val state by viewModel.uiState.collectAsState()

    LaunchedEffect(state.savedSuccessfully) {
        if (state.savedSuccessfully) {
            onSaveSuccess()
        }
    }

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
                value = state.weight,
                enabled = !state.isSaving,
                onValueChange = viewModel::onWeightChanged
            )

            if (state.error != null) {
                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = state.error ?: "",
                    color = JaguarRed,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            ProfileSetupLabel(text = stringResource(R.string.gender_label))

            Spacer(modifier = Modifier.height(12.dp))

            GenderSelector(
                selectedGender = state.selectedGender,
                enabled = !state.isSaving,
                onGenderSelected = viewModel::onGenderChanged
            )

            Spacer(modifier = Modifier.height(24.dp))

            WeightWarning()

            Spacer(modifier = Modifier.weight(1f))

            ProfileSetupActions(
                isSaving = state.isSaving,
                onSave = viewModel::guardarPeso,
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
    enabled: Boolean,
    onValueChange: (String) -> Unit
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        enabled = enabled,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = JaguarSurface,
            unfocusedContainerColor = JaguarSurface,
            disabledContainerColor = JaguarSurface,
            focusedBorderColor = JaguarTeal,
            unfocusedBorderColor = JaguarBorder,
            disabledBorderColor = JaguarBorder,
            focusedTextColor = JaguarWhite,
            unfocusedTextColor = JaguarWhite,
            disabledTextColor = JaguarGray,
            cursorColor = JaguarTeal
        ),
        singleLine = true,
        suffix = {
            Text(
                text = "kg",
                color = JaguarGray
            )
        },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
    )
}

@Composable
private fun GenderSelector(
    selectedGender: String,
    enabled: Boolean,
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
                        color = if (isSelected) JaguarWhite.copy(alpha = 0.5f) else JaguarBorder,
                        shape = RoundedCornerShape(12.dp)
                    )
                    .clickable(enabled = enabled) {
                        onGenderSelected(option)
                    },
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
    isSaving: Boolean,
    onSave: () -> Unit,
    onSkip: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Button(
            onClick = onSkip,
            enabled = !isSaving,
            modifier = Modifier
                .weight(1f)
                .height(56.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent,
                contentColor = JaguarWhite,
                disabledContainerColor = Color.Transparent,
                disabledContentColor = JaguarGray
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
            enabled = !isSaving,
            modifier = Modifier
                .weight(1f)
                .height(56.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = JaguarTeal,
                contentColor = JaguarBlack,
                disabledContainerColor = JaguarGray,
                disabledContentColor = JaguarBlack
            )
        ) {
            if (isSaving) {
                CircularProgressIndicator(
                    modifier = Modifier.size(22.dp),
                    color = JaguarBlack,
                    strokeWidth = 2.dp
                )
            } else {
                Text(
                    text = stringResource(R.string.save_button),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }
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