package dao;

import java.util.List;

import modelo.Movimento;

public interface MovimentoDAO {
	long inclui(Movimento umMovimento);

	List<Movimento> recuperaMovimentos();
}