package ni.edu.uam.Jaguar_api.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "ejercicio")
@Data
public class Ejercicio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_ejercicio")
    private Long idEjercicio;

    private String nombre;

    private String foto;

    @Column(name = "serie_recomendadas")
    private Integer serieRecomendadas;

    @Column(name = "repeticiones_recomendadas")
    private Integer repeticionesRecomendadas;

    @OneToMany(mappedBy = "ejercicio", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<RutinaEjercicio> rutinaEjercicios;
}
