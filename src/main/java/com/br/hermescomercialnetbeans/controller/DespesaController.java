
package com.br.hermescomercialnetbeans.controller;

import com.br.hermescomercialnetbeans.dao.DespesaDao;

import com.br.hermescomercialnetbeans.model.Despesa;
import org.apache.logging.log4j.LogManager;


public class DespesaController {

   

    DespesaDao dao = new DespesaDao();
    Despesa despesa;
    private static final org.apache.logging.log4j.Logger logger = LogManager.getLogger(DespesaController.class);

    public void salvar( ){
        try {
        	despesa = new Despesa();
        	
        	
            dao.salvar(despesa);
        } catch (Exception e) {
           logger.info(e.getMessage());

        }
    }

    public void remove( ){
        try {
            dao.remove(txtNome.getText());
        } catch (Exception e) {
           logger.info(e.getMessage());

        }
    }

    public void update(Despesa despesa ){
        try {
            dao.update(despesa);
        } catch (Exception e) {
           logger.info(e.getMessage());

        }
    }

    public void buscar(String nome ){
        try {
            dao.buscar(nome);
        } catch (Exception e) {
           logger.info(e.getMessage());

        }
    }

    public void listar(){
        try {

            dao.listar();

        } catch (Exception e) {
           logger.info(e.getMessage());

        }
    }

}
