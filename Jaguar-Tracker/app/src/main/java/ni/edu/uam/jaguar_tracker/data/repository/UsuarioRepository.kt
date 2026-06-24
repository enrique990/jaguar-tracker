package ni.edu.uam.jaguar_tracker.data.repository

import ni.edu.uam.jaguar_tracker.data.model.UsuarioDto
import ni.edu.uam.jaguar_tracker.data.remote.RetrofitClient

object UsuarioRepository {

    suspend fun iniciarSesion(
        correo: String,
        contrasenia: String
    ): UsuarioDto {
        val usuarios = RetrofitClient.apiService.obtenerUsuarios()

        return usuarios.firstOrNull { usuario ->
            usuario.correo.equals(correo, ignoreCase = true) &&
                    usuario.contrasenia == contrasenia
        } ?: throw Exception("Correo o contraseña incorrectos")
    }
}