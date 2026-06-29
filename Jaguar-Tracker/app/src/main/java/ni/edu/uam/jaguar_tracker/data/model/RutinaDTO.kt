package ni.edu.uam.jaguar_tracker.data.model

data class UsuarioRefDto(
    val idUsuario: Int
)

data class RutinaRequestDto(
    val usuario: UsuarioRefDto,
    val nombre: String,
    val usaMicrociclos: Boolean,
    val cantidadMicrociclos: Int,
    val fechaCreacion: String
)

data class RutinaResponseDto(
    val idRutina: Int? = null,
    val usuario: UsuarioRefDto? = null,
    val nombre: String? = null,
    val usaMicrociclos: Boolean? = null,
    val cantidadMicrociclos: Int? = null,
    val fechaCreacion: String? = null
)