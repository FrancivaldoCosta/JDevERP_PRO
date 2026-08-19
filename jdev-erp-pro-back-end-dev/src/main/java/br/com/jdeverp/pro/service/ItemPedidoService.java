package br.com.jdeverp.pro.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.jdeverp.pro.model.ItemPedido;
import br.com.jdeverp.pro.repository.ItemPedidoRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Service
public class ItemPedidoService {

    @Autowired
    private ItemPedidoRepository itemPedidoRepository;

    @PersistenceContext
    private EntityManager entityManager;

    public List<ItemPedido> findAll(Long idPedido, Long idEmpresa) {
        return itemPedidoRepository.findAll(idPedido, idEmpresa);
    }

    List<ItemPedido> buscaPorNome(String nome, Long idPedido, Long idEmpresa) {
        return itemPedidoRepository.buscaPorNome(nome, idPedido, idEmpresa);
    }

    boolean existePorNome(String nome, Long idPedido, Long idEmpresa) {
        return itemPedidoRepository.existePorNome(nome, idPedido, idEmpresa);
    }

    boolean existePorNomeDiferenteId(Long id, String nome, Long idPedido, Long idEmpresa) {
        return itemPedidoRepository.existePorNomeDiferenteId(id, nome, idPedido, idEmpresa);
    }

    void deleteById(Long id, Long idPedido, Long idEmpresa) {
        itemPedidoRepository.deleteById(id, idPedido, idEmpresa);
    }

    public List<ItemPedido> findAllByPedido(Long idPedido, Long idEmpresa) {
        return itemPedidoRepository.findAllByPedido(idPedido, idEmpresa);
    }

    List<ItemPedido> buscaPorNomePorPedido(String nome, Long idPedido, Long idEmpresa) {
        return itemPedidoRepository.buscaPorNomePorPedido(nome, idPedido, idEmpresa);
    }

    boolean existePorNomePorPedido(String nome, Long idPedido, Long idEmpresa) {
        return itemPedidoRepository.existePorNomePorPedido(nome, idPedido, idEmpresa);
    }

    boolean existePorNomeDiferenteIdPorPedido(Long id, String nome, Long idPedido, Long idEmpresa) {
        return itemPedidoRepository.existePorNomeDiferenteIdPorPedido(id, nome, idPedido, idEmpresa);
    }

    void deleteByIdAndPedido(Long id, Long idPedido, Long idEmpresa) {
        itemPedidoRepository.deleteByIdAndPedido(id, idPedido, idEmpresa);
    }

}
