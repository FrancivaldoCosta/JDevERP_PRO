package br.com.jdeverp.pro.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.jdeverp.pro.model.ClienteFuncionario;
import br.com.jdeverp.pro.repository.ClienteFuncionarioRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Service
public class ClienteFuncionarioService {

    @Autowired
    private ClienteFuncionarioRepository clienteFuncionarioRepository;

    @PersistenceContext
    private EntityManager entityManager;

    public List<ClienteFuncionario> findAll(Long idEmpresa) {
        return clienteFuncionarioRepository.findAll(idEmpresa);
    }

    List<ClienteFuncionario> buscaPorNome(String nome, Long idEmpresa) {
        return clienteFuncionarioRepository.buscaPorNome(nome, idEmpresa);
    }

    boolean existePorNome(String nome, Long idEmpresa) {
        return clienteFuncionarioRepository.existePorNome(nome, idEmpresa);
    }

    boolean existePorNomeDiferenteId(Long id, String nome, Long idEmpresa) {
        return clienteFuncionarioRepository.existePorNomeDiferenteId(id, nome, idEmpresa);
    }

    void deleteById(Long id, Long idEmpresa) {
        clienteFuncionarioRepository.deleteById(id, idEmpresa);
    }

}
