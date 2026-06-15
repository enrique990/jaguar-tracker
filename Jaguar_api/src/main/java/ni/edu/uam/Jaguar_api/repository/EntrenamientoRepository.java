package ni.edu.uam.Jaguar_api.repository;

import ni.edu.uam.Jaguar_api.entity.Entrenamiento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EntrenamientoRepository extends JpaRepository<Entrenamiento, Long> {
}
