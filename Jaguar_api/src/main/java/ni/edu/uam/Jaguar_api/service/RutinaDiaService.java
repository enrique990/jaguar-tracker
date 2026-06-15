package ni.edu.uam.Jaguar_api.service;

import ni.edu.uam.Jaguar_api.entity.RutinaDia;
import ni.edu.uam.Jaguar_api.repository.RutinaDiaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class RutinaDiaService {

    @Autowired
    private RutinaDiaRepository rutinaDiaRepository;

    public List<RutinaDia> obtenerTodos() {
        return rutinaDiaRepository.findAll();
    }

    public RutinaDia obtenerPorId(Long id) {
        return rutinaDiaRepository.findById(id).orElse(null);
    }

    public RutinaDia guardar(RutinaDia rutinaDia) {
        return rutinaDiaRepository.save(rutinaDia);
    }

    public void eliminar(Long id) {
        rutinaDiaRepository.deleteById(id);
    }
}
