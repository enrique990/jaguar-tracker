package ni.edu.uam.Jaguar_api.repository;

import ni.edu.uam.Jaguar_api.entity.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
}
