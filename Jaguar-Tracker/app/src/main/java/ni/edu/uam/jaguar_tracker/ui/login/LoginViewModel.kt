package ni.edu.uam.jaguar_tracker.ui.login

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

// Data Class -> Agrupa los datos relacionados a la interfaz. Es inmutable.
data class LoginState(
    val correo: String = "",
    val contrasena: String = "",
    val cargando: Boolean = false,
    val error: String? = null
)

// Herencia -> Extendemos de ViewModel para sobrevivir a cambios de configuración.
class LoginViewModel : ViewModel() {

    // Encapsulamiento -> El estado real es privado y solo se modifica aquí adentro.
    private val _uiState = MutableStateFlow(LoginState())

    // Estado público de solo lectura para que la Vista lo observe.
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

        // Validaciones UX básicas antes de enviar datos al Backend de Harry
        if (correoActual.isBlank() || contrasenaActual.isBlank()) {
            _uiState.update { it.copy(error = "Los campos no pueden estar vacíos") }
            return
        }

        // Cambiamos el estado a cargando. Andrés conectará aquí la lógica de Retrofit luego.
        _uiState.update { it.copy(cargando = true, error = null) }
    }
}