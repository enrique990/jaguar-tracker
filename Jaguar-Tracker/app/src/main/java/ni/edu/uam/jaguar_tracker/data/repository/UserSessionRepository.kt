package ni.edu.uam.jaguar_tracker.data.repository

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ni.edu.uam.jaguar_tracker.data.local.DatabaseProvider
import ni.edu.uam.jaguar_tracker.data.local.session.UserSessionDao
import ni.edu.uam.jaguar_tracker.data.local.session.UserSessionEntity

object UserSessionRepository {

    private var dao: UserSessionDao? = null

    var currentUserId: Int? = null
        private set

    var currentUserEmail: String? = null
        private set

    fun init(context: Context) {
        dao = DatabaseProvider.getDatabase(context).userSessionDao()
    }

    suspend fun cargarSesion(): UserSessionEntity? {
        return withContext(Dispatchers.IO) {
            val session = dao?.obtenerSesion()

            currentUserId = session?.idUsuario
            currentUserEmail = session?.correo

            session
        }
    }

    suspend fun guardarSesion(
        idUsuario: Int,
        correo: String
    ) {
        withContext(Dispatchers.IO) {
            val session = UserSessionEntity(
                1,
                idUsuario,
                correo,
                System.currentTimeMillis()
            )

            dao?.guardarSesion(session)

            currentUserId = idUsuario
            currentUserEmail = correo
        }
    }

    suspend fun cerrarSesion() {
        withContext(Dispatchers.IO) {
            dao?.cerrarSesion()
            currentUserId = null
            currentUserEmail = null
        }
    }

    suspend fun obtenerIdUsuarioActual(): Int? {
        currentUserId?.let { return it }

        val session = cargarSesion()
        return session?.idUsuario
    }

    suspend fun requerirIdUsuarioActual(): Int {
        return obtenerIdUsuarioActual()
            ?: throw Exception("No hay usuario con sesión activa")
    }
}