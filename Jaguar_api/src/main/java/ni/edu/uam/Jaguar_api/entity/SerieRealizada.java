package ni.edu.uam.Jaguar_api.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "serie_realizada")
@Data
public class SerieRealizada {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_serie_realizada")
    private Long idSerieRealizada;

    @ManyToOne
    @JoinColumn(name = "id_entrenamiento_ejercicio", nullable = false)
    private EntrenamientoEjercicio entrenamientoEjercicio;

    @Column(name = "numero_serie")
    private Integer numeroSerie;

    private Float peso;

    private Integer repeticiones;

    private Integer rir;

    @Column(name = "unidad_medida")
    private String unidadMedida;
}
