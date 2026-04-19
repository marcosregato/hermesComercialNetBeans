package com.br.hermescomercialnetbeans;

import com.br.hermescomercialnetbeans.view.TelaLogin;
import javax.swing.SwingUtilities;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Classe principal para iniciar o sistema PDV
 * @author marcos
 */
public class PDVPrincipal {
    
    private static final Logger logger = LogManager.getLogger(PDVPrincipal.class);

    public static void main(String[] args) {
        logger.info("Iniciando Hermes Comercial PDV v1.0");
        logger.info("Java Version: " + System.getProperty("java.version"));
        logger.info("OS: " + System.getProperty("os.name") + " " + System.getProperty("os.version"));
        logger.info("User Home: " + System.getProperty("user.home"));
        
        if (args != null && args.length > 0) {
            logger.info("Argumentos de linha de comando recebidos:");
            for (int i = 0; i < args.length; i++) {
                logger.info("  arg[" + i + "]: " + args[i]);
            }
        }
        
        // Inicia a aplicação com tela de login
        SwingUtilities.invokeLater(() -> {
            try {
                logger.info("Iniciando interface gráfica (Swing)");
                logger.info("Mostrando tela de login primeiro");
                
                TelaLogin telaLogin = new TelaLogin();
                telaLogin.setVisible(true);
                logger.info("Tela de login exibida com sucesso");
                
            } catch (Exception e) {
                logger.error("Erro ao iniciar aplicação: " + e.getMessage(), e);
                System.err.println("Erro ao iniciar aplicação: " + e.getMessage());
                e.printStackTrace();
            }
        });
    }
}
