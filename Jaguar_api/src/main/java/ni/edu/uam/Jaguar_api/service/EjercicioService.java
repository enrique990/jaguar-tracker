package ni.edu.uam.Jaguar_api.service;

import ni.edu.uam.Jaguar_api.entity.Ejercicio;
import ni.edu.uam.Jaguar_api.repository.EjercicioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class EjercicioService {

    @Autowired
    private EjercicioRepository ejercicioRepository;

    public List<Ejercicio> obtenerTodos() {
        return ejercicioRepository.findAll();
    }

    public Ejercicio obtenerPorId(Long id) {
        return ejercicioRepository.findById(id).orElse(null);
    }

    public Ejercicio guardar(Ejercicio ejercicio) {
        return ejercicioRepository.save(ejercicio);
    }

    public void eliminar(Long id) {
        ejercicioRepository.deleteById(id);
    }
}
