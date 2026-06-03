package ni.edu.uam.jaguar_tracker.ui.login

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class LoginState(
    val correo: String = "",
    val contrasena: String = "",
    val cargando: Boolean = false,
    val error: String? = null,
    val loginExitoso: Boolean = false,
)

class LoginViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(LoginState())
    val uiState: StateFlow<LoginState> = _uiState.asStateFlow()

    fun onCorreoCambiado(nuevoCorreo: String) {
        _uiState.update { it.copy(correo = nuevoCorreo, error = null) }
    }

    fun onContrasenaCambiada(nuevaContrasena: String) {
        _uiState.update { it.copy(contrasena = nuevaContrasena, error = null) }
    }

    fun iniciarSesion() {
        val correoActual = _uiState.value.correo
        val contrasenaActual = _uiState.value.contrasena

        if (correoActual.isBlank() || contrasenaActual.isBlank()) {
            _uiState.update { it.copy(error = "Los campos no pueden estar vacíos") }
            return
        }

        _uiState.update { it.copy(cargando = true, error = null) }

        // Simulación de éxito
        _uiState.update { it.copy(cargando = false, loginExitoso = true) }
    }
}