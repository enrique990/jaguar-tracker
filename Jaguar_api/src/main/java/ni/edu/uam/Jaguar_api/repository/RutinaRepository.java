package ni.edu.uam.Jaguar_api.repository;

import ni.edu.uam.Jaguar_api.entity.Rutina;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RutinaRepository extends JpaRepository<Rutina, Long> {
}
