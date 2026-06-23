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
}