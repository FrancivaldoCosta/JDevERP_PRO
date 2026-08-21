package br.com.jdeverp.pro.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.jdeverp.pro.model.Categoria;
import br.com.jdeverp.pro.repository.CategoriaRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

/*O QUE É O SERVICE*/
/*Dentro do service voce pode criar infinitos métodos, gerar grafico, api de pagamento, gerar relatorio e etc*/

@Service
public class CategoriaService {

	@Autowired /* Injeção de dependência */
	private CategoriaRepository categoriaRepository;

	/*
	 * Posso escrever query customizadas e dinâmicas, mais complexas do que no
	 * Repository
	 */
	@PersistenceContext
	private EntityManager entityManager;

	/* Os métodos do service serão chamador pelo Controller */
	public List<Categoria> findAll(Long idEmpresa) {
		return categoriaRepository.findAll(idEmpresa);
	}

	List<Categoria> buscaPorNome(String nome, Long idEmpresa) {
		return categoriaRepository.buscaPorNome(nome, idEmpresa);
	}

	boolean existePorNome(String nome, Long idEmpresa) {

		return categoriaRepository.existePorNome(nome, idEmpresa);
	}

	boolean existePorNomeDiferenteId(Long id, String nome, Long idEmpresa) {
		return categoriaRepository.existePorNomeDiferenteId(id, nome, idEmpresa);
	}

	void deleteById(Long id, Long idEmpresa) {
		categoriaRepository.deleteById(id, idEmpresa);
	}

	/*
	 * ====================dentro dos métodos do
	 * service===============================
	 */

	// Verificar se está em uso
	// Realizar um consulta com integração para saber se pode deletar
	// Fazer copia e backup
	// Fazer inumeras validações de regra de negocio
	// Fazer validações
	// Lançar exeções
	// Escrever regras de negócio

}