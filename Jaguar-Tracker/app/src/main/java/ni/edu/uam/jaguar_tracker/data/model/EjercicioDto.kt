package ni.edu.uam.jaguar_tracker.data.model

data class EjercicioDto(
    val idEjercicio: Int? = null,
    val nombre: String,
    val foto: String? = null,
    val serieRecomendadas: Int,
    val repeticionesRecomendadas: Int
)