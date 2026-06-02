package ni.edu.uam.jaguar_tracker.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import ni.edu.uam.jaguar_tracker.ui.theme.JaguarTrackerTheme
import androidx.lifecycle.viewmodel.compose.viewModel

// Composición -> Construimos la vista ensamblando funciones más pequeñas.
@Composable
fun LoginScreen(
    viewModel: LoginViewModel = viewModel()
) {
    // Observamos el flujo de datos encapsulado
    val state by viewModel.uiState.collectAsState()

    LoginContent(
        state = state,
        onCorreoCambiado = viewModel::onCorreoCambiado,
        onContrasenaCambiada = viewModel::onContrasenaCambiada,
        onIniciarSesion = viewModel::iniciarSesion
    )
}

@Composable
fun LoginContent(
    state: LoginState,
    onCorreoCambiado: (String) -> Unit,
    onContrasenaCambiada: (String) -> Unit,
    onIniciarSesion: () -> Unit
) {
    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Jaguar Tracker",
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(bottom = 32.dp)
            )

            // Ley de Proximidad: Inputs agrupados cerca del botón
            OutlinedTextField(
                value = state.correo,
                onValueChange = onCorreoCambiado,
                label = { Text("Correo Electrónico") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                isError = state.error != null
            )

            OutlinedTextField(
                value = state.contrasena,
                onValueChange = onContrasenaCambiada,
                label = { Text("Contraseña") },
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                isError = state.error != null
            )

            // Espacio dinámico para mensajes de error (UX)
            if (state.error != null) {
                Text(
                    text = state.error,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier
                        .align(Alignment.Start)
                        .padding(bottom = 16.dp)
                )
            } else {
                Spacer(modifier = Modifier.height(16.dp))
            }

            Button(
                onClick = onIniciarSesion,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                enabled = !state.cargando
            ) {
                if (state.cargando) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Text("Iniciar Sesión")
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
            onIniciarSesion = {}
        )
    }
}