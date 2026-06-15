package ni.edu.uam.Jaguar_api.controller;

import ni.edu.uam.Jaguar_api.entity.EntrenamientoEjercicio;
import ni.edu.uam.Jaguar_api.service.EntrenamientoEjercicioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/entrenamiento-ejercicios")
public class EntrenamientoEjercicioController {

    @Autowired
    private EntrenamientoEjercicioService entrenamientoEjercicioService;

    @GetMapping
    public List<EntrenamientoEjercicio> listar() {
        return entrenamientoEjercicioService.obtenerTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntrenamientoEjercicio> obtenerPorId(@PathVariable Long id) {
        EntrenamientoEjercicio entrenamientoEjercicio = entrenamientoEjercicioService.obtenerPorId(id);
        if (entrenamientoEjercicio != null) {
            return ResponseEntity.ok(entrenamientoEjercicio);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public EntrenamientoEjercicio crear(@RequestBody EntrenamientoEjercicio entrenamientoEjercicio) {
        return entrenamientoEjercicioService.guardar(entrenamientoEjercicio);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EntrenamientoEjercicio> actualizar(@PathVariable Long id, @RequestBody EntrenamientoEjercicio entrenamientoEjercicio) {
        EntrenamientoEjercicio existente = entrenamientoEjercicioService.obtenerPorId(id);
        if (existente != null) {
            entrenamientoEjercicio.setIdEntrenamientoEjercicio(id);
            return ResponseEntity.ok(entrenamientoEjercicioService.guardar(entrenamientoEjercicio));
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        entrenamientoEjercicioService.eliminar(id);
    }
}
