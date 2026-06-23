package ni.edu.uam.jaguar_tracker.data.remote

import ni.edu.uam.jaguar_tracker.data.model.EjercicioDto
import ni.edu.uam.jaguar_tracker.data.model.RutinaRequestDto
import ni.edu.uam.jaguar_tracker.data.model.RutinaResponseDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {

    @GET("api/ejercicios")
    suspend fun obtenerEjercicios(): List<EjercicioDto>

    @POST("api/rutinas")
    suspend fun crearRutina(
        @Body rutina: RutinaRequestDto
    ): RutinaResponseDto
}