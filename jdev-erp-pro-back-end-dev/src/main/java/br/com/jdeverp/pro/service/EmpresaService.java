package br.com.jdeverp.pro.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.jdeverp.pro.model.Empresa;
import br.com.jdeverp.pro.repository.EmpresaRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Service
public class EmpresaService {

    @Autowired /* Injeção de depência */
    private EmpresaRepository empresaRepository;
    
    /*Posso escrever query customizadas e dinâmicas, mais complexas do que no Repository*/
    @PersistenceContext
    private EntityManager entityManager;
    

    /* Os métodos do service serão chamador pelo Controller */
    public List<Empresa> findAll() {
        return empresaRepository.findAll();
    }

    public Empresa buscarPorId(Long id) {
        return empresaRepository.buscarPorId(id);
    }

    List<Empresa> buscaPorNome(String nome) {
        return empresaRepository.buscaPorNome(nome);
    }

    boolean existePorNome(String nome) {
        return empresaRepository.existePorNome(nome);
    }

    boolean existePorNomeDiferenteId(Long id, String nome) {
        return empresaRepository.existePorNomeDiferenteId(id, nome);
    }

    void deleteById(Long id) {
        empresaRepository.deleteById(id);
    }

}
