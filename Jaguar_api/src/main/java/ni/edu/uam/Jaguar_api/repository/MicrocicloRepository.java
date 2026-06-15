package ni.edu.uam.Jaguar_api.repository;

import ni.edu.uam.Jaguar_api.entity.Microciclo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MicrocicloRepository extends JpaRepository<Microciclo, Long> {
}
