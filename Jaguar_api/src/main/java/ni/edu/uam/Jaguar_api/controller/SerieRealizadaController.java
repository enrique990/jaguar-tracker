package ni.edu.uam.Jaguar_api.controller;

import ni.edu.uam.Jaguar_api.entity.SerieRealizada;
import ni.edu.uam.Jaguar_api.service.SerieRealizadaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/series-realizadas")
public class SerieRealizadaController {

    @Autowired
    private SerieRealizadaService serieRealizadaService;

    @GetMapping
    public List<SerieRealizada> listar() {
        return serieRealizadaService.obtenerTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<SerieRealizada> obtenerPorId(@PathVariable Long id) {
        SerieRealizada serieRealizada = serieRealizadaService.obtenerPorId(id);
        if (serieRealizada != null) {
            return ResponseEntity.ok(serieRealizada);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public SerieRealizada crear(@RequestBody SerieRealizada serieRealizada) {
        return serieRealizadaService.guardar(serieRealizada);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SerieRealizada> actualizar(@PathVariable Long id, @RequestBody SerieRealizada serieRealizada) {
        SerieRealizada existente = serieRealizadaService.obtenerPorId(id);
        if (existente != null) {
            serieRealizada.setIdSerieRealizada(id);
            return ResponseEntity.ok(serieRealizadaService.guardar(serieRealizada));
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        serieRealizadaService.eliminar(id);
    }
}
