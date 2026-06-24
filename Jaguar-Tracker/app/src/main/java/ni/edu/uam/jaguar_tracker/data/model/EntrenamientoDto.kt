package ni.edu.uam.jaguar_tracker.data.model

data class EntrenamientoRequestDto(
    val usuario: UsuarioRefDto,
    val rutina: RutinaRefDto,
    val microciclo: MicrocicloRefDto,
    val fechaEntrenamiento: String,
    val completado: Boolean
)

data class EntrenamientoResponseDto(
    val idEntrenamiento: Int? = null,
    val usuario: UsuarioRefDto? = null,
    val rutina: RutinaResponseDto? = null,
    val microciclo: MicrocicloResponseDto? = null,
    val fechaEntrenamiento: String? = null,
    val completado: Boolean? = null
)

data class EntrenamientoRefDto(
    val idEntrenamiento: Int
)

data class MicrocicloEjercicioRefDto(
    val idMicrocicloEjercicio: Int
)

data class EntrenamientoEjercicioRequestDto(
    val entrenamiento: EntrenamientoRefDto,
    val microcicloEjercicio: MicrocicloEjercicioRefDto
)

data class EntrenamientoEjercicioResponseDto(
    val idEntrenamientoEjercicio: Int? = null,
    val entrenamiento: EntrenamientoResponseDto? = null,
    val microcicloEjercicio: MicrocicloEjercicioResponseDto? = null
)

data class EntrenamientoEjercicioRefDto(
    val idEntrenamientoEjercicio: Int
)

data class SerieRealizadaRequestDto(
    val entrenamientoEjercicio: EntrenamientoEjercicioRefDto,
    val numeroSerie: Int,
    val peso: Double,
    val repeticiones: Int,
    val rir: Int,
    val unidadMedida: String
)

data class SerieRealizadaResponseDto(
    val idSerieRealizada: Int? = null,
    val entrenamientoEjercicio: EntrenamientoEjercicioResponseDto? = null,
    val numeroSerie: Int? = null,
    val peso: Double? = null,
    val repeticiones: Int? = null,
    val rir: Int? = null,
    val unidadMedida: String? = null
)