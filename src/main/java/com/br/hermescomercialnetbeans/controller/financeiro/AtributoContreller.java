
package com.br.hermescomercialnetbeans.controller.financeiro;

import com.br.hermescomercialnetbeans.dao.AtributoDao;
import com.br.hermescomercialnetbeans.model.Atributo;
import org.apache.logging.log4j.LogManager;

import java.util.Collections;
import java.util.List;


public class AtributoContreller {

    AtributoDao dao;

    private static final org.apache.logging.log4j.Logger logger = LogManager.getLogger(AtributoContreller.class);

    public Boolean salvar(Atributo atributo){
        try {
            if(atributo != null){
                dao.salvar(atributo);
                return true;
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Atributo> listar(){
        try {

            return dao.listar();


        } catch (Exception e) {
            e.printStackTrace();

        }
        return Collections.emptyList();

    }

    public void remove(String nome){
        try {
            dao.remove(nome);

        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    public void update(Atributo atributo){
        try {
            dao.update(atributo);

        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    public void buscar(String nome){
        try {
            dao.buscar(nome);

        } catch (Exception e) {
            e.printStackTrace();

        }
    }
}
