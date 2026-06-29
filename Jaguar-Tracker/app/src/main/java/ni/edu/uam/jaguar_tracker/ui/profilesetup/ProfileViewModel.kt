package ni.edu.uam.jaguar_tracker.ui.profilesetup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ni.edu.uam.jaguar_tracker.data.remote.RetrofitClient
import ni.edu.uam.jaguar_tracker.data.repository.UserSessionRepository
import java.text.SimpleDateFormat
import java.util.Locale
import ni.edu.uam.jaguar_tracker.data.model.PesoUsuarioRequestDto
import ni.edu.uam.jaguar_tracker.data.model.UsuarioRefDto
import java.util.Date

data class ProfileUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val correo: String = "Usuario",
    val cif: String = "",
    val pesoActual: String = "Sin registro",
    val fechaUltimoPeso: String = "Sin registro",
    val entrenamientosCompletados: String = "0",
    val rutinasCreadas: String = "0",
    val diasActivos: String = "0",
    val showWeightDialog: Boolean = false,
    val pesoInput: String = "",
    val isSavingWeight: Boolean = false,
    val successMessage: String? = null
)

class ProfileViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    init {
        cargarPerfil()
    }
    fun abrirDialogoPeso() {
        val pesoActual = _uiState.value.pesoActual
            .replace(" kg", "")
            .takeIf { it.firstOrNull()?.isDigit() == true }
            ?: ""

        _uiState.update {
            it.copy(
                showWeightDialog = true,
                pesoInput = pesoActual,
                error = null,
                successMessage = null
            )
        }
    }

    fun cerrarDialogoPeso() {
        _uiState.update {
            it.copy(
                showWeightDialog = false,
                pesoInput = "",
                isSavingWeight = false
            )
        }
    }

    fun onPesoInputChanged(value: String) {
        _uiState.update {
            it.copy(
                pesoInput = value,
                error = null,
                successMessage = null
            )
        }
    }

    fun guardarPeso() {
        val peso = _uiState.value.pesoInput.replace(",", ".").toDoubleOrNull()

        if (peso == null || peso <= 0.0) {
            _uiState.update {
                it.copy(error = "Ingresá un peso válido")
            }
            return
        }

        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isSavingWeight = true,
                    error = null,
                    successMessage = null
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
                        showWeightDialog = false,
                        pesoInput = "",
                        isSavingWeight = false,
                        successMessage = "Peso actualizado correctamente"
                    )
                }

                cargarPerfil()

            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isSavingWeight = false,
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
    fun cargarPerfil() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(isLoading = true, error = null)
            }

            try {
                val idUsuario = UserSessionRepository.requerirIdUsuarioActual()
                val sesion = UserSessionRepository.cargarSesion()

                val usuarios = RetrofitClient.apiService.obtenerUsuarios()
                val usuarioActual = usuarios.firstOrNull { usuario ->
                    usuario.idUsuario == idUsuario
                }

                val pesosUsuario = RetrofitClient.apiService.obtenerPesosUsuarios()
                    .filter { peso ->
                        peso.usuario?.idUsuario == idUsuario
                    }
                    .sortedByDescending { peso ->
                        peso.fechaRegistro ?: ""
                    }

                val ultimoPeso = pesosUsuario.firstOrNull()

                val entrenamientos = try {
                    RetrofitClient.apiService.obtenerEntrenamientos()
                        .filter { entrenamiento ->
                            entrenamiento.usuario?.idUsuario == idUsuario &&
                                    entrenamiento.completado == true
                        }
                } catch (e: Exception) {
                    emptyList()
                }

                val rutinas = try {
                    RetrofitClient.apiService.obtenerRutinas()
                        .filter { rutina ->
                            rutina.usuario?.idUsuario == idUsuario
                        }
                } catch (e: Exception) {
                    emptyList()
                }

                val diasActivos = entrenamientos
                    .mapNotNull { it.fechaEntrenamiento?.substringBefore("T") }
                    .distinct()
                    .size

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = null,
                        correo = usuarioActual?.correo ?: sesion?.correo ?: "Usuario",
                        cif = usuarioActual?.cif ?: "",
                        pesoActual = if (ultimoPeso?.peso != null) {
                            "${formatNumber(ultimoPeso.peso)} kg"
                        } else {
                            "Sin registro"
                        },
                        fechaUltimoPeso = formatDate(ultimoPeso?.fechaRegistro),
                        entrenamientosCompletados = entrenamientos.size.toString(),
                        rutinasCreadas = rutinas.size.toString(),
                        diasActivos = diasActivos.toString()
                    )
                }

            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = "No se pudo cargar el perfil: ${e.message ?: "Error desconocido"}"
                    )
                }
            }
        }
    }

    private fun formatDate(rawDate: String?): String {
        if (rawDate.isNullOrBlank()) return "Sin registro"

        return try {
            val input = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
            val output = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val date = input.parse(rawDate)
            if (date != null) output.format(date) else rawDate
        } catch (e: Exception) {
            rawDate.substringBefore("T")
        }
    }

    private fun formatNumber(value: Double): String {
        return if (value % 1.0 == 0.0) {
            value.toInt().toString()
        } else {
            String.format(Locale.US, "%.1f", value)
        }
    }
}