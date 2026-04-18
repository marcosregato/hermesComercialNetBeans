
package com.br.hermescomercialnetbeans.business.permissao;

import com.br.hermescomercialnetbeans.dao.UsuarioDao;
import com.br.hermescomercialnetbeans.model.Usuario;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PermissaoAcesso {

    private static final Logger logger = LogManager.getLogger(PermissaoAcesso.class);
    UsuarioDao dao;

    public void acessoAcao(Usuario usuario){
        try {
            if(usuario != null){
                 dao.buscarPorLogin(usuario.getNome());

            }
        } catch (Exception e) {
            logger.error("Error saving alert", e);

        }
    }

}
