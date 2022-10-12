package servico;

import java.util.Calendar;
import java.util.List;

import dao.ContaDAO;
import dao.MovimentoDAO;
import dao.controle.FabricaDeDAOs;
import excecao.ContaNaoEncontradaException;
import excecao.InfraestruturaException;
import excecao.ObjetoNaoEncontradoException;
import modelo.Conta;
import modelo.Movimento;
import util.JPAUtil;

public class ContaAppService {
	private static ContaDAO contaDAO = FabricaDeDAOs.getDAO(ContaDAO.class);
	private static MovimentoDAO movimentoDAO = FabricaDeDAOs.getDAO(MovimentoDAO.class);
	
	public void transfereValor(Conta contaDebitada, Conta contaCreditada, double valor, Calendar hoje)
			throws ContaNaoEncontradaException {
		try {
			JPAUtil.beginTransaction();
			
			if (contaDebitada.getId() < contaCreditada.getId()) {          //        t1       t2    
				debita(contaDebitada, valor, hoje);						   //      d 101     c 101
				credita(contaCreditada, valor, hoje);                      //      c 102     d 102
			}
			else {
				credita(contaCreditada, valor, hoje);
				debita(contaDebitada, valor, hoje);
			}

//			Conta umaConta;
//			try {
//				umaConta = contaDAO.recuperaUmaContaComLock(contaDebitada.getId());
//				umaConta.debita(valor);
//				Movimento umMovimento = new Movimento(valor, hoje, "D", contaDebitada);
//				movimentoDAO.inclui(umMovimento);
//			} catch (ObjetoNaoEncontradoException e) {
//				JPAUtil.rollbackTransaction();
//				throw new ContaNaoEncontradaException(
//					"Conta debitada número " + contaDebitada.getId() + " não encontrada");
//			}
//
//			try {
//				umaConta = contaDAO.recuperaUmaContaComLock(contaCreditada.getId());
//				umaConta.credita(valor);
//				Movimento umMovimento = new Movimento(valor, hoje, "C", contaCreditada);
//				movimentoDAO.inclui(umMovimento);
//			} catch (ObjetoNaoEncontradoException e) {
//				JPAUtil.rollbackTransaction();
//				throw new ContaNaoEncontradaException(
//					"Conta creditada número " + contaCreditada.getId() + " não encontrada");
//			}

			JPAUtil.commitTransaction();
		} catch (InfraestruturaException e) {
			try {
				JPAUtil.rollbackTransaction();
			} catch (InfraestruturaException ie) {
			}

			throw e;
		} finally {
			JPAUtil.closeEntityManager();
		}
	}

	public void debita(Conta conta, double valor, Calendar hoje) throws ContaNaoEncontradaException {
		try {
			JPAUtil.beginTransaction();

			Conta umaConta = contaDAO.recuperaUmaContaComLock(conta.getId());

			umaConta.debita(valor);

			Movimento umMovimento = new Movimento(valor, hoje, "D", umaConta);
			
			movimentoDAO.inclui(umMovimento);
			
			JPAUtil.commitTransaction();

		} catch (ObjetoNaoEncontradoException e) {
			JPAUtil.rollbackTransaction();

			throw new ContaNaoEncontradaException("Conta debitada número " + conta.getId() + " não encontrada");
		} catch (InfraestruturaException e) {
			try {
				JPAUtil.rollbackTransaction();
			} catch (InfraestruturaException ie) {
			}

			throw e;
		} finally {
			JPAUtil.closeEntityManager();
		}
	}

	public void credita(Conta conta, double valor, Calendar hoje) throws ContaNaoEncontradaException {
		try {
			JPAUtil.beginTransaction();

			Conta umaConta = contaDAO.recuperaUmaContaComLock(conta.getId());

			umaConta.credita(valor);

			Movimento umMovimento = new Movimento(valor, hoje, "C", umaConta);
			
			movimentoDAO.inclui(umMovimento);

			JPAUtil.commitTransaction();
		
		} catch (ObjetoNaoEncontradoException e) {
			JPAUtil.rollbackTransaction();

			throw new ContaNaoEncontradaException("Conta creditada " + conta.getId() + " não encontrada");
		} catch (InfraestruturaException e) {
			try {
				JPAUtil.rollbackTransaction();
			} catch (InfraestruturaException ie) {
			}

			throw e;
		} finally {
			JPAUtil.closeEntityManager();
		}
	}

	public long inclui(Conta umConta) {
		try {
			// NENHUMA VALIDAÇÃO ESTÁ SENDO REALIZADA AQUI!!!

			JPAUtil.beginTransaction();

			long numero = contaDAO.inclui(umConta);

			JPAUtil.commitTransaction();

			return numero;
		} catch (InfraestruturaException e) {
			try {
				JPAUtil.rollbackTransaction();
			} catch (InfraestruturaException ie) {
			}

			throw e;
		} finally {
			JPAUtil.closeEntityManager();
		}
	}

	public void altera(Conta conta) throws ContaNaoEncontradaException {
		try {
			JPAUtil.beginTransaction();

			// O objeto recuperado é inserido na lista de objetos monitorados do entity manager.
			contaDAO.recuperaUmaContaComLock(conta.getId());

			contaDAO.altera(conta);

			JPAUtil.commitTransaction();
		} catch (ObjetoNaoEncontradoException e) {
			JPAUtil.rollbackTransaction();

			throw new ContaNaoEncontradaException("Conta não encontrada");
		} catch (InfraestruturaException e) {
			try {
				JPAUtil.rollbackTransaction();
			} catch (InfraestruturaException ie) {
			}

			throw e;
		} finally {
			JPAUtil.closeEntityManager();
		}
	}

	public void exclui(long numero) throws ContaNaoEncontradaException {
		try {
			JPAUtil.beginTransaction();

			contaDAO.exclui(numero);

			JPAUtil.commitTransaction();
		} catch (ObjetoNaoEncontradoException e) {
			JPAUtil.rollbackTransaction();

			throw new ContaNaoEncontradaException("Conta não encontrado");
		} catch (InfraestruturaException e) {
			try {
				JPAUtil.rollbackTransaction();
			} catch (InfraestruturaException ie) {
			}

			throw e;
		} finally {
			JPAUtil.closeEntityManager();
		}
	}

	public Conta recuperaUmaConta(long numero) throws ContaNaoEncontradaException {
		try {
			Conta umConta = contaDAO.recuperaUmaConta(numero);

			return umConta;
		} catch (ObjetoNaoEncontradoException e) {
			throw new ContaNaoEncontradaException("Conta não encontrada");
		} finally {
			JPAUtil.closeEntityManager();
		}
	}

	public List<Conta> recuperaContas() {
		try {
			List<Conta> contas = contaDAO.recuperaContas();

			return contas;
		} finally {
			JPAUtil.closeEntityManager();
		}
	}
}