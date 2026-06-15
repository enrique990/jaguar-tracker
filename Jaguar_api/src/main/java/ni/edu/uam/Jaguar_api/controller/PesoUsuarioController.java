package ni.edu.uam.Jaguar_api.controller;

import ni.edu.uam.Jaguar_api.entity.PesoUsuario;
import ni.edu.uam.Jaguar_api.service.PesoUsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/pesos-usuarios")
public class PesoUsuarioController {

    @Autowired
    private PesoUsuarioService pesoUsuarioService;

    @GetMapping
    public List<PesoUsuario> listar() {
        return pesoUsuarioService.obtenerTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<PesoUsuario> obtenerPorId(@PathVariable Long id) {
        PesoUsuario pesoUsuario = pesoUsuarioService.obtenerPorId(id);
        if (pesoUsuario != null) {
            return ResponseEntity.ok(pesoUsuario);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public PesoUsuario crear(@RequestBody PesoUsuario pesoUsuario) {
        return pesoUsuarioService.guardar(pesoUsuario);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PesoUsuario> actualizar(@PathVariable Long id, @RequestBody PesoUsuario pesoUsuario) {
        PesoUsuario existente = pesoUsuarioService.obtenerPorId(id);
        if (existente != null) {
            pesoUsuario.setIdPeso(id);
            return ResponseEntity.ok(pesoUsuarioService.guardar(pesoUsuario));
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        pesoUsuarioService.eliminar(id);
    }
}
