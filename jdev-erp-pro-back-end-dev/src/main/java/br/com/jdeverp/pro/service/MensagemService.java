package br.com.jdeverp.pro.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.jdeverp.pro.model.Mensagem;
import br.com.jdeverp.pro.repository.MensagemRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Service
public class MensagemService {

    @Autowired
    private MensagemRepository mensagemRepository;

    @PersistenceContext
    private EntityManager entityManager;

    public List<Mensagem> findAll(Long idEmpresa) {
        return mensagemRepository.findAll(idEmpresa);
    }

    List<Mensagem> buscaPorConteudo(String conteudo, Long idEmpresa) {
        return mensagemRepository.buscaPorConteudo(conteudo, idEmpresa);
    }

    boolean existePorConteudo(String conteudo, Long idEmpresa) {
        return mensagemRepository.existePorConteudo(conteudo, idEmpresa);
    }

    boolean existePorConteudoDiferenteId(Long id, String conteudo, Long idEmpresa) {
        return mensagemRepository.existePorConteudoDiferenteId(id, conteudo, idEmpresa);
    }

    void deleteById(Long id, Long idEmpresa) {
        mensagemRepository.deleteById(id, idEmpresa);
    }

    public List<Mensagem> findAllByChamado(Long idChamado, Long idEmpresa) {
        return mensagemRepository.findAllByChamado(idChamado, idEmpresa);
    }

    List<Mensagem> buscaPorConteudoByChamado(String conteudo, Long idChamado, Long idEmpresa) {
        return mensagemRepository.buscaPorConteudoByChamado(conteudo, idChamado, idEmpresa);
    }

    boolean existePorConteudoByChamado(String conteudo, Long idChamado, Long idEmpresa) {
        return mensagemRepository.existePorConteudoByChamado(conteudo, idChamado, idEmpresa);
    }

    boolean existePorConteudoDiferenteIdByChamado(Long id, String conteudo, Long idChamado, Long idEmpresa) {
        return mensagemRepository.existePorConteudoDiferenteIdByChamado(id, conteudo, idChamado, idEmpresa);
    }

    long countByChamado(Long idChamado, Long idEmpresa) {
        return mensagemRepository.countByChamado(idChamado, idEmpresa);
    }

    void deleteAllByChamado(Long idChamado, Long idEmpresa) {
        mensagemRepository.deleteAllByChamado(idChamado, idEmpresa);
    }

    void deleteByIdAndChamado(Long id, Long idChamado, Long idEmpresa) {
        mensagemRepository.deleteByIdAndChamado(id, idChamado, idEmpresa);
    }

    public List<Mensagem> findAllNaoLidas(Long idEmpresa) {
        return mensagemRepository.findAllNaoLidas(idEmpresa);
    }

    public List<Mensagem> findAllNaoLidasByChamado(Long idChamado, Long idEmpresa) {
        return mensagemRepository.findAllNaoLidasByChamado(idChamado, idEmpresa);
    }

    public long countNaoLidasByChamado(Long idChamado, Long idEmpresa) {
        return mensagemRepository.countNaoLidasByChamado(idChamado, idEmpresa);
    }

    void updateLida(Long id, Boolean lida, Long idEmpresa) {
        mensagemRepository.updateLida(id, lida, idEmpresa);
    }

    public List<Mensagem> findAllByAtendente(Long idAtendente, Long idEmpresa) {
        return mensagemRepository.findAllByAtendente(idAtendente, idEmpresa);
    }

    public long countByAtendente(Long idAtendente, Long idEmpresa) {
        return mensagemRepository.countByAtendente(idAtendente, idEmpresa);
    }

    public List<Mensagem> findAllByCliente(Long idCliente, Long idEmpresa) {
        return mensagemRepository.findAllByCliente(idCliente, idEmpresa);
    }

    public long countByCliente(Long idCliente, Long idEmpresa) {
        return mensagemRepository.countByCliente(idCliente, idEmpresa);
    }

    public List<Mensagem> findAllByChamadoAndAtendente(Long idChamado, Long idAtendente, Long idEmpresa) {
        return mensagemRepository.findAllByChamadoAndAtendente(idChamado, idAtendente, idEmpresa);
    }

    public List<Mensagem> findAllNaoLidasByAtendente(Long idAtendente, Long idEmpresa) {
        return mensagemRepository.findAllNaoLidasByAtendente(idAtendente, idEmpresa);
    }

    public List<Mensagem> findAllNaoLidasByCliente(Long idCliente, Long idEmpresa) {
        return mensagemRepository.findAllNaoLidasByCliente(idCliente, idEmpresa);
    }

}
