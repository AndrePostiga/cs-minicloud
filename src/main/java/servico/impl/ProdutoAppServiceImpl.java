package servico.impl;

import anotacao.Autowired;
import anotacao.Transactional;
import dao.ProdutoDAO;
import excecao.ObjetoNaoEncontradoException;
import excecao.ProdutoNaoEncontradoException;
import modelo.Produto;
import servico.ProdutoAppService;

import java.util.List;

public class ProdutoAppServiceImpl implements ProdutoAppService {

	@Autowired
	private ProdutoDAO produtoDAO;

	@Transactional
	public long inclui(Produto umProduto) {
		return produtoDAO.inclui(umProduto);
	}

	@Transactional(rollbackFor = { ProdutoNaoEncontradoException.class })
	public void altera(Produto umProduto) throws ProdutoNaoEncontradoException {
		try {
			produtoDAO.altera(umProduto);
		} catch (ObjetoNaoEncontradoException e) {
			throw new ProdutoNaoEncontradoException("Produto não encontrado");
		}
	}

	@Transactional
	public void exclui(long numero) throws ProdutoNaoEncontradoException {
		try {
			produtoDAO.exclui(numero);
		} catch (ObjetoNaoEncontradoException e) {
			throw new ProdutoNaoEncontradoException("Produto não encontrado");
		}
	}

	public Produto recuperaUmProduto(long numero) throws ProdutoNaoEncontradoException {
		try {
			return produtoDAO.recuperaUmProduto(numero);
		} catch (ObjetoNaoEncontradoException e) {
			throw new ProdutoNaoEncontradoException("Produto não encontrado");
		}
	}

	public List<Produto> recuperaProdutos() {
		List<Produto> produtos = produtoDAO.recuperaProdutos();

		return produtos;
	}
}