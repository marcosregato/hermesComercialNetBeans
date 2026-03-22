package com.br.hermescomercialnetbeans.Repository;

import java.util.List;

import com.br.hermescomercialnetbeans.model.Estoque;
import com.br.hermescomercialnetbeans.model.Produto;

public interface RepositoryEstoque {
	
	void salvar(Estoque estoque);

	void remove(String nome);

	void update(Estoque produto);

	List<Estoque> listar();

	List<Estoque> buscar(String nome);

}
