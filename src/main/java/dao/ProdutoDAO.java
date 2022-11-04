package dao;

import excecao.ObjetoNaoEncontradoException;
import modelo.Produto;

import java.util.List;

public interface ProdutoDAO {
	long inclui(Produto umProduto);

	void altera(Produto umProduto) throws ObjetoNaoEncontradoException;

	void exclui(long id) throws ObjetoNaoEncontradoException;

	Produto recuperaUmProduto(long numero) throws ObjetoNaoEncontradoException;

	List<Produto> recuperaProdutos();
}