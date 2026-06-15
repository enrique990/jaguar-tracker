package ni.edu.uam.Jaguar_api.service;

import ni.edu.uam.Jaguar_api.entity.Entrenamiento;
import ni.edu.uam.Jaguar_api.repository.EntrenamientoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class EntrenamientoService {

    @Autowired
    private EntrenamientoRepository entrenamientoRepository;

    public List<Entrenamiento> obtenerTodos() {
        return entrenamientoRepository.findAll();
    }

    public Entrenamiento obtenerPorId(Long id) {
        return entrenamientoRepository.findById(id).orElse(null);
    }

    public Entrenamiento guardar(Entrenamiento entrenamiento) {
        return entrenamientoRepository.save(entrenamiento);
    }

    public void eliminar(Long id) {
        entrenamientoRepository.deleteById(id);
    }
}
