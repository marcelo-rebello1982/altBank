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

import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.gson.Gson;

import br.com.cadastroit.services.api.domain.Conta;
import br.com.cadastroit.services.api.domain.Conta_;
import br.com.cadastroit.services.api.domain.Fisica;
import br.com.cadastroit.services.api.domain.Fisica_;
import br.com.cadastroit.services.api.domain.Pessoa;
import br.com.cadastroit.services.api.domain.Pessoa_;
import br.com.cadastroit.services.api.enums.DbLayerMessage;
import br.com.cadastroit.services.exceptions.ContaException;
import br.com.cadastroit.services.repositories.impl.ContaRepositoryImpl;
import br.com.cadastroit.services.web.mapper.ContaMapper;
import br.com.cadastroit.sevices.web.dto.ContaDTO;
import br.com.cadastroit.sevices.web.dto.FisicaDTO;
import lombok.NoArgsConstructor;

@Repository
@NoArgsConstructor
@Transactional(propagation = Propagation.REQUIRED)
public class ContaRepository implements Serializable {
	
	private static final long serialVersionUID = 4649386654025041110L;
	
	private static final String MODE = "Error on %s mode to %s, [error] = %s";
	private static final String OBJECT = "CONTA";
	private static final String ORDER = "order";
	
	@Autowired
	private ContaRepositoryImpl contaRepositoryImpl;
	
	@Autowired
	private Gson gson;
	
	protected final ContaMapper cartaoMapper = Mappers.getMapper(ContaMapper.class);

	public Long maxId(EntityManagerFactory entityManagerFactory) {

		EntityManager em = entityManagerFactory.createEntityManager();

		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<Long> cq = cb.createQuery(Long.class);
			Root<Conta> from = cq.from(Conta.class);
			TypedQuery<Long> result = em.createQuery(cq.select(cb.max(from.get(Conta_.id))));
			return result.getSingleResult();
		} catch (Exception ex) {
			throw new ContaException(String.format(DbLayerMessage.NO_RESULT_POR_ID.message(), OBJECT, "", null));
		} finally {
			em.clear();
			em.close();
		}
	}

	public Long maxId(EntityManagerFactory entityManagerFactory, Long pessoaId) throws ContaException {

		EntityManager em = entityManagerFactory.createEntityManager();
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<Long> cq = cb.createQuery(Long.class);
			Root<Conta> from = cq.from(Conta.class);
			TypedQuery<Long> tQuery = em.createQuery(cq.select(cb.max(from.get(Conta_.id))));
			return tQuery.getSingleResult();
		} catch (Exception ex) {
			throw new ContaException(String.format(DbLayerMessage.NO_RESULT_POR_ID.message(), OBJECT, "", pessoaId));
		} finally {
			em.clear();
			em.close();
		}
	}

	public Conta findById(Long id, EntityManagerFactory entityManagerFactory) {

		EntityManager em = entityManagerFactory.createEntityManager();
		
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<Conta> cq = cb.createQuery(Conta.class);
			Root<Conta> from = cq.from(Conta.class);
			TypedQuery<Conta> tQuery = em.createQuery(cq.select(from).where(cb.equal(from.get(Conta_.id), id)));
			return tQuery.getSingleResult();
		} catch (NoResultException ex) {
			throw new ContaException(String.format(DbLayerMessage.NO_RESULT_POR_ID.message(), OBJECT, "", id));
		} finally {
			em.clear();
			em.close();
		}
	}

	public Conta findById(Long id, Long pessoaId, EntityManagerFactory entityManagerFactory) {

		EntityManager em = entityManagerFactory.createEntityManager();

		try {

			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<Conta> cq = cb.createQuery(Conta.class);
			Root<Conta> from = cq.from(Conta.class);
			Join<Conta, Pessoa> joinPessoa = from.join(Conta_.pessoa, JoinType.INNER);
			TypedQuery<Conta> tQuery = null;
			
			if (id != null && id > 0L) {
				
				tQuery = em.createQuery(cq.select(from).where(cb.equal(from.get(Conta_.id), id)));
				
			} else if (id == 0 && pessoaId > 0L) {
				
				tQuery = em.createQuery(cq.select(from).where(cb.equal(joinPessoa.get(Pessoa_.id), pessoaId)));	
			}
			
			return tQuery.getSingleResult();
		} catch (NoResultException ex) {
			throw new ContaException(String.format(DbLayerMessage.NO_RESULT_POR_ID.message(), OBJECT, "", id));
		} finally {
			em.clear();
			em.close();
		}
	}

	public Long count(Long pessoaId, Long contaId , ContaDTO entityDto, EntityManagerFactory entityManagerFactory) throws ContaException {

		EntityManager em = entityManagerFactory.createEntityManager();
		List<Predicate> predicates = new ArrayList<>();

		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<Long> cq = cb.createQuery(Long.class);
			Root<Conta> from = cq.from(Conta.class);
			predicates = this.createPredicates(pessoaId, contaId, entityDto != null ? entityDto : null, from, cb);

			return em.createQuery(cq.select(cb.count(from)).where(predicates.toArray(new Predicate[] {}))).getSingleResult();

		} catch (Exception ex) {
			throw new ContaException(String.format(MODE, "count", OBJECT, ex.getMessage()));
		} finally {
			em.clear();
			em.close();
		}
	}

	public List<Conta> findAll(Long pessoaId, EntityManagerFactory entityManagerFactory, Map<String, String> requestParams, int page, int length)
			throws ContaException {

		EntityManager em = entityManagerFactory.createEntityManager();
		try {

			List<Order> orderBy = new ArrayList<>();
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<Conta> cq = cb.createQuery(Conta.class);
			Root<Conta> from = cq.from(Conta.class);
			Join<Conta, Pessoa> joinPessoa = from.join(Conta_.pessoa, JoinType.INNER);

			for (Entry<String, String> entry : requestParams.entrySet()) {
				if (entry.getKey().startsWith(ORDER)) {
					orderBy.add((entry.getValue() == null || entry.getValue().equals("desc") ? cb.desc(from.get(Conta_.id))
							: cb.asc(from.get(Conta_.id))));
				}
			}

			TypedQuery<Conta> tQuery = em.createQuery(
					cq.select(from).where(cb.equal(joinPessoa.get(Pessoa_.id), pessoaId)).orderBy(orderBy));
			
			List<Conta> cartoes = tQuery.getResultList();

			cartoes.stream().forEach(pessoa -> {
				
			});

			tQuery.setFirstResult((page - 1) * length);
			tQuery.setMaxResults(length);
			return cartoes;
		} catch (Exception ex) {
			throw new ContaException(String.format(MODE, "pagination", OBJECT, ex.getMessage()));
		} finally {
			em.clear();
			em.close();
		}
	}

	public List<Conta> findByFilters(Long pessoaId, ContaDTO dto, EntityManagerFactory entityManagerFactory, Map<String, String> requestParams, int page, int max)
			throws ContaException {

		EntityManager em = entityManagerFactory.createEntityManager();

		try {

			List<Order> orderBy = new ArrayList<>();
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<Conta> cq = cb.createQuery(Conta.class);
			Root<Conta> from = cq.from(Conta.class);

			// aqui poderia receber além do parametro para ordenar a lista por desc/asc, poderia ser
			// adicionado outros paramêtros conforme necessário.
			
			for (Entry<String, String> entry : requestParams.entrySet()) {
				if (entry.getKey().startsWith(ORDER)) {
					orderBy.add((entry.getValue() == null || entry.getValue().equals("desc") ? cb.desc(from.get(Conta_.id))
							: cb.asc(from.get(Conta_.id))));
				}
			}

			List<Predicate> predicates = this.createPredicates(pessoaId, null, dto, from, cb);
			TypedQuery<Conta> tQuery = em.createQuery(cq.select(from).where(predicates.stream().toArray(Predicate[]::new)).orderBy(orderBy));
			if (page != 0 && max != 0) {
				tQuery.setFirstResult((page - 1) * max);
				tQuery.setMaxResults(max);
			}
			return tQuery.getResultList();
		} catch (Exception ex) {
			throw new ContaException(String.format(MODE, "buscar por filtros", OBJECT, ex.getMessage()));
		} finally {
			em.clear();
			em.close();
		}
	}
	
	public List<Conta> findByAccount(Long contaId, ContaDTO dto, EntityManagerFactory entityManagerFactory, Map<String, String> requestParams, int page, int max)
			throws ContaException {

		EntityManager em = entityManagerFactory.createEntityManager();

		try {

			List<Order> orderBy = new ArrayList<>();
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<Conta> cq = cb.createQuery(Conta.class);
			Root<Conta> from = cq.from(Conta.class);

			// aqui poderia receber além do parametro para ordenar a lista por desc/asc, poderia ser
			// adicionado outros paramêtros conforme necessário.
			
			for (Entry<String, String> entry : requestParams.entrySet()) {
				if (entry.getKey().startsWith(ORDER)) {
					orderBy.add((entry.getValue() == null || entry.getValue().equals("desc") ? cb.desc(from.get(Conta_.id))
							: cb.asc(from.get(Conta_.id))));
				}
			}

			List<Predicate> predicates = this.createPredicates(contaId, null, dto, from, cb);
			TypedQuery<Conta> tQuery = em.createQuery(cq.select(from).where(predicates.stream().toArray(Predicate[]::new)).orderBy(orderBy));
			if (page != 0 && max != 0) {
				tQuery.setFirstResult((page - 1) * max);
				tQuery.setMaxResults(max);
			}
			return tQuery.getResultList();
		} catch (Exception ex) {
			throw new ContaException(String.format(MODE, "buscar por filtros", OBJECT, ex.getMessage()));
		} finally {
			em.clear();
			em.close();
		}
	}

	public List<Predicate> createPredicates(Long pessoaId, Long contaId, ContaDTO entityDto, Root<Conta> from, CriteriaBuilder cb) {

		List<Predicate> predicates = new ArrayList<>();
		
		Join<Conta, Pessoa> joinPessoa = from.join(Conta_.pessoa, JoinType.INNER);

		if (pessoaId != 0L && pessoaId > 0L) {
			
			predicates.add(cb.equal(joinPessoa.get(Pessoa_.id), pessoaId));
			
		if (entityDto != null) {
			
			//{
			//    "pessoa": {
			//        "fisica": {
			//            "numCpf": 27769157803
			//        }
			//    }
			// }
			
			if (entityDto.getPessoa().getFisica().getNumCpf() != null) {
				
				FisicaDTO fisica = entityDto.getPessoa().getFisica();
				Join<Pessoa, Fisica> joinFisica = joinPessoa.join(Pessoa_.fisica, JoinType.INNER);
				predicates.add(cb.equal(joinFisica.get(Fisica_.numCpf), fisica.getNumCpf()));
				
				}
			}
		}

		return predicates;
	}

	public Conta handleUpdateAprovedPayment(Conta entity, EntityManagerFactory entityManagerFactory) throws JsonProcessingException {

		// cartaoService.validarConta(entity);

		// Conta ContaAtualizado = ContaClient.handleUpdateAprovedPayment(entity.getId(), ContaService.convertToJson(entity));

		return null; // ContaAtualizado;
	}

	public Optional<Conta> findById(Long id) {
		return contaRepositoryImpl.findById(id);
	}

	public <S extends Conta> S save(S entity) {
		return contaRepositoryImpl.save(entity);
	}

	public void delete(Conta entity) {
		contaRepositoryImpl.delete(entity);
	}

	public <T> Optional<T> checkIsNull(T field) {
		return Optional.ofNullable(field);
	}
}
