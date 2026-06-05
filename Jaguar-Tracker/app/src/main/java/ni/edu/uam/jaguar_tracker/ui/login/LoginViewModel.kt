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
        _uiState.update {
            it.copy(
                correo = nuevoCorreo,
                error = null,
                loginExitoso = false
            )
        }
    }

    fun onContrasenaCambiada(nuevaContrasena: String) {
        _uiState.update {
            it.copy(
                contrasena = nuevaContrasena,
                error = null,
                loginExitoso = false
            )
        }
    }

    fun iniciarSesion() {
        val currentState = _uiState.value

        if (currentState.cargando) return

        val correoActual = currentState.correo.trim()
        val contrasenaActual = currentState.contrasena

        if (correoActual.isBlank()) {
            _uiState.update {
                it.copy(error = "Debes ingresar tu correo")
            }
            return
        }

        if (!correoEsValido(correoActual)) {
            _uiState.update {
                it.copy(error = "Ingresa un correo válido")
            }
            return
        }

        if (contrasenaActual.isBlank()) {
            _uiState.update {
                it.copy(error = "Debes ingresar tu contraseña")
            }
            return
        }

        if (contrasenaActual.length < 6) {
            _uiState.update {
                it.copy(error = "La contraseña debe tener mínimo 6 caracteres")
            }
            return
        }

        _uiState.update {
            it.copy(
                cargando = true,
                error = null
            )
        }

        // Login simulado hasta conectar con backend/API.
        _uiState.update {
            it.copy(
                cargando = false,
                loginExitoso = true
            )
        }
    }

    private fun correoEsValido(correo: String): Boolean {
        val correoRegex = Regex(
            pattern = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
        )

        return correoRegex.matches(correo)
    }
}