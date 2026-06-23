package ni.edu.uam.jaguar_tracker.data.model

data class RutinaRefDto(
    val idRutina: Int
)

data class EjercicioRefDto(
    val idEjercicio: Int
)

data class RutinaDiaRequestDto(
    val rutina: RutinaRefDto,
    val diaSemana: String
)

data class RutinaDiaResponseDto(
    val idRutinaDia: Int? = null,
    val diaSemana: String? = null,
    val rutina: RutinaResponseDto? = null
)

data class RutinaEjercicioRequestDto(
    val rutina: RutinaRefDto,
    val ejercicio: EjercicioRefDto,
    val orden: Int
)

data class RutinaEjercicioResponseDto(
    val idRutinaEjercicio: Int? = null,
    val orden: Int? = null
)