package servico;

import java.util.List;

import dao.MovimentoDAO;
import dao.controle.FabricaDeDAOs;
import modelo.Movimento;
import util.JPAUtil;

public class MovimentoAppService {
	private static MovimentoDAO movimentoDAO = FabricaDeDAOs.getDAO(MovimentoDAO.class);
	
	public List<Movimento> recuperaMovimentos() {
		try {
			List<Movimento> movimentos = movimentoDAO.recuperaMovimentos();

			return movimentos;
		} finally {
			JPAUtil.closeEntityManager();
		}
	}
}