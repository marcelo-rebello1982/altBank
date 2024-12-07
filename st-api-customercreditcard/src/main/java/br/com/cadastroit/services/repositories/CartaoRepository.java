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
import javax.persistence.criteria.From;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.SingularAttribute;

import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.gson.Gson;

import br.com.cadastroit.services.api.domain.Cartao;
import br.com.cadastroit.services.api.domain.Cartao_;
import br.com.cadastroit.services.api.domain.Conta;
import br.com.cadastroit.services.api.domain.Conta_;
import br.com.cadastroit.services.api.domain.Pessoa;
import br.com.cadastroit.services.api.domain.Pessoa_;
import br.com.cadastroit.services.api.enums.DbLayerMessage;
import br.com.cadastroit.services.exceptions.CartaoException;
import br.com.cadastroit.services.repositories.impl.CartaoRepositoryImpl;
import br.com.cadastroit.services.web.mapper.CartaoMapper;
import br.com.cadastroit.sevices.web.dto.CartaoDTO;
import lombok.NoArgsConstructor;

@Repository
@NoArgsConstructor
@Transactional(propagation = Propagation.REQUIRED)
public class CartaoRepository implements Serializable {
	
	private static final long serialVersionUID = 4649386654025041110L;
	
	private static final String MODE = "Error on %s mode to %s, [error] = %s";
	private static final String OBJECT = "CARTAO";
	private static final String ORDER = "order";
	
	@Autowired
	private CartaoRepositoryImpl cartaoRepositoryImpl;
	
	@Autowired
	private Gson gson;
	
	protected final CartaoMapper cartaoMapper = Mappers.getMapper(CartaoMapper.class);

	public Long maxId(EntityManagerFactory entityManagerFactory) {

		EntityManager em = entityManagerFactory.createEntityManager();

		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<Long> cq = cb.createQuery(Long.class);
			Root<Cartao> from = cq.from(Cartao.class);
			TypedQuery<Long> result = em.createQuery(cq.select(cb.max(from.get(Cartao_.id))));
			return result.getSingleResult();
		} catch (Exception ex) {
			throw new CartaoException(String.format(DbLayerMessage.NO_RESULT_POR_ID.message(), OBJECT, "", null));
		} finally {
			em.clear();
			em.close();
		}
	}

	public Long maxId(EntityManagerFactory entityManagerFactory, Long pessoaId) throws CartaoException {

		EntityManager em = entityManagerFactory.createEntityManager();
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<Long> cq = cb.createQuery(Long.class);
			Root<Cartao> from = cq.from(Cartao.class);
			TypedQuery<Long> tQuery = em.createQuery(cq.select(cb.max(from.get(Cartao_.id))));
			return tQuery.getSingleResult();
		} catch (Exception ex) {
			throw new CartaoException(String.format(DbLayerMessage.NO_RESULT_POR_ID.message(), OBJECT, "", pessoaId));
		} finally {
			em.clear();
			em.close();
		}
	}

	public Cartao findById(Long id, EntityManagerFactory entityManagerFactory) {

		EntityManager em = entityManagerFactory.createEntityManager();
		
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<Cartao> cq = cb.createQuery(Cartao.class);
			Root<Cartao> from = cq.from(Cartao.class);
			TypedQuery<Cartao> tQuery = em.createQuery(cq.select(from).where(cb.equal(from.get(Cartao_.id), id)));
			return tQuery.getSingleResult();
		} catch (NoResultException ex) {
			throw new CartaoException(String.format(DbLayerMessage.NO_RESULT_POR_ID.message(), OBJECT, "", id));
		} finally {
			em.clear();
			em.close();
		}
	}

	public Cartao findById(Long pessoaId, Long id, EntityManagerFactory entityManagerFactory) {

		EntityManager em = entityManagerFactory.createEntityManager();

		try {

			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<Cartao> cq = cb.createQuery(Cartao.class);
			Root<Cartao> from = cq.from(Cartao.class);
			Join<Cartao, Pessoa> joinPessoa = from.join(Cartao_.pessoa, JoinType.INNER);
			TypedQuery<Cartao> tQuery = em.createQuery(cq.select(from)
					.where(cb.equal(joinPessoa.get(Pessoa_.id), pessoaId), cb.equal(from.get(Cartao_.id), id)));
			return tQuery.getSingleResult();
		} catch (NoResultException ex) {
			throw new CartaoException(String.format(DbLayerMessage.NO_RESULT_POR_ID.message(), OBJECT, "", pessoaId));
		} finally {
			em.clear();
			em.close();
		}
	}

	public Long count(Long pessoaId, Long contaId, CartaoDTO entityDto, EntityManagerFactory entityManagerFactory) throws CartaoException {

		EntityManager em = entityManagerFactory.createEntityManager();
		List<Predicate> predicates = new ArrayList<>();

		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<Long> cq = cb.createQuery(Long.class);
			Root<Cartao> from = cq.from(Cartao.class);
			predicates = this.createPredicates(pessoaId, contaId, entityDto != null ? entityDto : null, from, cb);

			return em.createQuery(cq.select(cb.count(from)).where(predicates.toArray(new Predicate[] {}))).getSingleResult();

		} catch (Exception ex) {
			throw new CartaoException(String.format(MODE, "count", OBJECT, ex.getMessage()));
		} finally {
			em.clear();
			em.close();
		}
	}

	public List<Cartao> findAll(Long pessoaId, EntityManagerFactory entityManagerFactory, Map<String, String> requestParams, int page, int length)
			throws CartaoException {

		EntityManager em = entityManagerFactory.createEntityManager();
		try {

			List<Order> orderBy = new ArrayList<>();
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<Cartao> cq = cb.createQuery(Cartao.class);
			Root<Cartao> from = cq.from(Cartao.class);
			Join<Cartao, Pessoa> joinPessoa = from.join(Cartao_.pessoa, JoinType.INNER);

			for (Entry<String, String> entry : requestParams.entrySet()) {
				if (entry.getKey().startsWith(ORDER)) {
					orderBy.add((entry.getValue() == null || entry.getValue().equals("desc") ? cb.desc(from.get(Cartao_.id))
							: cb.asc(from.get(Cartao_.id))));
				}
			}

			TypedQuery<Cartao> tQuery = em.createQuery(
					cq.select(from).where(cb.equal(joinPessoa.get(Pessoa_.id), pessoaId)).orderBy(orderBy));
			
			List<Cartao> cartoes = tQuery.getResultList();

			cartoes.stream().forEach(pessoa -> {
			});

			tQuery.setFirstResult((page - 1) * length);
			tQuery.setMaxResults(length);
			return cartoes;
		} catch (Exception ex) {
			throw new CartaoException(String.format(MODE, "pagination", OBJECT, ex.getMessage()));
		} finally {
			em.clear();
			em.close();
		}
	}

	public List<Cartao> findByFilters(Long pessoaId, CartaoDTO dto, EntityManagerFactory entityManagerFactory, Map<String, String> requestParams, int page, int max)
			throws CartaoException {

		EntityManager em = entityManagerFactory.createEntityManager();

		try {

			List<Order> orderBy = new ArrayList<>();
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<Cartao> cq = cb.createQuery(Cartao.class);
			Root<Cartao> from = cq.from(Cartao.class);

			// aqui poderia receber além do parametro para ordenar a lista por desc/asc, poderia ser
			// adicionado outros paramêtros conforme necessário.
			
			for (Entry<String, String> entry : requestParams.entrySet()) {
				if (entry.getKey().startsWith(ORDER)) {
					orderBy.add((entry.getValue() == null || entry.getValue().equals("desc") ? cb.desc(from.get(Cartao_.id))
							: cb.asc(from.get(Cartao_.id))));
				}
			}

			List<Predicate> predicates = this.createPredicates(pessoaId, null, dto, from, cb);
			TypedQuery<Cartao> tQuery = em.createQuery(cq.select(from).where(predicates.stream().toArray(Predicate[]::new)).orderBy(orderBy));
			if (page != 0 && max != 0) {
				tQuery.setFirstResult((page - 1) * max);
				tQuery.setMaxResults(max);
			}
			return tQuery.getResultList();
		} catch (Exception ex) {
			throw new CartaoException(String.format(MODE, "buscar por filtros", OBJECT, ex.getMessage()));
		} finally {
			em.clear();
			em.close();
		}
	}

	public List<Predicate> createPredicates(Long pessoaId, Long contaId, CartaoDTO entityDto, Root<Cartao> from, CriteriaBuilder cb) {

		List<Predicate> predicates = new ArrayList<>();

		if (isValidId(pessoaId) && contaId == null) {
			
			Join<Cartao, Pessoa> joinPessoa = from.join(Cartao_.pessoa, JoinType.INNER);
			predicates.add(cb.equal(joinPessoa.get(Pessoa_.id), pessoaId));
			
		} else if (isValidId(contaId) && pessoaId == null) {
			
			Join<Cartao, Conta> joinConta = from.join(Cartao_.conta, JoinType.INNER);
			predicates.add(cb.equal(joinConta.get(Conta_.id), contaId));
			
		} 

		if (entityDto != null) {

			// if (entityDto.getDataInicio() != null) {
			// predicates.add(cb.greaterThanOrEqualTo(joinTarefa.get(Tarefa_.dataInicio), entityDto.getDataInicio()));
			//
			// }

			// if (entityDto.getDataFinal() != null) {
			// predicates.add(cb.lessThanOrEqualTo(joinTarefa.get(Tarefa_.dataFinal), entityDto.getDataFinal()));
			// }

		}

		return predicates;
	}
	
	

	public Cartao handleUpdateAprovedPayment(Cartao entity, EntityManagerFactory entityManagerFactory) throws JsonProcessingException {

		// cartaoService.validarCartao(entity);

		// Cartao CartaoAtualizado = CartaoClient.handleUpdateAprovedPayment(entity.getId(), CartaoService.convertToJson(entity));

		return null; // CartaoAtualizado;
	}

	public Optional<Cartao> findById(Long id) {
		return cartaoRepositoryImpl.findById(id);
	}

	public <S extends Cartao> S save(S entity) {
		return cartaoRepositoryImpl.save(entity);
	}

	public void delete(Cartao entity) {
		cartaoRepositoryImpl.delete(entity);
	}

	public <T> Optional<T> checkIsNull(T field) {
		return Optional.ofNullable(field);
	}
	
	private boolean isValidId(Long id) {
	    return id != null && id > 0L;
	}
}
