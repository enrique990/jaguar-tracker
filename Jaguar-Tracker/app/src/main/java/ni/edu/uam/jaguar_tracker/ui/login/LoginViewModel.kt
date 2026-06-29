package ni.edu.uam.jaguar_tracker.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ni.edu.uam.jaguar_tracker.data.repository.UserSessionRepository
import ni.edu.uam.jaguar_tracker.data.repository.UsuarioRepository

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
            _uiState.update { it.copy(error = "Debes ingresar tu correo") }
            return
        }

        if (!correoEsValido(correoActual)) {
            _uiState.update { it.copy(error = "Solo se permiten correos institucionales @uamv.edu.ni") }
            return
        }

        if (contrasenaActual.isBlank()) {
            _uiState.update { it.copy(error = "Debes ingresar tu contraseña") }
            return
        }

        if (contrasenaActual.length < 6) {
            _uiState.update { it.copy(error = "La contraseña debe tener mínimo 6 caracteres") }
            return
        }

        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    cargando = true,
                    error = null
                )
            }

            try {
                val usuario = UsuarioRepository.iniciarSesion(
                    correo = correoActual,
                    contrasenia = contrasenaActual
                )

                val idUsuario = usuario.idUsuario
                    ?: throw Exception("El backend no devolvió idUsuario")

                UserSessionRepository.guardarSesion(
                    idUsuario = idUsuario,
                    correo = usuario.correo ?: correoActual
                )

                _uiState.update {
                    it.copy(
                        cargando = false,
                        loginExitoso = true,
                        error = null
                    )
                }

            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        cargando = false,
                        loginExitoso = false,
                        error = e.message ?: "No se pudo iniciar sesión"
                    )
                }
            }
        }
    }

    private fun correoEsValido(correo: String): Boolean {
        return correo.endsWith("@uamv.edu.ni", ignoreCase = true)
    }
}