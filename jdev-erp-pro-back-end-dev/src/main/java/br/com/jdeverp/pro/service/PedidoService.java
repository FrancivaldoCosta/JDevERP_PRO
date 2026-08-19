package br.com.jdeverp.pro.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.jdeverp.pro.model.Pedido;
import br.com.jdeverp.pro.repository.PedidoRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Service
public class PedidoService {

    @Autowired /* Injeção de depência */
    private PedidoRepository pedidoRepository;
    
    @PersistenceContext
    private EntityManager entityManager;
    

    /* Os métodos do service serão chamador pelo Controller */
    public List<Pedido> findAll(Long idEmpresa) {
        return pedidoRepository.findAll(idEmpresa);
    }

    List<Pedido> buscaPorNumeroPedido(String numeroPedido, Long idEmpresa) {
        return pedidoRepository.buscaPorNumeroPedido(numeroPedido, idEmpresa);
    }

    boolean existePorNumeroPedido(String numeroPedido, Long idEmpresa) {
        return pedidoRepository.existePorNumeroPedido(numeroPedido, idEmpresa);
    }

    boolean existePorNumeroPedidoDiferenteId(Long id, String numeroPedido, Long idEmpresa) {
        return pedidoRepository.existePorNumeroPedidoDiferenteId(id, numeroPedido, idEmpresa);
    }

    void deleteById(Long id, Long idEmpresa) {
        pedidoRepository.deleteById(id, idEmpresa);
    }

}
