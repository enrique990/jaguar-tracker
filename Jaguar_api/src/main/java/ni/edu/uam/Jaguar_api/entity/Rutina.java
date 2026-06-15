package ni.edu.uam.Jaguar_api.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "rutina")
@Data
public class Rutina {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_rutina")
    private Long idRutina;

    @ManyToOne
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    private String nombre;

    @Column(name = "usa_microciclos")
    private Boolean usaMicrociclos;

    @Column(name = "cantidad_microciclos")
    private Integer cantidadMicrociclos;

    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion;

    @OneToMany(mappedBy = "rutina", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<RutinaDia> rutinaDias;

    @OneToMany(mappedBy = "rutina", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<RutinaEjercicio> rutinaEjercicios;

    @OneToMany(mappedBy = "rutina", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Microciclo> microciclos;

    @OneToMany(mappedBy = "rutina", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Entrenamiento> entrenamientos;
}
