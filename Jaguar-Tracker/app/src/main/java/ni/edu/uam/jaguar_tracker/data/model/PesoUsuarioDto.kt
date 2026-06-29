package ni.edu.uam.jaguar_tracker.data.model

data class PesoUsuarioDto(
    val idPeso: Int? = null,
    val peso: Double? = null,
    val fechaRegistro: String? = null,
    val usuario: UsuarioDto? = null
)

data class PesoUsuarioRequestDto(
    val usuario: UsuarioRefDto,
    val peso: Double,
    val fechaRegistro: String
)
