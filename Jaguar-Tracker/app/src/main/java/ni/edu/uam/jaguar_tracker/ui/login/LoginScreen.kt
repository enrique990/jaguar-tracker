package ni.edu.uam.jaguar_tracker.ui.login

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import ni.edu.uam.jaguar_tracker.R
import ni.edu.uam.jaguar_tracker.ui.theme.JaguarTrackerTheme
import ni.edu.uam.jaguar_tracker.ui.theme.JaguarTeal
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun LoginScreen(
    viewModel: LoginViewModel = viewModel(),
    onProfileClick: () -> Unit = {},
    onLoginSuccess: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()

    LaunchedEffect(state.loginExitoso) {
        if (state.loginExitoso) {
            onLoginSuccess()
        }
    }

    LoginContent(
        state = state,
        onCorreoCambiado = viewModel::onCorreoCambiado,
        onContrasenaCambiada = viewModel::onContrasenaCambiada,
        onIniciarSesion = viewModel::iniciarSesion,
        onProfileClick = onProfileClick
    )
}

@Composable
fun LoginContent(
    state: LoginState,
    onCorreoCambiado: (String) -> Unit,
    onContrasenaCambiada: (String) -> Unit,
    onIniciarSesion: () -> Unit,
    onProfileClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        modifier = modifier,

    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Logo GYM
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surfaceVariant),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(id = R.string.gym_logo_text),
                    style = MaterialTheme.typography.displayLarge.copy(
                        color = JaguarTeal,
                        fontWeight = FontWeight.Black
                    )
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Titles
            Text(
                text = stringResource(id = R.string.gym_title),
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onBackground
            )

            Text(
                text = stringResource(id = R.string.exclusive_access),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 8.dp)
            )

            Spacer(modifier = Modifier.height(48.dp))

            // Correo Input
            Text(
                text = stringResource(id = R.string.correo_label),
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier
                    .align(Alignment.Start)
                    .padding(bottom = 8.dp)
            )
            OutlinedTextField(
                value = state.correo,
                onValueChange = onCorreoCambiado,
                placeholder = {
                    Text(
                        text = stringResource(id = R.string.correo_placeholder),
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                    focusedBorderColor = MaterialTheme.colorScheme.outline,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                    focusedTextColor = MaterialTheme.colorScheme.onBackground,
                    unfocusedTextColor = MaterialTheme.colorScheme.onBackground
                ),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Contrasena Input
            Text(
                text = stringResource(id = R.string.contrasena_label),
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier
                    .align(Alignment.Start)
                    .padding(bottom = 8.dp)
            )
            OutlinedTextField(
                value = state.contrasena,
                onValueChange = onContrasenaCambiada,
                placeholder = {
                    Text(
                        text = stringResource(id = R.string.contrasena_placeholder),
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                    focusedBorderColor = MaterialTheme.colorScheme.outline,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                    focusedTextColor = MaterialTheme.colorScheme.onBackground,
                    unfocusedTextColor = MaterialTheme.colorScheme.onBackground
                ),
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
            )

            if (state.error != null) {
                Text(
                    text = state.error,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier
                        .align(Alignment.Start)
                        .padding(top = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Login Button
            Button(
                onClick = onIniciarSesion,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = JaguarTeal,
                    contentColor = Color.Black
                ),
                enabled = !state.cargando
            ) {
                if (state.cargando) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = Color.Black
                    )
                } else {
                    Text(
                        text = stringResource(id = R.string.entrar_button),
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    JaguarTrackerTheme {
        LoginContent(
            state = LoginState(),
            onCorreoCambiado = {},
            onContrasenaCambiada = {},
            onIniciarSesion = {},
            onProfileClick = {}
        )
    }
}
@Composable
fun CheckingSessionScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(color = JaguarTeal)
    }
}