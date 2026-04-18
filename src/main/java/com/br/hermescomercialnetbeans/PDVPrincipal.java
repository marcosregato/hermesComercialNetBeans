package com.br.hermescomercialnetbeans;

import com.br.hermescomercialnetbeans.view.TelaPrincipal;
import javax.swing.SwingUtilities;

/**
 * Classe principal para iniciar o sistema PDV
 * @author marcos
 */
public class PDVPrincipal {

    public static void main(String[] args) {
        // Inicia a aplicação com tela principal
        SwingUtilities.invokeLater(() -> {
            try {
                TelaPrincipal telaPrincipal = new TelaPrincipal();
                telaPrincipal.setVisible(true);
                
            } catch (Exception e) {
                System.err.println("Erro ao iniciar aplicação: " + e.getMessage());
                e.printStackTrace();
            }
        });
    }
}
