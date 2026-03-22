package com.br.hermescomercialnetbeans.Repository;

import java.util.List;

import com.br.hermescomercialnetbeans.model.Usuario;

public interface RepositoryUsuario {
	
	
	void salvar(Usuario usuario);
	
	void remove(String nome);
	
	void update(Usuario usuario);
	
	List<Usuario> lista();
	
	List<Usuario> buscar(String nome);
	
	
	

}
