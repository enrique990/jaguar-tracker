package ni.edu.uam.Jaguar_api.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "microciclo_ejercicio")
@Data
public class MicrocicloEjercicio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_microciclo_ejercicio")
    private Long idMicrocicloEjercicio;

    @ManyToOne
    @JoinColumn(name = "id_microciclo", nullable = false)
    private Microciclo microciclo;

    @ManyToOne
    @JoinColumn(name = "id_rutina_ejercicio", nullable = false)
    private RutinaEjercicio rutinaEjercicio;

    private Integer series;

    private Integer repeticiones;

    private Integer rir;

    @Column(name = "descanso_segundos")
    private Integer descansoSegundos;

    @OneToMany(mappedBy = "microcicloEjercicio", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<EntrenamientoEjercicio> entrenamientoEjercicios;
}
