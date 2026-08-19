package br.com.jdeverp.pro.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.jdeverp.pro.model.Role;
import br.com.jdeverp.pro.repository.RoleRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Service
public class RoleService {

    @Autowired /* Injeção de depência */
    private RoleRepository roleRepository;
    
    /*Posso escrever query customizadas e dinâmicas, mais complexas do que no Repository*/
    @PersistenceContext
    private EntityManager entityManager;
    

    /* Os métodos do service serão chamador pelo Controller */
    public List<Role> findAll() {
        return roleRepository.findAll();
    }

    List<Role> buscaPorAcesso(String acesso) {
        return roleRepository.buscaPorAcesso(acesso);
    }

    boolean existePorAcesso(String acesso) {
        return roleRepository.existePorAcesso(acesso);
    }

    boolean existePorAcessoDiferenteId(Long id, String acesso) {
        return roleRepository.existePorAcessoDiferenteId(id, acesso);
    }

    void deleteById(Long id) {
        roleRepository.deleteById(id);
    }

}
