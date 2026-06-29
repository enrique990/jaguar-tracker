    package ni.edu.uam.Jaguar_api.controller;

    import ni.edu.uam.Jaguar_api.entity.Ejercicio;
    import ni.edu.uam.Jaguar_api.service.EjercicioService;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.http.ResponseEntity;
    import org.springframework.web.bind.annotation.*;
    import java.util.List;

    @RestController
    @RequestMapping("/api/ejercicios")
    public class EjercicioController {

        @Autowired
        private EjercicioService ejercicioService;

        @GetMapping
        public List<Ejercicio> listar() {
            return ejercicioService.obtenerTodos();
        }

        @GetMapping("/{id}")
        public ResponseEntity<Ejercicio> obtenerPorId(@PathVariable Long id) {
            Ejercicio ejercicio = ejercicioService.obtenerPorId(id);
            if (ejercicio != null) {
                return ResponseEntity.ok(ejercicio);
            }
            return ResponseEntity.notFound().build();
        }

        @PostMapping
        public Ejercicio crear(@RequestBody Ejercicio ejercicio) {
            return ejercicioService.guardar(ejercicio);
        }

        @PutMapping("/{id}")
        public ResponseEntity<Ejercicio> actualizar(@PathVariable Long id, @RequestBody Ejercicio ejercicio) {
            Ejercicio existente = ejercicioService.obtenerPorId(id);
            if (existente != null) {
                ejercicio.setIdEjercicio(id);
                return ResponseEntity.ok(ejercicioService.guardar(ejercicio));
            }
            return ResponseEntity.notFound().build();
        }

        @DeleteMapping("/{id}")
        public void eliminar(@PathVariable Long id) {
            ejercicioService.eliminar(id);
        }
    }
