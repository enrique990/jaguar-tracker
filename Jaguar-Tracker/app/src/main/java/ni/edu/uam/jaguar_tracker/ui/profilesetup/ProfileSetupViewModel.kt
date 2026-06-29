package ni.edu.uam.jaguar_tracker.ui.profilesetup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ni.edu.uam.jaguar_tracker.data.model.PesoUsuarioRequestDto
import ni.edu.uam.jaguar_tracker.data.model.UsuarioRefDto
import ni.edu.uam.jaguar_tracker.data.remote.RetrofitClient
import ni.edu.uam.jaguar_tracker.data.repository.UserSessionRepository
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class ProfileSetupUiState(
    val weight: String = "70",
    val selectedGender: String = "Masculino",
    val isSaving: Boolean = false,
    val error: String? = null,
    val savedSuccessfully: Boolean = false
)

class ProfileSetupViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileSetupUiState())
    val uiState: StateFlow<ProfileSetupUiState> = _uiState.asStateFlow()

    fun onWeightChanged(value: String) {
        _uiState.update {
            it.copy(
                weight = value,
                error = null
            )
        }
    }

    fun onGenderChanged(value: String) {
        _uiState.update {
            it.copy(selectedGender = value)
        }
    }

    fun guardarPeso() {
        val currentState = _uiState.value
        val peso = currentState.weight.replace(",", ".").toDoubleOrNull()

        if (peso == null || peso <= 0.0) {
            _uiState.update {
                it.copy(error = "Ingresá un peso válido")
            }
            return
        }

        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isSaving = true,
                    error = null
                )
            }

            try {
                val idUsuario = UserSessionRepository.requerirIdUsuarioActual()

                RetrofitClient.apiService.crearPesoUsuario(
                    PesoUsuarioRequestDto(
                        usuario = UsuarioRefDto(idUsuario = idUsuario),
                        peso = peso,
                        fechaRegistro = fechaActualBackend()
                    )
                )

                _uiState.update {
                    it.copy(
                        isSaving = false,
                        savedSuccessfully = true,
                        error = null
                    )
                }

            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isSaving = false,
                        error = "No se pudo guardar el peso: ${e.message ?: "Error desconocido"}"
                    )
                }
            }
        }
    }

    private fun fechaActualBackend(): String {
        val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
        return formatter.format(Date())
    }
}