package ni.edu.uam.Jaguar_api.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "entrenamiento")
@Data
public class Entrenamiento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_entrenamiento")
    private Long idEntrenamiento;

    @ManyToOne
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "id_rutina", nullable = false)
    private Rutina rutina;

    @ManyToOne
    @JoinColumn(name = "id_microciclo", nullable = false)
    private Microciclo microciclo;

    @Column(name = "fecha_entrenamiento")
    private LocalDateTime fechaEntrenamiento;

    private Boolean completado;

    @OneToMany(mappedBy = "entrenamiento", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<EntrenamientoEjercicio> entrenamientoEjercicios;
}
