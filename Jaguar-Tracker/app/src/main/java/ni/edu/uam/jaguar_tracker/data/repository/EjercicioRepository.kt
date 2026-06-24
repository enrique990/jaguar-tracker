package ni.edu.uam.jaguar_tracker.data.repository

import ni.edu.uam.jaguar_tracker.data.model.EjercicioDto
import ni.edu.uam.jaguar_tracker.data.remote.RetrofitClient

class EjercicioRepository {

    suspend fun obtenerEjercicios(): List<EjercicioDto> {
        return RetrofitClient.apiService.obtenerEjercicios()
    }
}