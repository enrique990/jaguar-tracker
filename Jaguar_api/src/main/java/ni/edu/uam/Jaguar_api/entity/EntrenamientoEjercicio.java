package ni.edu.uam.Jaguar_api.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "entrenamiento_ejercicio")
@Data
public class EntrenamientoEjercicio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_entrenamiento_ejercicio")
    private Long idEntrenamientoEjercicio;

    @ManyToOne
    @JoinColumn(name = "id_entrenamiento", nullable = false)
    private Entrenamiento entrenamiento;

    @ManyToOne
    @JoinColumn(name = "id_microciclo_ejercicio", nullable = false)
    private MicrocicloEjercicio microcicloEjercicio;

    @OneToMany(mappedBy = "entrenamientoEjercicio", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<SerieRealizada> seriesRealizadas;
}
