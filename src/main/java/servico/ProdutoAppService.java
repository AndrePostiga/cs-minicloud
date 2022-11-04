package servico;

import excecao.ProdutoNaoEncontradoException;
import modelo.Produto;

import java.util.List;

public interface ProdutoAppService {
	long inclui(Produto umProduto);

	void altera(Produto umProduto) throws ProdutoNaoEncontradoException;

	void exclui(long numero) throws ProdutoNaoEncontradoException;

	Produto recuperaUmProduto(long numero) throws ProdutoNaoEncontradoException;

	List<Produto> recuperaProdutos();
}