package ni.edu.uam.Jaguar_api.controller;

import ni.edu.uam.Jaguar_api.entity.Microciclo;
import ni.edu.uam.Jaguar_api.service.MicrocicloService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/microciclos")
public class MicrocicloController {

    @Autowired
    private MicrocicloService microcicloService;

    @GetMapping
    public List<Microciclo> listar() {
        return microcicloService.obtenerTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Microciclo> obtenerPorId(@PathVariable Long id) {
        Microciclo microciclo = microcicloService.obtenerPorId(id);
        if (microciclo != null) {
            return ResponseEntity.ok(microciclo);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public Microciclo crear(@RequestBody Microciclo microciclo) {
        return microcicloService.guardar(microciclo);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Microciclo> actualizar(@PathVariable Long id, @RequestBody Microciclo microciclo) {
        Microciclo existente = microcicloService.obtenerPorId(id);
        if (existente != null) {
            microciclo.setIdMicrociclo(id);
            return ResponseEntity.ok(microcicloService.guardar(microciclo));
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        microcicloService.eliminar(id);
    }
}
