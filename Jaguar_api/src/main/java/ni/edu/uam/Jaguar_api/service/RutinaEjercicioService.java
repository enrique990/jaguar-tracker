package ni.edu.uam.Jaguar_api.service;

import ni.edu.uam.Jaguar_api.entity.RutinaEjercicio;
import ni.edu.uam.Jaguar_api.repository.RutinaEjercicioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class RutinaEjercicioService {

    @Autowired
    private RutinaEjercicioRepository rutinaEjercicioRepository;

    public List<RutinaEjercicio> obtenerTodos() {
        return rutinaEjercicioRepository.findAll();
    }

    public RutinaEjercicio obtenerPorId(Long id) {
        return rutinaEjercicioRepository.findById(id).orElse(null);
    }

    public RutinaEjercicio guardar(RutinaEjercicio rutinaEjercicio) {
        return rutinaEjercicioRepository.save(rutinaEjercicio);
    }

    public void eliminar(Long id) {
        rutinaEjercicioRepository.deleteById(id);
    }
}
