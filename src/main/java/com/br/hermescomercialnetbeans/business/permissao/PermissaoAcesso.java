
package com.br.hermescomercialnetbeans.business.permissao;

import com.br.hermescomercialnetbeans.dao.UsuarioDao;
import com.br.hermescomercialnetbeans.model.Pessoa;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PermissaoAcesso {

    private static final Logger logger = LogManager.getLogger(PermissaoAcesso.class);
    UsuarioDao dao;

    public void acessoAcao(Pessoa pessoa){
        try {
            if(pessoa != null){
                 dao.buscar(pessoa.getTipoPessoa());

            }
        } catch (Exception e) {
            logger.error("Error saving alert", e);

        }
    }

}
