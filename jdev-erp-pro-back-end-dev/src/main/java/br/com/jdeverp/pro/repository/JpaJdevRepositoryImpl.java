package br.com.jdeverp.pro.repository;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.apache.commons.collections4.IterableUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;

public class JpaJdevRepositoryImpl<T, ID extends Serializable> extends SimpleJpaRepository<T, ID>
		implements JpaJdevRepository<T, ID> {

	private final Class<T> domainClass; /* Classe model ou entidade */
	private final EntityManager entityManager; /* É o nucleo da persistencia do JPA */
	private final boolean multiEmpresa;

	/**
	 * Cria uma nova instância do repositório genérico utilizando apenas a classe da
	 * entidade e o {@link EntityManager}.
	 *
	 * <p>
	 * Este construtor é responsável por inicializar a implementação base do
	 * {@link SimpleJpaRepository}, permitindo que todos os métodos padrões do
	 * Spring Data JPA (save, findById, delete, findAll, entre outros) funcionem
	 * corretamente.
	 * </p>
	 *
	 * <p>
	 * Além disso, armazena a classe da entidade e a referência do
	 * {@link EntityManager}, que poderão ser utilizadas pelos métodos
	 * personalizados implementados nesta classe.
	 * </p>
	 *
	 * @param domainClass   Classe da entidade JPA que será manipulada pelo
	 *                      repositório. É utilizada para identificar o tipo da
	 *                      entidade durante operações genéricas.
	 *
	 * @param entityManager Gerenciador de persistência do JPA responsável pela
	 *                      comunicação com o banco de dados. Permite executar
	 *                      consultas, persistir, atualizar, remover entidades e
	 *                      controlar o contexto de persistência.
	 */
	public JpaJdevRepositoryImpl(Class<T> domainClass, EntityManager entityManager) {
		super(domainClass, entityManager);
		this.domainClass = domainClass;
		this.entityManager = entityManager;
		multiEmpresa = possuiEmpresa();
	}

	/**
	 * Cria uma nova instância do repositório genérico utilizando os metadados da
	 * entidade fornecidos pelo Spring Data JPA.
	 *
	 * <p>
	 * Diferentemente do outro construtor, este recebe um objeto
	 * {@link JpaEntityInformation}, que contém informações completas da entidade,
	 * como:
	 * </p>
	 *
	 * <ul>
	 * <li>Classe da entidade.</li>
	 * <li>Tipo da chave primária.</li>
	 * <li>Campo anotado com {@code @Id}.</li>
	 * <li>Estratégia de geração do identificador.</li>
	 * <li>Metadados utilizados internamente pelo Spring Data JPA.</li>
	 * </ul>
	 *
	 * <p>
	 * Este construtor é o mais utilizado internamente pelo Spring durante a criação
	 * automática dos repositórios através da {@code JpaRepositoryFactory}.
	 * </p>
	 *
	 * @param entityInformation Objeto contendo todos os metadados da entidade
	 *                          gerenciados pelo Spring Data JPA.
	 *
	 * @param entityManager     Gerenciador de persistência responsável pelas
	 *                          operações de acesso ao banco de dados.
	 */
	public JpaJdevRepositoryImpl(JpaEntityInformation<T, ?> entityInformation, EntityManager entityManager) {

		super(entityInformation, entityManager);
		this.domainClass = entityInformation.getJavaType();
		this.entityManager = entityManager;
		multiEmpresa = possuiEmpresa();
	}

	@Override
	public Page<T> listarPaginado(Long empresaId, Pageable pageable) {

		String entidade = domainClass.getSimpleName();

		String jpql = "from " + entidade;

		if (multiEmpresa) {
			jpql += " Where empresa.id = :empresaId";
		}

//TRECHO QUE DEU ERRO

		if (pageable.getSort().isSorted()) {
			jpql += " order by ";

			List<String> orders = new ArrayList<String>();
			for (Sort.Order order : pageable.getSort()) {
				orders.add(order.getProperty() + " " + order.getDirection().name());

			}

			jpql += String.join(", ", orders);
		}

		TypedQuery<T> query = entityManager.createQuery(jpql, domainClass);

		if (multiEmpresa) {
			query.setParameter("empresaId", empresaId);
		}

		List<T> lista = query.setFirstResult((int) pageable.getOffset()).setMaxResults(pageable.getPageSize())
				.getResultList();

		return new PageImpl<T>(lista, pageable, total(empresaId));
	}

	@Override
	public long total(Long empresaId) {

		String entidade = domainClass.getSimpleName();

		String jpql = "select count(*) from " + entidade;

		if (multiEmpresa) {
			jpql += " Where empresa.id = :empresaId";
		}

		TypedQuery<Long> query = entityManager.createQuery(jpql, Long.class);

		if (multiEmpresa) {
			query.setParameter("empresaId", empresaId);
		}

		return query.getSingleResult();
	}

	private boolean possuiEmpresa() {
		try {
			return domainClass.getDeclaredField("empresa") != null;
		} catch (NoSuchFieldException e) {
			return false;
		}

	}

	@Override
	public Optional<T> buscarPorId(ID id, Long empresaId) {
		// TODO Auto-generated method stub
		return Optional.empty();
	}

	@Override
	public List<T> listar(Long empresaId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean existeById(ID id, Long empresaId) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<T> buscarPorIds(Iterable<ID> ids, Long empresaId) {

		if (IterableUtils.isEmpty(ids)) {
			return Collections.emptyList();

		}

		String jpql = "from " + domainClass.getSimpleName() + " where id in :ids";

		if (multiEmpresa) {
			jpql += " and empresa.id = :empresaId";
		}

		TypedQuery<T> query = entityManager.createQuery(jpql, domainClass);
		query.setParameter("ids", ids);

		if (multiEmpresa) {
			query.setParameter("empresa.id", empresaId);
		}

		return query.getResultList();
	}

	@Override
	public void deletarAllById(Iterable<ID> ids, Long empresaId) {

		String jpql = "delete from " + domainClass.getSimpleName() + " where id in :ids";
		if (multiEmpresa) {
			jpql += " and empresa.id = :empresaId";
		}

		Query query = entityManager.createQuery(jpql);
		query.setParameter("ids", ids);

		if (multiEmpresa) {
			query.setParameter("empresa.id", empresaId);
		}

		query.executeUpdate();

	}

	@Override
	public long deleteAll(Long empresaID) {

		String jpql = "delete from " + domainClass.getSimpleName();
		if (multiEmpresa) {
			jpql += " where empresa.id = :empresaId";
		}

		Query query = entityManager.createQuery(jpql);

		if (multiEmpresa) {
			query.setParameter("empresa.id", empresaID);
		}

		return query.executeUpdate();
	}

}
