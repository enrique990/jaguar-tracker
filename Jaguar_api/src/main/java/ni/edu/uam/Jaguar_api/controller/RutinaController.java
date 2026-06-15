package ni.edu.uam.Jaguar_api.controller;

import ni.edu.uam.Jaguar_api.entity.Rutina;
import ni.edu.uam.Jaguar_api.service.RutinaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/rutinas")
public class RutinaController {

    @Autowired
    private RutinaService rutinaService;

    @GetMapping
    public List<Rutina> listar() {
        return rutinaService.obtenerTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Rutina> obtenerPorId(@PathVariable Long id) {
        Rutina rutina = rutinaService.obtenerPorId(id);
        if (rutina != null) {
            return ResponseEntity.ok(rutina);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public Rutina crear(@RequestBody Rutina rutina) {
        return rutinaService.guardar(rutina);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Rutina> actualizar(@PathVariable Long id, @RequestBody Rutina rutina) {
        Rutina existente = rutinaService.obtenerPorId(id);
        if (existente != null) {
            rutina.setIdRutina(id);
            return ResponseEntity.ok(rutinaService.guardar(rutina));
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        rutinaService.eliminar(id);
    }
}
