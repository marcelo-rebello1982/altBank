package br.com.cadastroit.services.repositories;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.com.cadastroit.services.api.domain.Cartao;
import br.com.cadastroit.services.api.domain.Cartao_;
import br.com.cadastroit.services.api.domain.Endereco;
import br.com.cadastroit.services.api.domain.Endereco_;
import br.com.cadastroit.services.api.domain.Pessoa;
import br.com.cadastroit.services.api.domain.Pessoa_;
import br.com.cadastroit.services.api.enums.DbLayerMessage;
import br.com.cadastroit.services.exceptions.CartaoException;
import br.com.cadastroit.services.exceptions.PessoaException;
import br.com.cadastroit.services.repositories.impl.PessoaRepositoryImpl;
import br.com.cadastroit.sevices.web.dto.PessoaDTO;
import lombok.NoArgsConstructor;

@Repository
@NoArgsConstructor
@Transactional(propagation = Propagation.REQUIRED)
public class PessoaRepository implements Serializable {
	
	private static final long serialVersionUID = 2246460735361455499L;
	
	private static final String MODE = "Error on %s mode to %s, [error] = %s";
	private static final String OBJECT = "PESSOA";
	private static final String ORDER = "order";
	
	@Autowired
	private PessoaRepositoryImpl pessoaRepositoryImpl;
	
	public Long maxId(EntityManagerFactory entityManagerFactory) {

		EntityManager em = entityManagerFactory.createEntityManager();

		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<Long> cq = cb.createQuery(Long.class);
			Root<Pessoa> from = cq.from(Pessoa.class);
			TypedQuery<Long> result = em.createQuery(cq.select(cb.max(from.get(Pessoa_.id))));
			return result.getSingleResult();
		} catch (Exception ex) {
			throw new PessoaException(String.format(DbLayerMessage.NO_RESULT_POR_ID.message(), OBJECT, "", null));
		} finally {
			em.clear();
			em.close();
		}
	}

	public Long maxId(EntityManagerFactory entityManagerFactory, Long bancoId) throws CartaoException {

		EntityManager em = entityManagerFactory.createEntityManager();
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<Long> cq = cb.createQuery(Long.class);
			Root<Cartao> from = cq.from(Cartao.class);
			TypedQuery<Long> tQuery = em.createQuery(cq.select(cb.max(from.get(Cartao_.id))));
			return tQuery.getSingleResult();
		} catch (Exception ex) {
			throw new CartaoException(String.format(DbLayerMessage.NO_RESULT_POR_ID.message(), OBJECT, "", bancoId));
		} finally {
			em.clear();
			em.close();
		}
	}
	
	public Pessoa findById(Long id, EntityManagerFactory entityManagerFactory) {
		
		EntityManager em												 		 = entityManagerFactory.createEntityManager();
		
		try {
			CriteriaBuilder cb											 		 = em.getCriteriaBuilder();
			CriteriaQuery<Pessoa> cq 							 				 = cb.createQuery(Pessoa.class);
			Root<Pessoa> from = cq.from(Pessoa.class);
			TypedQuery<Pessoa> tQuery = em
					.createQuery(cq.select(from).where(cb.equal(from.get(Pessoa_.id), id)));
			return tQuery.getSingleResult();
		} catch (NoResultException ex) {
			throw new PessoaException(String.format(DbLayerMessage.NO_RESULT_POR_ID.message(), OBJECT, "",id));
		} finally {
			em.clear();
			em.close();
		}
	}
	
	public List<Pessoa> findAll(Long modFiscalId, EntityManagerFactory entityManagerFactory, Map<String, String> requestParams,  int page, int length) throws PessoaException {
		
		EntityManager em	 											 = entityManagerFactory.createEntityManager();
		try {

			List<Order> orderBy											 = new ArrayList<>();
			CriteriaBuilder cb											 = em.getCriteriaBuilder();
			CriteriaQuery<Pessoa> cq									 = cb.createQuery(Pessoa.class);
			Root<Pessoa> from											 = cq.from(Pessoa.class);

			for (Entry<String, String> entry : requestParams.entrySet()) {
				if (entry.getKey().startsWith(ORDER)) {
					orderBy.add((entry.getValue() == null || entry.getValue().equals("desc")
							? cb.desc(from.get(Pessoa_.id))
							: cb.asc(from.get(Pessoa_.id))));
				}
			}

			TypedQuery<Pessoa> tQuery									 = em.createQuery(cq.select(from).orderBy(orderBy));
			tQuery.setFirstResult((page - 1) * length);
			tQuery.setMaxResults(length);
			return tQuery.getResultList();
		} catch (Exception ex) {
			throw new PessoaException(String.format(MODE, "pagination", OBJECT, ex.getMessage()));
		} finally {
			em.clear();
			em.close();
		}
	}
	
	public List<Pessoa> findByFilters(Long modFiscalId, PessoaDTO entityDto,
			EntityManagerFactory entityManagerFactory, Map<String, String> requestParams, int page, int max)
			throws PessoaException {
		EntityManager em 										     			= entityManagerFactory.createEntityManager();

		try {

			List<Order> orderBy 												 = new ArrayList<>();
			CriteriaBuilder cb 													 = em.getCriteriaBuilder();
			CriteriaQuery<Pessoa> cq 											 = cb.createQuery(Pessoa.class);
			Root<Pessoa> from 													 = cq.from(Pessoa.class);

			requestParams.entrySet().stream()
			.filter(entry -> entry.getKey().startsWith(ORDER))
				.map(e -> {
					return (e.getValue() == null || e.getValue().equals("desc"))
						? cb.desc(from.get(Pessoa_.id))
						: cb.asc(from.get(Pessoa_.id));
	
				}).forEach(orderBy::add);

			List<Predicate> predicates 										     = createPredicates(modFiscalId, entityDto, from, cb);
			TypedQuery<Pessoa> tQuery											 = em
																					.createQuery(cq.select(from)
																						.where(predicates.stream()
																							.toArray(Predicate[]::new))
																								.orderBy(orderBy));
			
			if (page != 0 && max != 0) {
				tQuery.setFirstResult((page - 1) * max);
				tQuery.setMaxResults(max);
			}
			return tQuery.getResultList();
		} catch (Exception ex) {
			throw new PessoaException(String.format(MODE, "buscar por filtros", OBJECT, ex.getMessage()));
		} finally {
			em.clear();
			em.close();
		}
	}
	
	public Long count(Long modFiscalId, PessoaDTO entityDto, EntityManagerFactory entityManagerFactory)
			throws PessoaException {

		EntityManager em = entityManagerFactory.createEntityManager();
		List<Predicate> predicates 												 = new ArrayList<>();

		try {
			CriteriaBuilder cb 													 = em.getCriteriaBuilder();
			CriteriaQuery<Long> cq												 = cb.createQuery(Long.class);
			Root<Pessoa> from 											 = cq.from(Pessoa.class);
			predicates 															 = createPredicates(modFiscalId, entityDto != null 
																						? entityDto : null,	from, cb);
			
			return em.createQuery(cq.select(
						cb.count(from))
							.where(predicates.toArray
								(new Predicate[] {})))
									.getSingleResult();
		} catch (Exception ex) {
			throw new PessoaException(String.format(MODE, "count", OBJECT, ex.getMessage()));
		} finally {
			em.clear();
			em.close();
		}
	}
	
	public List<Predicate> createPredicates(Long enderecoId, PessoaDTO entityDto, Root<Pessoa> from, CriteriaBuilder cb) {

		List<Predicate> predicates = new ArrayList<>();

		if ( enderecoId != null && enderecoId > 0) {
			Join<Pessoa, Endereco> joinEndereco = from.join(Pessoa_.endereco, JoinType.INNER);
			predicates.add(cb.equal(joinEndereco.get(Endereco_.id), enderecoId));
		}
		
		Optional.ofNullable(entityDto).ifPresent(entity -> {

			checkIsNull(entity.getNome()).ifPresent(
					field -> predicates.add(cb.like(from.get(Pessoa_.nome), "%" + field + "%")));

			/*
			checkIsNull(entity.getDescr()).ifPresent(
					field -> predicates.add(cb.like(from.get(Pessoa_.descr), "%" + field + "%")));
			
			checkIsNull(entity.getDtIni()).ifPresent(field -> predicates.add(cb.greaterThanOrEqualTo(from.get(Pessoa_.dtIni), field)));
	    	checkIsNull(entity.getDtFim()).ifPresent(field -> predicates.add(cb.lessThanOrEqualTo(from.get(Pessoa_.dtFim), field)));
	    	*/

		});

		return predicates;
	}
	
	public Optional<Pessoa> findById(Long id) {
		return pessoaRepositoryImpl.findById(id);
	}

	public <S extends Pessoa> S save(S entity) {
		return pessoaRepositoryImpl.save(entity);
	}

	public void delete(Pessoa entity) {
		pessoaRepositoryImpl.delete(entity);
	}

	public <T> Optional<T> checkIsNull(T field) {
		return Optional.ofNullable(field);
	}
}
