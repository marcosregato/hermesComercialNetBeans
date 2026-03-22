
package com.br.hermescomercialnetbeans.controller.produto;

import java.util.List;

import com.br.hermescomercialnetbeans.dao.ProdutoDao;
import com.br.hermescomercialnetbeans.model.Produto;
import com.br.hermescomercialnetbeans.util.Alerta;
import org.apache.logging.log4j.LogManager;

/**
 *
 * @author marcos
 */
public class CadProdutoController {


	private ProdutoDao dao = new ProdutoDao();
	private Alerta alerta = new Alerta();
    private static final org.apache.logging.log4j.Logger logger = LogManager.getLogger(CadProdutoController.class);

  
    public void handleBtSalvar() {
        try {
            Produto produto = new Produto();
            produto.setNome(txtNome.getText());
            produto.setCodigo(txtCodigo.getText());
            produto.setCategoria(txtCategoria.getText());
            produto.setSubCategoria(txtSubCategoria.getText());
            produto.setMarca(txtMarca.getText());
            produto.setDataCompra(txtDataCompra.getText());

            dao.salvar(produto);
            logger.info("Produto salvo com sucesso!");
        } catch (Exception e) {
            logger.error("Erro ao salvar produto: " + e.getMessage(), e);
            alerta.showAlert(Alert.AlertType.ERROR,
                    btSalvar.getScene().getWindow(),
                    "Erro ao salvar", "Não foi possível salvar o produto.");
        }
    }

	public void remove() {
		try {
			if (txtNome.getText() != null && !txtNome.getText().isEmpty()) {
				dao.remove(txtNome.getText());
			}
		} catch (Exception e) {
			logger.error("Erro ao remover produto: " + e.getMessage(), e);
		}
	}

	public void update() {
		try {
            Produto produto = new Produto();
            produto.setNome(txtNome.getText());
            produto.setCodigo(txtCodigo.getText());
            produto.setCategoria(txtCategoria.getText());
            produto.setSubCategoria(txtSubCategoria.getText());
            produto.setMarca(txtMarca.getText());
            produto.setDataCompra(txtDataCompra.getText());
            
            dao.update(produto);
		} catch (Exception e) {
			logger.error("Erro ao atualizar produto: " + e.getMessage(), e);
		}
	}

	public void listar() {
		try {
			List<Produto> lista = dao.listar();
            for (Produto p : lista) {
                logger.info("Produto: " + p.getNome());
            }
		} catch (Exception e) {
			logger.error("Erro ao listar produtos: " + e.getMessage(), e);
		}
	}

	public void buscar() {
		try {
            if (txtNome.getText() != null && !txtNome.getText().isEmpty()) {
                List<Produto> resultados = dao.buscar(txtNome.getText());
                if (!resultados.isEmpty()) {
                    Produto p = resultados.get(0);
                    txtNome.setText(p.getNome());
                    txtCodigo.setText(p.getCodigo());
                    txtCategoria.setText(p.getCategoria());
                    txtSubCategoria.setText(p.getSubCategoria());
                    txtMarca.setText(p.getMarca());
                    txtDataCompra.setText(p.getDataCompra());
                }
            }
		} catch (Exception e) {
			logger.error("Erro ao buscar produto: " + e.getMessage(), e);
		}
	}
}
