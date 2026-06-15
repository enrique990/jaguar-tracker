package ni.edu.uam.Jaguar_api.controller;

import ni.edu.uam.Jaguar_api.entity.RutinaDia;
import ni.edu.uam.Jaguar_api.service.RutinaDiaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/rutina-dias")
public class RutinaDiaController {

    @Autowired
    private RutinaDiaService rutinaDiaService;

    @GetMapping
    public List<RutinaDia> listar() {
        return rutinaDiaService.obtenerTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<RutinaDia> obtenerPorId(@PathVariable Long id) {
        RutinaDia rutinaDia = rutinaDiaService.obtenerPorId(id);
        if (rutinaDia != null) {
            return ResponseEntity.ok(rutinaDia);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public RutinaDia crear(@RequestBody RutinaDia rutinaDia) {
        return rutinaDiaService.guardar(rutinaDia);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RutinaDia> actualizar(@PathVariable Long id, @RequestBody RutinaDia rutinaDia) {
        RutinaDia existente = rutinaDiaService.obtenerPorId(id);
        if (existente != null) {
            rutinaDia.setIdRutinaDia(id);
            return ResponseEntity.ok(rutinaDiaService.guardar(rutinaDia));
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        rutinaDiaService.eliminar(id);
    }
}
