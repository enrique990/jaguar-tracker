package ni.edu.uam.jaguar_tracker.data.local.session;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "usuario_sesion")
public class UserSessionEntity {

    @PrimaryKey
    public int id;

    public int idUsuario;

    @NonNull
    public String correo;

    public long fechaLogin;

    public UserSessionEntity(
            int id,
            int idUsuario,
            @NonNull String correo,
            long fechaLogin
    ) {
        this.id = id;
        this.idUsuario = idUsuario;
        this.correo = correo;
        this.fechaLogin = fechaLogin;
    }
}