package com.br.hermescomercialnetbeans.Repository;

import java.util.List;

import com.br.hermescomercialnetbeans.model.Login;

public interface RepositoryLogin {
	
void salvar(Login produto);
	
	void delete(String nome);
	
	void update(Login produto);
	
	List<Login> lista();
	
	List<Login> buscar();

}
