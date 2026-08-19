package br.com.jdeverp.pro.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.jdeverp.pro.model.Usuario;
import br.com.jdeverp.pro.repository.UsuarioRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Service
public class UsuarioService {

    @Autowired /* Injeção de depência */
    private UsuarioRepository usuarioRepository;
    
    /*Posso escrever query customizadas e dinâmicas, mais complexas do que no Repository*/
    @PersistenceContext
    private EntityManager entityManager;
    

    /* Os métodos do service serão chamador pelo Controller */
    public List<Usuario> findAll(Long idEmpresa) {
        return usuarioRepository.findAll(idEmpresa);
    }

    public Usuario buscaPorLogin(String login) {
        return usuarioRepository.buscaPorLogin(login);
    }

    List<Usuario> buscaPorNome(String nome, Long idEmpresa) {
        return usuarioRepository.buscaPorNome(nome, idEmpresa);
    }

    boolean existePorNome(String nome, Long idEmpresa) {

        return usuarioRepository.existePorNome(nome, idEmpresa);
    }

    boolean existePorNomeDiferenteId(Long id, String nome, Long idEmpresa) {
        return usuarioRepository.existePorNomeDiferenteId(id, nome, idEmpresa);
    }

    void deleteById(Long id, Long idEmpresa) {
        usuarioRepository.deleteById(id, idEmpresa);
    }

}
