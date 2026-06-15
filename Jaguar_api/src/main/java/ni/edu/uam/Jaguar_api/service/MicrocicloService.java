package ni.edu.uam.Jaguar_api.service;

import ni.edu.uam.Jaguar_api.entity.Microciclo;
import ni.edu.uam.Jaguar_api.repository.MicrocicloRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class MicrocicloService {

    @Autowired
    private MicrocicloRepository microcicloRepository;

    public List<Microciclo> obtenerTodos() {
        return microcicloRepository.findAll();
    }

    public Microciclo obtenerPorId(Long id) {
        return microcicloRepository.findById(id).orElse(null);
    }

    public Microciclo guardar(Microciclo microciclo) {
        return microcicloRepository.save(microciclo);
    }

    public void eliminar(Long id) {
        microcicloRepository.deleteById(id);
    }
}
