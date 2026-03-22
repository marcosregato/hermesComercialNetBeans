
package com.br.hermescomercialnetbeans.controller.produto;

import com.br.hermescomercialnetbeans.dao.EstoqueDao;

import com.br.hermescomercialnetbeans.model.Estoque;
import org.apache.logging.log4j.LogManager;
import java.util.List;

public class CadEstoqueController {

   
    private EstoqueDao dao = new EstoqueDao();
    private static final org.apache.logging.log4j.Logger logger = LogManager.getLogger(CadEstoqueController.class);

    public void salvar() {
        try {
            Estoque estoque = new Estoque();
            estoque.setQuantidade(txtEstQuantidade.getText());
            estoque.setMaximo(Integer.valueOf(txtEstMaxima.getText()));
            estoque.setMinimo(Integer.valueOf(txtEstMinimo.getText()));

            dao.salvar(estoque);
        } catch (Exception e) {
            logger.error("Erro ao salvar estoque: " + e.getMessage(), e);
        }
    }

    public void remove() {
        try {
            if (ComboCodigoProduto.getValue() != null) {
                dao.remove(ComboCodigoProduto.getValue());
            }
        } catch (Exception e) {
            logger.error("Erro ao remover estoque: " + e.getMessage(), e);
        }
    }

    public void update() {
        try {
            Estoque estoque = new Estoque();
            estoque.setQuantidade(txtEstQuantidade.getText());
            estoque.setMaximo(Integer.valueOf(txtEstMaxima.getText()));
            estoque.setMinimo(Integer.valueOf(txtEstMinimo.getText()));
            
            dao.update(estoque);
        } catch (Exception e) {
            logger.error("Erro ao atualizar estoque: " + e.getMessage(), e);
        }
    }
    
    public void listar() {
        try {
            List<Estoque> todos = dao.listar();
            for (Estoque e : todos) {
                logger.info("Item no estoque: " + e.getQuantidade());
            }
        } catch (Exception e) {
            logger.error("Erro ao listar estoque: " + e.getMessage(), e);
        }
    }

    public void buscar() {
        try {
            if (ComboCodigoProduto.getValue() != null) {
                List<Estoque> resultados = dao.buscar(ComboCodigoProduto.getValue());
                if (!resultados.isEmpty()) {
                    Estoque e = resultados.get(0);
                    txtEstQuantidade.setText(e.getQuantidade());
                    txtEstMaxima.setText(String.valueOf(e.getMaximo()));
                    txtEstMinimo.setText(String.valueOf(e.getMinimo()));
                }
            }
        } catch (Exception e) {
            logger.error("Erro ao buscar estoque: " + e.getMessage(), e);
        }
    }
}
