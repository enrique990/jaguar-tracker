package ni.edu.uam.Jaguar_api.service;

import ni.edu.uam.Jaguar_api.entity.Rutina;
import ni.edu.uam.Jaguar_api.repository.RutinaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class RutinaService {

    @Autowired
    private RutinaRepository rutinaRepository;

    public List<Rutina> obtenerTodos() {
        return rutinaRepository.findAll();
    }

    public Rutina obtenerPorId(Long id) {
        return rutinaRepository.findById(id).orElse(null);
    }

    public Rutina guardar(Rutina rutina) {
        return rutinaRepository.save(rutina);
    }

    public void eliminar(Long id) {
        rutinaRepository.deleteById(id);
    }
}
