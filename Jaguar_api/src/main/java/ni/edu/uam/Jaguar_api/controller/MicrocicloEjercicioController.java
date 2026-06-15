package ni.edu.uam.Jaguar_api.controller;

import ni.edu.uam.Jaguar_api.entity.MicrocicloEjercicio;
import ni.edu.uam.Jaguar_api.service.MicrocicloEjercicioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/microciclo-ejercicios")
public class MicrocicloEjercicioController {

    @Autowired
    private MicrocicloEjercicioService microcicloEjercicioService;

    @GetMapping
    public List<MicrocicloEjercicio> listar() {
        return microcicloEjercicioService.obtenerTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<MicrocicloEjercicio> obtenerPorId(@PathVariable Long id) {
        MicrocicloEjercicio microcicloEjercicio = microcicloEjercicioService.obtenerPorId(id);
        if (microcicloEjercicio != null) {
            return ResponseEntity.ok(microcicloEjercicio);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public MicrocicloEjercicio crear(@RequestBody MicrocicloEjercicio microcicloEjercicio) {
        return microcicloEjercicioService.guardar(microcicloEjercicio);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MicrocicloEjercicio> actualizar(@PathVariable Long id, @RequestBody MicrocicloEjercicio microcicloEjercicio) {
        MicrocicloEjercicio existente = microcicloEjercicioService.obtenerPorId(id);
        if (existente != null) {
            microcicloEjercicio.setIdMicrocicloEjercicio(id);
            return ResponseEntity.ok(microcicloEjercicioService.guardar(microcicloEjercicio));
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        microcicloEjercicioService.eliminar(id);
    }
}
