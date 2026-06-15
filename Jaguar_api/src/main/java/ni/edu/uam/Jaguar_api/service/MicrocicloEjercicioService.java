package ni.edu.uam.Jaguar_api.service;

import ni.edu.uam.Jaguar_api.entity.MicrocicloEjercicio;
import ni.edu.uam.Jaguar_api.repository.MicrocicloEjercicioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class MicrocicloEjercicioService {

    @Autowired
    private MicrocicloEjercicioRepository microcicloEjercicioRepository;

    public List<MicrocicloEjercicio> obtenerTodos() {
        return microcicloEjercicioRepository.findAll();
    }

    public MicrocicloEjercicio obtenerPorId(Long id) {
        return microcicloEjercicioRepository.findById(id).orElse(null);
    }

    public MicrocicloEjercicio guardar(MicrocicloEjercicio microcicloEjercicio) {
        return microcicloEjercicioRepository.save(microcicloEjercicio);
    }

    public void eliminar(Long id) {
        microcicloEjercicioRepository.deleteById(id);
    }
}
