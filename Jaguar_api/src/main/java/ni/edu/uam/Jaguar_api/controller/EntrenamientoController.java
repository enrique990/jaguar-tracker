package ni.edu.uam.Jaguar_api.controller;

import ni.edu.uam.Jaguar_api.entity.Entrenamiento;
import ni.edu.uam.Jaguar_api.service.EntrenamientoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/entrenamientos")
public class EntrenamientoController {

    @Autowired
    private EntrenamientoService entrenamientoService;

    @GetMapping
    public List<Entrenamiento> listar() {
        return entrenamientoService.obtenerTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Entrenamiento> obtenerPorId(@PathVariable Long id) {
        Entrenamiento entrenamiento = entrenamientoService.obtenerPorId(id);
        if (entrenamiento != null) {
            return ResponseEntity.ok(entrenamiento);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public Entrenamiento crear(@RequestBody Entrenamiento entrenamiento) {
        return entrenamientoService.guardar(entrenamiento);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Entrenamiento> actualizar(@PathVariable Long id, @RequestBody Entrenamiento entrenamiento) {
        Entrenamiento existente = entrenamientoService.obtenerPorId(id);
        if (existente != null) {
            entrenamiento.setIdEntrenamiento(id);
            return ResponseEntity.ok(entrenamientoService.guardar(entrenamiento));
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        entrenamientoService.eliminar(id);
    }
}
