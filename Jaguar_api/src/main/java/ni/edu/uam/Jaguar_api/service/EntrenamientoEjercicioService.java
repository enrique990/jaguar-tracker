package ni.edu.uam.Jaguar_api.service;

import ni.edu.uam.Jaguar_api.entity.EntrenamientoEjercicio;
import ni.edu.uam.Jaguar_api.repository.EntrenamientoEjercicioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class EntrenamientoEjercicioService {

    @Autowired
    private EntrenamientoEjercicioRepository entrenamientoEjercicioRepository;

    public List<EntrenamientoEjercicio> obtenerTodos() {
        return entrenamientoEjercicioRepository.findAll();
    }

    public EntrenamientoEjercicio obtenerPorId(Long id) {
        return entrenamientoEjercicioRepository.findById(id).orElse(null);
    }

    public EntrenamientoEjercicio guardar(EntrenamientoEjercicio entrenamientoEjercicio) {
        return entrenamientoEjercicioRepository.save(entrenamientoEjercicio);
    }

    public void eliminar(Long id) {
        entrenamientoEjercicioRepository.deleteById(id);
    }
}
