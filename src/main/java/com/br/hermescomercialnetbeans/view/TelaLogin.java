package com.br.hermescomercialnetbeans.view;

import com.br.hermescomercialnetbeans.dao.UsuarioDao;
import com.br.hermescomercialnetbeans.model.Usuario;
import org.apache.logging.log4j.LogManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * Tela de Login elegante para o sistema PDV
 * @author marcos
 */
public class TelaLogin extends JDialog {
    
    private static final org.apache.logging.log4j.Logger logger = LogManager.getLogger(TelaLogin.class);
    
    // Componentes da interface
    private JPanel panelPrincipal;
    private JPanel panelLogo;
    private JPanel panelForm;
    private JPanel panelBotoes;
    
    private JLabel lblLogo;
    private JLabel lblTitulo;
    private JLabel lblSubtitulo;
    private JLabel lblLogin;
    private JLabel lblSenha;
    private JLabel lblVersao;
    
    private JTextField txtLogin;
    private JPasswordField txtSenha;
    private JCheckBox chkMostrarSenha;
    private JCheckBox chkLembrar;
    
    private JButton btEntrar;
    private JButton btCancelar;
    private JButton btConfig;
    
    // DAO e usuário
    private UsuarioDao usuarioDao;
    private Usuario usuarioAutenticado;
    
    // Cores do tema
    private static final Color COR_PRIMARIA = new Color(41, 128, 185);
    private static final Color COR_SECUNDARIA = new Color(52, 73, 94);
    private static final Color COR_SUCESSO = new Color(39, 174, 96);
    private static final Color COR_FUNDO = new Color(236, 240, 241);
    private static final Color COR_TEXTO = new Color(44, 62, 80);
    
    public TelaLogin() {
        super((Frame) null, "Hermes Comercial - Login", true);
        this.usuarioDao = new UsuarioDao();
        inicializarComponentes();
        configurarLayout();
        configurarEventos();
        aplicarTema();
    }
    
    private void inicializarComponentes() {
        // Painéis
        panelPrincipal = new JPanel(new BorderLayout());
        panelLogo = new JPanel(new BorderLayout());
        panelForm = new JPanel(new GridBagLayout());
        panelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        
        // Labels
        lblLogo = new JLabel();
        lblLogo.setIcon(criarIconeSistema());
        lblLogo.setHorizontalAlignment(SwingConstants.CENTER);
        
        lblTitulo = new JLabel("Hermes Comercial");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblTitulo.setForeground(COR_PRIMARIA);
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        
        lblSubtitulo = new JLabel("Sistema PDV - Ponto de Venda");
        lblSubtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblSubtitulo.setForeground(COR_SECUNDARIA);
        lblSubtitulo.setHorizontalAlignment(SwingConstants.CENTER);
        
        lblLogin = new JLabel("Login:");
        lblLogin.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblLogin.setForeground(COR_TEXTO);
        
        lblSenha = new JLabel("Senha:");
        lblSenha.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblSenha.setForeground(COR_TEXTO);
        
        lblVersao = new JLabel("v1.0.0");
        lblVersao.setFont(new Font("Segoe UI", Font.ITALIC, 10));
        lblVersao.setForeground(Color.GRAY);
        lblVersao.setHorizontalAlignment(SwingConstants.CENTER);
        
        // Campos de texto
        txtLogin = new JTextField(20);
        txtLogin.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtLogin.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(COR_PRIMARIA, 1),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        
        txtSenha = new JPasswordField(20);
        txtSenha.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtSenha.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(COR_PRIMARIA, 1),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        
        // Checkboxes
        chkMostrarSenha = new JCheckBox("Mostrar senha");
        chkMostrarSenha.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        chkMostrarSenha.setForeground(COR_TEXTO);
        chkMostrarSenha.setOpaque(false);
        
        chkLembrar = new JCheckBox("Lembrar usuário");
        chkLembrar.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        chkLembrar.setForeground(COR_TEXTO);
        chkLembrar.setOpaque(false);
        
        // Botões
        btEntrar = criarBotao("Entrar", COR_SUCESSO);
        btCancelar = criarBotao("Cancelar", Color.GRAY);
        btConfig = criarBotao("Config", COR_PRIMARIA);
    }
    
    private void configurarLayout() {
        // Configurar painel do logo
        panelLogo.add(lblTitulo, BorderLayout.NORTH);
        panelLogo.add(lblSubtitulo, BorderLayout.CENTER);
        panelLogo.add(lblLogo, BorderLayout.SOUTH);
        panelLogo.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));
        panelLogo.setBackground(Color.WHITE);
        
        // Configurar painel do formulário
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Login
        gbc.gridx = 0; gbc.gridy = 0;
        panelForm.add(lblLogin, gbc);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        panelForm.add(txtLogin, gbc);
        
        // Senha
        gbc.gridx = 0; gbc.gridy = 1;
        gbc.weightx = 0;
        panelForm.add(lblSenha, gbc);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        panelForm.add(txtSenha, gbc);
        
        // Checkboxes
        gbc.gridx = 0; gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.weightx = 0;
        panelForm.add(chkMostrarSenha, gbc);
        
        gbc.gridx = 0; gbc.gridy = 3;
        panelForm.add(chkLembrar, gbc);
        
        // Botões
        panelBotoes.add(btEntrar);
        panelBotoes.add(btCancelar);
        panelBotoes.add(btConfig);
        panelBotoes.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        panelBotoes.setBackground(Color.WHITE);
        
        // Montar painel principal
        JPanel panelCentro = new JPanel(new BorderLayout());
        panelCentro.add(panelLogo, BorderLayout.NORTH);
        panelCentro.add(panelForm, BorderLayout.CENTER);
        panelCentro.add(panelBotoes, BorderLayout.SOUTH);
        panelCentro.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));
        panelCentro.setBackground(Color.WHITE);
        
        panelPrincipal.add(panelCentro, BorderLayout.CENTER);
        panelPrincipal.add(lblVersao, BorderLayout.SOUTH);
        panelPrincipal.setBackground(COR_FUNDO);
        
        // Configurar dialog
        this.setContentPane(panelPrincipal);
        this.setSize(500, 450);
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }
    
    private void configurarEventos() {
        // Evento do botão Entrar
        btEntrar.addActionListener(e -> autenticar());
        
        // Evento do botão Cancelar
        btCancelar.addActionListener(e -> {
            usuarioAutenticado = null;
            dispose();
        });
        
        // Evento do botão Config
        btConfig.addActionListener(e -> mostrarConfiguracoes());
        
        // Evento do checkbox Mostrar Senha
        chkMostrarSenha.addActionListener(e -> {
            if (chkMostrarSenha.isSelected()) {
                txtSenha.setEchoChar((char) 0);
            } else {
                txtSenha.setEchoChar('·');
            }
        });
        
        // Eventos de teclado
        txtLogin.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {}
            
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    txtSenha.requestFocus();
                }
            }
            
            @Override
            public void keyReleased(KeyEvent e) {}
        });
        
        txtSenha.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {}
            
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    autenticar();
                }
            }
            
            @Override
            public void keyReleased(KeyEvent e) {}
        });
        
        // Foco inicial
        txtLogin.requestFocus();
    }
    
    private void aplicarTema() {
        // Aplicar Look and Feel moderno se disponível
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            SwingUtilities.updateComponentTreeUI(this);
        } catch (Exception e) {
            logger.warn("Não foi possível aplicar o Look and Feel do sistema: " + e.getMessage());
        }
    }
    
    private void autenticar() {
        String login = txtLogin.getText().trim();
        String senha = new String(txtSenha.getPassword()).trim();
        
        if (login.isEmpty()) {
            mostrarMensagem("Por favor, informe o login.", JOptionPane.WARNING_MESSAGE);
            txtLogin.requestFocus();
            return;
        }
        
        if (senha.isEmpty()) {
            mostrarMensagem("Por favor, informe a senha.", JOptionPane.WARNING_MESSAGE);
            txtSenha.requestFocus();
            return;
        }
        
        try {
            usuarioAutenticado = usuarioDao.autenticar(login, senha);
            
            if (usuarioAutenticado != null) {
                if (!usuarioAutenticado.getAtivo()) {
                    mostrarMensagem("Usuário inativo. Entre em contato com o administrador.", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                logger.info("Usuário autenticado com sucesso: " + usuarioAutenticado.getNome());
                mostrarMensagem("Bem-vindo, " + usuarioAutenticado.getNome() + "!", JOptionPane.INFORMATION_MESSAGE);
                
                // Abrir tela principal
                TelaPrincipal telaPrincipal = new TelaPrincipal(usuarioAutenticado);
                telaPrincipal.setVisible(true);
                
                dispose();
            } else {
                mostrarMensagem("Login ou senha inválidos.", JOptionPane.ERROR_MESSAGE);
                txtSenha.setText("");
                txtLogin.requestFocus();
            }
            
        } catch (Exception e) {
            logger.error("Erro ao autenticar usuário: " + e.getMessage(), e);
            mostrarMensagem("Erro ao autenticar. Tente novamente.", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void mostrarConfiguracoes() {
        JOptionPane.showMessageDialog(this, 
            "Configurações do Banco de Dados:\n\n" +
            "PostgreSQL: localhost:5432/hermes_pdv\n" +
            "MySQL: localhost:3306/hermes_pdv\n" +
            "SQLite: hermes_pdv.db\n\n" +
            "Verifique o arquivo config.properties", 
            "Configurações", 
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void mostrarMensagem(String mensagem, int tipo) {
        JOptionPane.showMessageDialog(this, mensagem, "Login", tipo);
    }
    
    private JButton criarBotao(String texto, Color cor) {
        JButton botao = new JButton(texto);
        botao.setFont(new Font("Segoe UI", Font.BOLD, 12));
        botao.setForeground(Color.WHITE);
        botao.setBackground(cor);
        botao.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25));
        botao.setFocusPainted(false);
        botao.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Efeito hover
        botao.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                botao.setBackground(cor.brighter());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                botao.setBackground(cor);
            }
        });
        
        return botao;
    }
    
    private Icon criarIconeSistema() {
        // Criar um ícone simples usando texto estilizado
        JPanel iconPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                
                // Desenhar círculo de fundo
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(COR_PRIMARIA);
                g2d.fillOval(10, 10, 60, 60);
                
                // Desenhar texto "HC"
                g2d.setColor(Color.WHITE);
                g2d.setFont(new Font("Arial", Font.BOLD, 20));
                FontMetrics fm = g2d.getFontMetrics();
                String text = "HC";
                int x = (80 - fm.stringWidth(text)) / 2;
                int y = (80 + fm.getAscent()) / 2 - fm.getDescent();
                g2d.drawString(text, x, y);
                
                g2d.dispose();
            }
        };
        
        iconPanel.setPreferredSize(new Dimension(80, 80));
        iconPanel.setOpaque(false);
        return null; // Retorna null por enquanto - ícone pode ser implementado posteriormente
    }
    
    public Usuario getUsuarioAutenticado() {
        return usuarioAutenticado;
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            TelaLogin telaLogin = new TelaLogin();
            telaLogin.setVisible(true);
        });
    }
}
