
package com.br.hermescomercialnetbeans.controller;

import com.br.hermescomercialnetbeans.dao.LoginDao;
import com.br.hermescomercialnetbeans.model.Pessoa;
import com.br.hermescomercialnetbeans.model.Usuario;
import org.apache.logging.log4j.LogManager;

public class PrincipalController {
	
	
    private String login;
    private String senha;
    LoginController loginController;

    private static final org.apache.logging.log4j.Logger logger = LogManager.getLogger(PrincipalController.class);
    private String tipoDeAcesso;
    
    LoginDao dao = new LoginDao();
    
    
    public void setUsuarioLogado(String login, String senha) {
    	this.login = login;
    	this.senha = senha;
    	
    }
    
  
    public Pessoa infoPessoa(String login, String senha) {
        try {
            return dao.acessarPessoa(login, senha);
        } catch (Exception e) {
            logger.error("Erro ao buscar informações do usuário: " + e.getMessage());
        }
        return null;
    }
}
