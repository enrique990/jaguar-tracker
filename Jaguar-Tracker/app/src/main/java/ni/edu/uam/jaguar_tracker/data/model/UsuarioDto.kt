package ni.edu.uam.jaguar_tracker.data.model

data class UsuarioDto(
    val idUsuario: Int? = null,
    val correo: String? = null,
    val cif: String? = null,
    val contrasenia: String? = null,
    val sexo: Boolean? = null,
    val notificacionesActivas: Boolean? = null,
    val fechaCreacion: String? = null
)