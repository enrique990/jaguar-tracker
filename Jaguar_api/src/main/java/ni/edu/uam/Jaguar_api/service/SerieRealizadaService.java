package ni.edu.uam.Jaguar_api.service;

import ni.edu.uam.Jaguar_api.entity.SerieRealizada;
import ni.edu.uam.Jaguar_api.repository.SerieRealizadaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class SerieRealizadaService {

    @Autowired
    private SerieRealizadaRepository serieRealizadaRepository;

    public List<SerieRealizada> obtenerTodos() {
        return serieRealizadaRepository.findAll();
    }

    public SerieRealizada obtenerPorId(Long id) {
        return serieRealizadaRepository.findById(id).orElse(null);
    }

    public SerieRealizada guardar(SerieRealizada serieRealizada) {
        return serieRealizadaRepository.save(serieRealizada);
    }

    public void eliminar(Long id) {
        serieRealizadaRepository.deleteById(id);
    }
}
