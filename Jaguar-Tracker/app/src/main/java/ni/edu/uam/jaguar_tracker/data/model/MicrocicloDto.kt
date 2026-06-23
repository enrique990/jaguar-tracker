package ni.edu.uam.jaguar_tracker.data.model

data class MicrocicloRequestDto(
    val rutina: RutinaRefDto,
    val numeroMicrociclo: Int,
    val intensidad: String,
    val volumen: String
)

data class MicrocicloResponseDto(
    val idMicrociclo: Int? = null,
    val rutina: RutinaResponseDto? = null,
    val numeroMicrociclo: Int? = null,
    val intensidad: String? = null,
    val volumen: String? = null
)

data class MicrocicloRefDto(
    val idMicrociclo: Int
)

data class RutinaEjercicioRefDto(
    val idRutinaEjercicio: Int
)

data class MicrocicloEjercicioRequestDto(
    val microciclo: MicrocicloRefDto,
    val rutinaEjercicio: RutinaEjercicioRefDto,
    val series: Int,
    val repeticiones: Int,
    val rir: Int,
    val descansoSegundos: Int
)

data class MicrocicloEjercicioResponseDto(
    val idMicrocicloEjercicio: Int? = null,
    val microciclo: MicrocicloResponseDto? = null,
    val rutinaEjercicio: RutinaEjercicioResponseDto? = null,
    val series: Int? = null,
    val repeticiones: Int? = null,
    val rir: Int? = null,
    val descansoSegundos: Int? = null
)