package dao.impl;

import java.util.List;

import javax.persistence.EntityManager;

import dao.MovimentoDAO;
import excecao.InfraestruturaException;
import modelo.Movimento;
import util.JPAUtil;

public class MovimentoDAOImpl implements MovimentoDAO {
	
	public long inclui(Movimento umMovimento) {
		try {
			EntityManager em = JPAUtil.getEntityManager();

			em.persist(umMovimento);

			return umMovimento.getId();
			
		} catch (RuntimeException e) {
			throw new InfraestruturaException(e);
		}
	}

	@SuppressWarnings("unchecked")
	public List<Movimento> recuperaMovimentos() {
		try {
			EntityManager em = JPAUtil.getEntityManager();

			List<Movimento> movimentos = em.createQuery(
				"select m from Movimento m order by m.id asc").getResultList();

			return movimentos;
		} catch (RuntimeException e) {
			throw new InfraestruturaException(e);
		}
	}
}