package ni.edu.uam.Jaguar_api.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "rutina_dia")
@Data
public class RutinaDia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_rutina_dia")
    private Long idRutinaDia;

    @ManyToOne
    @JoinColumn(name = "id_rutina", nullable = false)
    private Rutina rutina;

    @Column(name = "dia_semana")
    private String diaSemana;
}
