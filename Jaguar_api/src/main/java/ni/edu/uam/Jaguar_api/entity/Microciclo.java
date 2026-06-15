package ni.edu.uam.Jaguar_api.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "microciclo")
@Data
public class Microciclo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_microciclo")
    private Long idMicrociclo;

    @ManyToOne
    @JoinColumn(name = "id_rutina", nullable = false)
    private Rutina rutina;

    @Column(name = "numero_microciclo")
    private Integer numeroMicrociclo;

    private String intensidad;

    private String volumen;

    @OneToMany(mappedBy = "microciclo", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<MicrocicloEjercicio> microcicloEjercicios;

    @OneToMany(mappedBy = "microciclo", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Entrenamiento> entrenamientos;
}
