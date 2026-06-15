package ni.edu.uam.Jaguar_api.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "peso_usuario")
@Data
public class PesoUsuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_peso")
    private Long idPeso;

    @ManyToOne
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    private Float peso;

    @Column(name = "fecha_registro")
    private LocalDateTime fechaRegistro;
}
