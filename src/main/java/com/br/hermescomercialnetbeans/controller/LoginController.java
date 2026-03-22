
package com.br.hermescomercialnetbeans.controller;

import com.br.hermescomercialnetbeans.model.Pessoa;
import com.br.hermescomercialnetbeans.util.Alerta;
import com.br.hermescomercialnetbeans.util.ValidarCampo;

import java.io.IOException;

public class LoginController {

   

    private final Alerta alerta = new Alerta();
    private final ValidarCampo validarCampo = new ValidarCampo();

    private PrincipalController principalController = new PrincipalController();

    
    public void handleBtEntra() {

            Pessoa pessoa = principalController.infoPessoa(txtLogin.getText(), txtSenha.getText());
            if (pessoa != null) {
                System.out.println("Usuário logado com sucesso!");
                try {
                    // Carrega o FXML da tela principal
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Layout_principal.fxml"));
                    Parent root = loader.load();

                    // Cria uma nova cena
                    Scene scene = new Scene(root);

                    // Obtém o palco (Stage) atual a partir de qualquer nó na cena atual
                    Stage stage = (Stage) btEntrar.getScene().getWindow();

                    // Define a nova cena no palco
                    stage.setScene(scene);
                    stage.setTitle("Hermes Comercial - Principal");
                    stage.show();

                } catch (IOException e) {
                    e.printStackTrace();
                    alerta.showAlert(Alert.AlertType.ERROR,
                            btEntrar.getScene().getWindow(),
                            "Erro ao carregar a tela", "Não foi possível carregar a tela principal.");
                }
            } else {
                alerta.showAlert(Alert.AlertType.ERROR,
                        btEntrar.getScene().getWindow(),
                        "Erro de Login", "Login ou Senha incorreta");
            }

    }

    @FXML
    public void handleBtFechar() {
        Stage stage = (Stage) btFechar.getScene().getWindow();
        stage.close();
    }
}
