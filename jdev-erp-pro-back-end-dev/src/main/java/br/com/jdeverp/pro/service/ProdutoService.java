package br.com.jdeverp.pro.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.jdeverp.pro.model.Produto;
import br.com.jdeverp.pro.repository.ProdutoRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Service
public class ProdutoService {

    @Autowired /* Injeção de depência */
    private ProdutoRepository produtoRepository;
    
    /*Posso escrever query customizadas e dinâmicas, mais complexas do que no Repository*/
    @PersistenceContext
    private EntityManager entityManager;
    

    /* Os métodos do service serão chamador pelo Controller */
    public List<Produto> findAll(Long idEmpresa) {
        return produtoRepository.findAll(idEmpresa);
    }

    List<Produto> buscaPorNome(String nome, Long idEmpresa) {
        return produtoRepository.buscaPorNome(nome, idEmpresa);
    }

    boolean existePorNome(String nome, Long idEmpresa) {

        return produtoRepository.existePorNome(nome, idEmpresa);
    }

    boolean existePorNomeDiferenteId(Long id, String nome, Long idEmpresa) {
        return produtoRepository.existePorNomeDiferenteId(id, nome, idEmpresa);
    }

    void deleteById(Long id, Long idEmpresa) {
        produtoRepository.deleteById(id, idEmpresa);
    }

}
