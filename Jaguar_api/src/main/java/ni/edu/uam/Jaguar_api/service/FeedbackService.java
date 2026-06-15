package ni.edu.uam.Jaguar_api.service;

import ni.edu.uam.Jaguar_api.entity.Feedback;
import ni.edu.uam.Jaguar_api.repository.FeedbackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class FeedbackService {

    @Autowired
    private FeedbackRepository feedbackRepository;

    public List<Feedback> obtenerTodos() {
        return feedbackRepository.findAll();
    }

    public Feedback obtenerPorId(Long id) {
        return feedbackRepository.findById(id).orElse(null);
    }

    public Feedback guardar(Feedback feedback) {
        return feedbackRepository.save(feedback);
    }

    public void eliminar(Long id) {
        feedbackRepository.deleteById(id);
    }
}
