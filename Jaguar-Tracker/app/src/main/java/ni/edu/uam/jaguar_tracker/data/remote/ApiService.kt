package ni.edu.uam.jaguar_tracker.data.remote

import ni.edu.uam.jaguar_tracker.data.model.EjercicioDto
import ni.edu.uam.jaguar_tracker.data.model.RutinaRequestDto
import ni.edu.uam.jaguar_tracker.data.model.RutinaResponseDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import ni.edu.uam.jaguar_tracker.data.model.RutinaDiaRequestDto
import ni.edu.uam.jaguar_tracker.data.model.RutinaDiaResponseDto
import ni.edu.uam.jaguar_tracker.data.model.RutinaEjercicioRequestDto
import ni.edu.uam.jaguar_tracker.data.model.RutinaEjercicioResponseDto
import ni.edu.uam.jaguar_tracker.data.model.MicrocicloEjercicioRequestDto
import ni.edu.uam.jaguar_tracker.data.model.MicrocicloEjercicioResponseDto
import ni.edu.uam.jaguar_tracker.data.model.MicrocicloRequestDto
import ni.edu.uam.jaguar_tracker.data.model.MicrocicloResponseDto

interface ApiService {

    @GET("api/ejercicios")
    suspend fun obtenerEjercicios(): List<EjercicioDto>

    @POST("api/rutinas")
    suspend fun crearRutina(
        @Body rutina: RutinaRequestDto
    ): RutinaResponseDto

    @POST("api/rutina-dias")
    suspend fun crearRutinaDia(
        @Body rutinaDia: RutinaDiaRequestDto
    ): RutinaDiaResponseDto

    @POST("api/rutina-ejercicios")
    suspend fun crearRutinaEjercicio(
        @Body rutinaEjercicio: RutinaEjercicioRequestDto
    ): RutinaEjercicioResponseDto

    @POST("api/microciclos")
    suspend fun crearMicrociclo(
        @Body microciclo: MicrocicloRequestDto
    ): MicrocicloResponseDto

    @POST("api/microciclo-ejercicios")
    suspend fun crearMicrocicloEjercicio(
        @Body microcicloEjercicio: MicrocicloEjercicioRequestDto
    ): MicrocicloEjercicioResponseDto
}