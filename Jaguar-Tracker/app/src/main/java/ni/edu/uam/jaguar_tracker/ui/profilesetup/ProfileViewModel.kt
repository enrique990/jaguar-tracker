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

data class ProfileUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val successMessage: String? = null,

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

    val calculatorExercise: String = "Press de Banca",
    val calculatorWeight: String = "100",
    val calculatorReps: String = "8",
    val calculatorBodyWeight: String = "",
    val calculatorEstimatedOneRm: String = "-",
    val calculatorRelativeStrength: String = "-",
    val calculatorCategory: String = "Ingresá los datos y calculá tu fuerza relativa."
)

class ProfileViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    init {
        cargarPerfil()
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

                val pesoCorporal = ultimoPeso?.peso
                val pesoCorporalTexto = if (pesoCorporal != null) {
                    formatNumber(pesoCorporal)
                } else {
                    ""
                }

                _uiState.update { current ->
                    current.copy(
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
                        diasActivos = diasActivos.toString(),
                        calculatorBodyWeight = if (current.calculatorBodyWeight.isBlank()) {
                            pesoCorporalTexto
                        } else {
                            current.calculatorBodyWeight
                        }
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
                        successMessage = "Peso actualizado correctamente",
                        calculatorBodyWeight = formatNumber(peso)
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

    fun onCalculatorWeightChanged(value: String) {
        _uiState.update {
            it.copy(
                calculatorWeight = value,
                calculatorEstimatedOneRm = "-",
                calculatorRelativeStrength = "-",
                calculatorCategory = "Ingresá los datos y calculá tu fuerza relativa."
            )
        }
    }

    fun onCalculatorRepsChanged(value: String) {
        _uiState.update {
            it.copy(
                calculatorReps = value,
                calculatorEstimatedOneRm = "-",
                calculatorRelativeStrength = "-",
                calculatorCategory = "Ingresá los datos y calculá tu fuerza relativa."
            )
        }
    }

    fun onCalculatorBodyWeightChanged(value: String) {
        _uiState.update {
            it.copy(
                calculatorBodyWeight = value,
                calculatorEstimatedOneRm = "-",
                calculatorRelativeStrength = "-",
                calculatorCategory = "Ingresá los datos y calculá tu fuerza relativa."
            )
        }
    }

    fun calcularFuerzaRelativa() {
        val state = _uiState.value

        val pesoLevantado = state.calculatorWeight.replace(",", ".").toDoubleOrNull()
        val repeticiones = state.calculatorReps.toIntOrNull()
        val pesoCorporal = state.calculatorBodyWeight.replace(",", ".").toDoubleOrNull()

        if (pesoLevantado == null || pesoLevantado <= 0.0) {
            _uiState.update {
                it.copy(calculatorCategory = "Ingresá un peso levantado válido.")
            }
            return
        }

        if (repeticiones == null || repeticiones <= 0) {
            _uiState.update {
                it.copy(calculatorCategory = "Ingresá una cantidad de repeticiones válida.")
            }
            return
        }

        if (pesoCorporal == null || pesoCorporal <= 0.0) {
            _uiState.update {
                it.copy(calculatorCategory = "Ingresá un peso corporal válido.")
            }
            return
        }

        val oneRmEstimado = pesoLevantado * (1 + repeticiones / 30.0)
        val fuerzaRelativa = oneRmEstimado / pesoCorporal

        val categoria = when {
            fuerzaRelativa < 1.0 -> "Nivel inicial"
            fuerzaRelativa < 1.5 -> "Nivel intermedio"
            fuerzaRelativa < 2.0 -> "Nivel avanzado"
            else -> "Nivel excelente"
        }

        _uiState.update {
            it.copy(
                calculatorEstimatedOneRm = "${formatNumber(oneRmEstimado)} kg",
                calculatorRelativeStrength = "${formatNumberTwoDecimals(fuerzaRelativa)}x",
                calculatorCategory = categoria
            )
        }
    }

    private fun fechaActualBackend(): String {
        val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
        return formatter.format(Date())
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

    private fun formatNumberTwoDecimals(value: Double): String {
        return String.format(Locale.US, "%.2f", value)
    }
}