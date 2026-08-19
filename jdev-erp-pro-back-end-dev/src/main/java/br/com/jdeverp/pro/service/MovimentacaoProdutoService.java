package br.com.jdeverp.pro.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.jdeverp.pro.model.MovimentacaoProduto;
import br.com.jdeverp.pro.repository.MovimentacaoProdutoRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Service
public class MovimentacaoProdutoService {

    @Autowired
    private MovimentacaoProdutoRepository movimentacaoProdutoRepository;

    @PersistenceContext
    private EntityManager entityManager;

    public List<MovimentacaoProduto> findAll(Long idEmpresa) {
        return movimentacaoProdutoRepository.findAll(idEmpresa);
    }

    List<MovimentacaoProduto> buscaPorNome(String nome, Long idEmpresa) {
        return movimentacaoProdutoRepository.buscaPorNome(nome, idEmpresa);
    }

    boolean existePorNome(String nome, Long idEmpresa) {
        return movimentacaoProdutoRepository.existePorNome(nome, idEmpresa);
    }

    boolean existePorNomeDiferenteId(Long id, String nome, Long idEmpresa) {
        return movimentacaoProdutoRepository.existePorNomeDiferenteId(id, nome, idEmpresa);
    }

    void deleteById(Long id, Long idEmpresa) {
        movimentacaoProdutoRepository.deleteById(id, idEmpresa);
    }

    public List<MovimentacaoProduto> findAllByPedido(Long idPedido, Long idEmpresa) {
        return movimentacaoProdutoRepository.findAllByPedido(idPedido, idEmpresa);
    }

    List<MovimentacaoProduto> buscaPorNomeByPedido(String nome, Long idPedido, Long idEmpresa) {
        return movimentacaoProdutoRepository.buscaPorNomeByPedido(nome, idPedido, idEmpresa);
    }

    boolean existePorNomeByPedido(String nome, Long idPedido, Long idEmpresa) {
        return movimentacaoProdutoRepository.existePorNomeByPedido(nome, idPedido, idEmpresa);
    }

    boolean existePorNomeDiferenteIdByPedido(Long id, String nome, Long idPedido, Long idEmpresa) {
        return movimentacaoProdutoRepository.existePorNomeDiferenteIdByPedido(id, nome, idPedido, idEmpresa);
    }

    void deleteByIdAndPedido(Long id, Long idPedido, Long idEmpresa) {
        movimentacaoProdutoRepository.deleteByIdAndPedido(id, idPedido, idEmpresa);
    }

}
