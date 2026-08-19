package br.com.jdeverp.pro.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.jdeverp.pro.model.Pessoa;
import br.com.jdeverp.pro.repository.PessoaRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Service
public class PessoaService {

    @Autowired /* Injeção de depência */
    private PessoaRepository pessoaRepository;
    
    /*Posso escrever query customizadas e dinâmicas, mais complexas do que no Repository*/
    @PersistenceContext
    private EntityManager entityManager;
    

    /* Os métodos do service serão chamador pelo Controller */
    public List<Pessoa> findAll(Long idEmpresa) {
        return pessoaRepository.findAll(idEmpresa);
    }

    List<Pessoa> buscaPorNome(String nome, Long idEmpresa) {
        return pessoaRepository.buscaPorNome(nome, idEmpresa);
    }

    boolean existePorNome(String nome, Long idEmpresa) {

        return pessoaRepository.existePorNome(nome, idEmpresa);
    }

    boolean existePorNomeDiferenteId(Long id, String nome, Long idEmpresa) {
        return pessoaRepository.existePorNomeDiferenteId(id, nome, idEmpresa);
    }

    void deleteById(Long id, Long idEmpresa) {
        pessoaRepository.deleteById(id, idEmpresa);
    }

}
