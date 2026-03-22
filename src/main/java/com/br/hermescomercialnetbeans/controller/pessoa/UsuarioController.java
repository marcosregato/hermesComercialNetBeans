
package com.br.hermescomercialnetbeans.controller.pessoa;

import com.br.hermescomercialnetbeans.dao.UsuarioDao;
import com.br.hermescomercialnetbeans.model.Pessoa;
import com.br.hermescomercialnetbeans.model.Usuario;
import com.br.hermescomercialnetbeans.util.ValidarCampo;

import org.apache.logging.log4j.LogManager;


import java.util.List;

public class UsuarioController  {

    private static final org.apache.logging.log4j.Logger logger = LogManager.getLogger(UsuarioController.class);



    private UsuarioDao dao = new UsuarioDao();
    ValidarCampo validar;

    /*public void initialize(URL location, ResourceBundle resources) {
        if (comboEstado != null) {
            comboEstado.getItems().addAll(Arrays.asList(
                "AC", "AL", "AP", "AM", "BA", "CE", "DF", "ES", "GO",
                "MA", "MT", "MS", "MG", "PA", "PB", "PR", "PE", "PI",
                "RJ", "RN", "RS", "RO", "RR", "SC", "SP", "SE", "TO"
            ));
        }
        if (comboTipo != null) {
            comboTipo.getItems().addAll(Arrays.asList("Usuário", "Fornecedor", "Cliente", "Funcionário"));
        }
    }*/

    public void salvar(Usuario usuario) {
        try {
           /* usuario.setNome(txtNome.getText());
            usuario.setEndereco(txtEndereco.getText());
            usuario.setBairro(txtBairro.getText());
            usuario.setCidade(txtCidade.getText());
            usuario.setEstado(comboEstado.getValue());
            usuario.setCep(txtCep.getText());
            usuario.setEmail(txtEmail.getText());
            usuario.setTipoUsuario(comboTipo.getValue());*/

            if ("Fornecedor".equals(usuario.getTipoDocumento())) {
                validar.isCNPJ(usuario.getNumeroDocumetn());
                //usuario.setCnpj(txtCnpjCpf.getText());
            } else {
                validar.isCPF(usuario.getNumeroDocumetn());
            }

            dao.salvar(usuario);
        } catch (Exception e) {
            logger.error("Erro ao salvar: " + e.getMessage());
        }
    }

    public void delete() {
        try {
            if (txtNome.getText() != null && !txtNome.getText().isEmpty()) {
                dao.remove(txtNome.getText());
            }
        } catch (Exception e) {
            logger.error("Erro ao remover: " + e.getMessage());
        }
    }

    public void update() {
        try {
            Pessoa pessoa = new Pessoa();
            pessoa.setNome(txtNome.getText());
            pessoa.setEndereco(txtEndereco.getText());
            pessoa.setBairro(txtBairro.getText());
            pessoa.setCidade(txtCidade.getText());
            pessoa.setEstado(comboEstado.getValue());
            pessoa.setCep(txtCep.getText());
            pessoa.setEmail(txtEmail.getText());
            pessoa.setTipoUsuario(comboTipo.getValue());

            if ("Fornecedor".equals(comboTipo.getValue())) {
                pessoa.setCnpj(txtCnpjCpf.getText());
            } else {
                pessoa.setCpf(txtCnpjCpf.getText());
            }

            dao.update(pessoa);
        } catch (Exception e) {
            logger.error("Erro ao atualizar: " + e.getMessage());
        }
    }

    public void buscar() {
        try {
            if (PesNome != null && !PesNome.getText().isEmpty()) {
                List<Pessoa> resultados = dao.buscar(PesNome.getText());
                if (!resultados.isEmpty()) {
                    Pessoa p = resultados.get(0);
                    txtNome.setText(p.getNome());
                    txtEndereco.setText(p.getEndereco());
                    txtBairro.setText(p.getBairro());
                    txtCidade.setText(p.getCidade());
                    comboEstado.setValue(p.getEstado());
                    txtCep.setText(p.getCep());
                    txtEmail.setText(p.getEmail());
                    comboTipo.setValue(p.getTipoPessoa());
                    txtCnpjCpf.setText("Fornecedor".equals(p.getTipoPessoa()) ? p.getCnpj() : p.getCpf());
                }
            }
        } catch (Exception e) {
            logger.error("Erro ao buscar pessoa: " + e.getMessage());
        }
    }

    public void lista() {
        try {
            List<Pessoa> todos = dao.lista();
            for (Pessoa p : todos) {
                logger.info("Pessoa na lista: " + p.getNome());
            }
        } catch (Exception e) {
            logger.error("Erro ao listar pessoas: " + e.getMessage());
        }
    }
}
