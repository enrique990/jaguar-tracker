package ni.edu.uam.Jaguar_api.controller;

import ni.edu.uam.Jaguar_api.entity.RutinaEjercicio;
import ni.edu.uam.Jaguar_api.service.RutinaEjercicioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/rutina-ejercicios")
public class RutinaEjercicioController {

    @Autowired
    private RutinaEjercicioService rutinaEjercicioService;

    @GetMapping
    public List<RutinaEjercicio> listar() {
        return rutinaEjercicioService.obtenerTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<RutinaEjercicio> obtenerPorId(@PathVariable Long id) {
        RutinaEjercicio rutinaEjercicio = rutinaEjercicioService.obtenerPorId(id);
        if (rutinaEjercicio != null) {
            return ResponseEntity.ok(rutinaEjercicio);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public RutinaEjercicio crear(@RequestBody RutinaEjercicio rutinaEjercicio) {
        return rutinaEjercicioService.guardar(rutinaEjercicio);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RutinaEjercicio> actualizar(@PathVariable Long id, @RequestBody RutinaEjercicio rutinaEjercicio) {
        RutinaEjercicio existente = rutinaEjercicioService.obtenerPorId(id);
        if (existente != null) {
            rutinaEjercicio.setIdRutinaEjercicio(id);
            return ResponseEntity.ok(rutinaEjercicioService.guardar(rutinaEjercicio));
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        rutinaEjercicioService.eliminar(id);
    }
}
