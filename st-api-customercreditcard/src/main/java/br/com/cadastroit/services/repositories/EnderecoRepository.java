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
import br.com.cadastroit.services.exceptions.EnderecoException;
import br.com.cadastroit.services.exceptions.PessoaException;
import br.com.cadastroit.services.repositories.impl.EnderecoRepositoryImpl;
import br.com.cadastroit.sevices.web.dto.EnderecoDTO;
import lombok.NoArgsConstructor;

@Repository
@NoArgsConstructor
@Transactional(propagation = Propagation.REQUIRED)
public class EnderecoRepository implements Serializable {
	
	private static final long serialVersionUID = -3259206557782549360L;
	
	private static final String MODE = "Error on %s mode to %s, [error] = %s";
	private static final String OBJECT = "ENDERECO";
	private static final String ORDER = "order";
	
	@Autowired
	private EnderecoRepositoryImpl enderecoRepositoryImpl;
	
	public Long maxId(EntityManagerFactory entityManagerFactory) {

		EntityManager em = entityManagerFactory.createEntityManager();

		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<Long> cq = cb.createQuery(Long.class);
			Root<Endereco> from = cq.from(Endereco.class);
			TypedQuery<Long> result = em.createQuery(cq.select(cb.max(from.get(Endereco_.id))));
			return result.getSingleResult();
		} catch (Exception ex) {
			throw new EnderecoException(String.format(DbLayerMessage.NO_RESULT_POR_ID.message(), OBJECT, "", null));
		} finally {
			em.clear();
			em.close();
		}
	}

	public Long maxId(EntityManagerFactory entityManagerFactory, Long pessoaId) throws PessoaException {

		EntityManager em = entityManagerFactory.createEntityManager();
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<Long> cq = cb.createQuery(Long.class);
			Root<Cartao> from = cq.from(Cartao.class);
			TypedQuery<Long> tQuery = em.createQuery(cq.select(cb.max(from.get(Cartao_.id))));
			return tQuery.getSingleResult();
		} catch (Exception ex) {
			throw new PessoaException(String.format(DbLayerMessage.NO_RESULT_POR_ID.message(), OBJECT, "", pessoaId));
		} finally {
			em.clear();
			em.close();
		}
	}
	
	public Endereco findById(Long id, EntityManagerFactory entityManagerFactory) {
		
		EntityManager em												 		 = entityManagerFactory.createEntityManager();
		
		try {
			CriteriaBuilder cb											 		 = em.getCriteriaBuilder();
			CriteriaQuery<Endereco> cq 							 				 = cb.createQuery(Endereco.class);
			Root<Endereco> from = cq.from(Endereco.class);
			TypedQuery<Endereco> tQuery = em
					.createQuery(cq.select(from).where(cb.equal(from.get(Endereco_.id), id)));
			return tQuery.getSingleResult();
		} catch (NoResultException ex) {
			throw new EnderecoException(String.format(DbLayerMessage.NO_RESULT_POR_ID.message(), OBJECT, "",id));
		} finally {
			em.clear();
			em.close();
		}
	}
	
	public List<Endereco> findAll(Long pessoaId, EntityManagerFactory entityManagerFactory, Map<String, String> requestParams,  int page, int length) throws EnderecoException {
		
		EntityManager em	 											 = entityManagerFactory.createEntityManager();
		try {

			List<Order> orderBy											 = new ArrayList<>();
			CriteriaBuilder cb											 = em.getCriteriaBuilder();
			CriteriaQuery<Endereco> cq									 = cb.createQuery(Endereco.class);
			Root<Endereco> from											 = cq.from(Endereco.class);

			for (Entry<String, String> entry : requestParams.entrySet()) {
				if (entry.getKey().startsWith(ORDER)) {
					orderBy.add((entry.getValue() == null || entry.getValue().equals("desc")
							? cb.desc(from.get(Endereco_.id))
							: cb.asc(from.get(Endereco_.id))));
				}
			}

			TypedQuery<Endereco> tQuery									 = em.createQuery(cq.select(from).orderBy(orderBy));
			tQuery.setFirstResult((page - 1) * length);
			tQuery.setMaxResults(length);
			return tQuery.getResultList();
		} catch (Exception ex) {
			throw new EnderecoException(String.format(MODE, "pagination", OBJECT, ex.getMessage()));
		} finally {
			em.clear();
			em.close();
		}
	}
	
	public List<Endereco> findByFilters(Long pessoaId, EnderecoDTO entityDto,
			EntityManagerFactory entityManagerFactory, Map<String, String> requestParams, int page, int max)
			throws EnderecoException {
		EntityManager em 										     			= entityManagerFactory.createEntityManager();

		try {

			List<Order> orderBy 												 = new ArrayList<>();
			CriteriaBuilder cb 													 = em.getCriteriaBuilder();
			CriteriaQuery<Endereco> cq 											 = cb.createQuery(Endereco.class);
			Root<Endereco> from 													 = cq.from(Endereco.class);

			requestParams.entrySet().stream().filter(entry -> entry.getKey().startsWith(ORDER))
				.map(e -> {
					return (e.getValue() == null || e.getValue().equals("desc"))
						? cb.desc(from.get(Endereco_.id))
						: cb.asc(from.get(Endereco_.id));
	
			}).forEach(orderBy::add);

			List<Predicate> predicates 										     = createPredicates(pessoaId, entityDto, from, cb);
			TypedQuery<Endereco> tQuery											 = em
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
			throw new EnderecoException(String.format(MODE, "buscar por filtros", OBJECT, ex.getMessage()));
		} finally {
			em.clear();
			em.close();
		}
	}
	
	public Long count(Long pessoaId, EnderecoDTO entityDto, EntityManagerFactory entityManagerFactory)
			throws EnderecoException {

		EntityManager em = entityManagerFactory.createEntityManager();
		List<Predicate> predicates 												 = new ArrayList<>();

		try {
			CriteriaBuilder cb 													 = em.getCriteriaBuilder();
			CriteriaQuery<Long> cq												 = cb.createQuery(Long.class);
			Root<Endereco> from 												 = cq.from(Endereco.class);
			predicates 															 = createPredicates(pessoaId, entityDto != null 
																						? entityDto : null,	from, cb);
			
			return em.createQuery(cq.select(
						cb.count(from))
							.where(predicates.toArray
								(new Predicate[] {})))
									.getSingleResult();
		} catch (Exception ex) {
			throw new EnderecoException(String.format(MODE, "count", OBJECT, ex.getMessage()));
		} finally {
			em.clear();
			em.close();
		}
	}
	
	public List<Predicate> createPredicates(Long pessoaId, EnderecoDTO entityDto, Root<Endereco> from, CriteriaBuilder cb) {

		List<Predicate> predicates = new ArrayList<>();

		if ( pessoaId != null && pessoaId > 0) {
			Join<Endereco, Pessoa> joinPessoa = from.join(Endereco_.pessoas, JoinType.INNER);
			predicates.add(cb.equal(joinPessoa.get(Pessoa_.id), pessoaId));
		}
		
		Optional.ofNullable(entityDto).ifPresent(entity -> {

			checkIsNull(entity.getBairro()).ifPresent(
					field -> predicates.add(cb.like(from.get(Endereco_.bairro), "%" + field + "%")));

		});

		return predicates;
	}
	
	public Optional<Endereco> findById(Long id) {
		return enderecoRepositoryImpl.findById(id);
	}

	public <S extends Endereco> S save(S entity) {
		return enderecoRepositoryImpl.save(entity);
	}

	public void delete(Endereco entity) {
		enderecoRepositoryImpl.delete(entity);
	}

	public <T> Optional<T> checkIsNull(T field) {
		return Optional.ofNullable(field);
	}
}
