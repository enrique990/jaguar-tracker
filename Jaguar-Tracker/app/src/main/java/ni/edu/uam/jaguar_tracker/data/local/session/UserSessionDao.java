package ni.edu.uam.jaguar_tracker.data.local.session;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Dao
public interface UserSessionDao {

    @Query("SELECT * FROM usuario_sesion WHERE id = 1 LIMIT 1")
    UserSessionEntity obtenerSesion();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void guardarSesion(UserSessionEntity session);

    @Query("DELETE FROM usuario_sesion")
    void cerrarSesion();
}