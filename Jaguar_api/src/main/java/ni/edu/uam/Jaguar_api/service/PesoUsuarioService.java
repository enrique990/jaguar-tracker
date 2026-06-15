package ni.edu.uam.Jaguar_api.service;

import ni.edu.uam.Jaguar_api.entity.PesoUsuario;
import ni.edu.uam.Jaguar_api.repository.PesoUsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class PesoUsuarioService {

    @Autowired
    private PesoUsuarioRepository pesoUsuarioRepository;

    public List<PesoUsuario> obtenerTodos() {
        return pesoUsuarioRepository.findAll();
    }

    public PesoUsuario obtenerPorId(Long id) {
        return pesoUsuarioRepository.findById(id).orElse(null);
    }

    public PesoUsuario guardar(PesoUsuario pesoUsuario) {
        return pesoUsuarioRepository.save(pesoUsuario);
    }

    public void eliminar(Long id) {
        pesoUsuarioRepository.deleteById(id);
    }
}
