package br.com.jdeverp.pro.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.jdeverp.pro.model.RoleUsuario;
import br.com.jdeverp.pro.repository.RoleUsuarioRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Service
public class RoleUsuarioService {

    @Autowired /* Injeção de depência */
    private RoleUsuarioRepository roleUsuarioRepository;
    
    @PersistenceContext
    private EntityManager entityManager;
    
    public List<RoleUsuario> findAllByUsuario(Long idUsuario, Long idEmpresa) {
        return roleUsuarioRepository.findAllByUsuario(idUsuario, idEmpresa);
    }

    List<RoleUsuario> findAllByRoleAndEmpresa(Long idRole, Long idEmpresa) {
        return roleUsuarioRepository.findAllByRoleAndEmpresa(idRole, idEmpresa);
    }

    boolean existePorUsuarioERole(Long idUsuario, Long idRole, Long idEmpresa) {
        return roleUsuarioRepository.existePorUsuarioERole(idUsuario, idRole, idEmpresa);
    }

    void deleteById(Long id) {
        roleUsuarioRepository.deleteById(id);
    }

    void deleteByUsuarioAndRole(Long idUsuario, Long idRole, Long idEmpresa) {
        roleUsuarioRepository.deleteByUsuarioAndRole(idUsuario, idRole, idEmpresa);
    }

}
