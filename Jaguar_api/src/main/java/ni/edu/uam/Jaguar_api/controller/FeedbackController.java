package ni.edu.uam.Jaguar_api.controller;

import ni.edu.uam.Jaguar_api.entity.Feedback;
import ni.edu.uam.Jaguar_api.service.FeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/feedbacks")
public class FeedbackController {

    @Autowired
    private FeedbackService feedbackService;

    @GetMapping
    public List<Feedback> listar() {
        return feedbackService.obtenerTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Feedback> obtenerPorId(@PathVariable Long id) {
        Feedback feedback = feedbackService.obtenerPorId(id);
        if (feedback != null) {
            return ResponseEntity.ok(feedback);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public Feedback crear(@RequestBody Feedback feedback) {
        return feedbackService.guardar(feedback);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Feedback> actualizar(@PathVariable Long id, @RequestBody Feedback feedback) {
        Feedback existente = feedbackService.obtenerPorId(id);
        if (existente != null) {
            feedback.setIdFeedback(id);
            return ResponseEntity.ok(feedbackService.guardar(feedback));
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        feedbackService.eliminar(id);
    }
}
