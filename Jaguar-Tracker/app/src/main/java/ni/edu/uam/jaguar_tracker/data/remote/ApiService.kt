package ni.edu.uam.jaguar_tracker.data.remote

import ni.edu.uam.jaguar_tracker.data.model.EjercicioDto
import ni.edu.uam.jaguar_tracker.data.model.MicrocicloEjercicioRequestDto
import ni.edu.uam.jaguar_tracker.data.model.MicrocicloEjercicioResponseDto
import ni.edu.uam.jaguar_tracker.data.model.MicrocicloRequestDto
import ni.edu.uam.jaguar_tracker.data.model.MicrocicloResponseDto
import ni.edu.uam.jaguar_tracker.data.model.RutinaDiaRequestDto
import ni.edu.uam.jaguar_tracker.data.model.RutinaDiaResponseDto
import ni.edu.uam.jaguar_tracker.data.model.RutinaEjercicioRequestDto
import ni.edu.uam.jaguar_tracker.data.model.RutinaEjercicioResponseDto
import ni.edu.uam.jaguar_tracker.data.model.RutinaRequestDto
import ni.edu.uam.jaguar_tracker.data.model.RutinaResponseDto
import ni.edu.uam.jaguar_tracker.data.model.EntrenamientoEjercicioRequestDto
import ni.edu.uam.jaguar_tracker.data.model.EntrenamientoEjercicioResponseDto
import ni.edu.uam.jaguar_tracker.data.model.EntrenamientoRequestDto
import ni.edu.uam.jaguar_tracker.data.model.EntrenamientoResponseDto
import ni.edu.uam.jaguar_tracker.data.model.SerieRealizadaRequestDto
import ni.edu.uam.jaguar_tracker.data.model.SerieRealizadaResponseDto
import ni.edu.uam.jaguar_tracker.data.model.UsuarioDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST


interface ApiService {

    // Ejercicios
    @GET("api/ejercicios")
    suspend fun obtenerEjercicios(): List<EjercicioDto>

    // Rutinas
    @GET("api/rutinas")
    suspend fun obtenerRutinas(): List<RutinaResponseDto>

    @POST("api/rutinas")
    suspend fun crearRutina(
        @Body rutina: RutinaRequestDto
    ): RutinaResponseDto

    // Rutina días
    @GET("api/rutina-dias")
    suspend fun obtenerRutinaDias(): List<RutinaDiaResponseDto>

    @POST("api/rutina-dias")
    suspend fun crearRutinaDia(
        @Body rutinaDia: RutinaDiaRequestDto
    ): RutinaDiaResponseDto

    // Rutina ejercicios
    @GET("api/rutina-ejercicios")
    suspend fun obtenerRutinaEjercicios(): List<RutinaEjercicioResponseDto>

    @POST("api/rutina-ejercicios")
    suspend fun crearRutinaEjercicio(
        @Body rutinaEjercicio: RutinaEjercicioRequestDto
    ): RutinaEjercicioResponseDto

    // Microciclos
    @GET("api/microciclos")
    suspend fun obtenerMicrociclos(): List<MicrocicloResponseDto>

    @POST("api/microciclos")
    suspend fun crearMicrociclo(
        @Body microciclo: MicrocicloRequestDto
    ): MicrocicloResponseDto

    // Microciclo ejercicios
    @GET("api/microciclo-ejercicios")
    suspend fun obtenerMicrocicloEjercicios(): List<MicrocicloEjercicioResponseDto>

    @POST("api/microciclo-ejercicios")
    suspend fun crearMicrocicloEjercicio(
        @Body microcicloEjercicio: MicrocicloEjercicioRequestDto
    ): MicrocicloEjercicioResponseDto

    //entrenamientos
    @POST("api/entrenamientos")
    suspend fun crearEntrenamiento(
        @Body entrenamiento: EntrenamientoRequestDto
    ): EntrenamientoResponseDto

    @POST("api/entrenamiento-ejercicios")
    suspend fun crearEntrenamientoEjercicio(
        @Body entrenamientoEjercicio: EntrenamientoEjercicioRequestDto
    ): EntrenamientoEjercicioResponseDto

    @POST("api/series-realizadas")
    suspend fun crearSerieRealizada(
        @Body serieRealizada: SerieRealizadaRequestDto
    ): SerieRealizadaResponseDto

    @GET("api/entrenamientos")
    suspend fun obtenerEntrenamientos(): List<EntrenamientoResponseDto>

    @GET("api/entrenamiento-ejercicios")
    suspend fun obtenerEntrenamientoEjercicios(): List<EntrenamientoEjercicioResponseDto>

    @GET("api/series-realizadas")
    suspend fun obtenerSeriesRealizadas(): List<SerieRealizadaResponseDto>

    @GET("api/usuarios")
    suspend fun obtenerUsuarios(): List<UsuarioDto>
}