package br.com.jdeverp.pro.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.jdeverp.pro.model.Plano;
import br.com.jdeverp.pro.repository.PlanoRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Service
public class PlanoService {

    @Autowired
    private PlanoRepository planoRepository;

    @PersistenceContext
    private EntityManager entityManager;

    public List<Plano> findAll() {
        return planoRepository.findAll();
    }

    List<Plano> buscaPorNome(String nome) {
        return planoRepository.buscaPorNome(nome);
    }

    boolean existePorNome(String nome) {
        return planoRepository.existePorNome(nome);
    }

    boolean existePorNomeDiferenteId(Long id, String nome) {
        return planoRepository.existePorNomeDiferenteId(id, nome);
    }

    void deleteById(Long id) {
        planoRepository.deleteById(id);
    }

}
