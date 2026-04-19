package com.br.hermescomercialnetbeans.view;

import com.br.hermescomercialnetbeans.dao.UsuarioDao;
import com.br.hermescomercialnetbeans.model.Usuario;
import com.br.hermescomercialnetbeans.utils.ResponsiveUI;
import com.br.hermescomercialnetbeans.utils.FontConfig;
import org.apache.logging.log4j.LogManager;

import javax.swing.*;
import java.awt.*;
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
    private static final Color COR_SUCESSO = new Color(46, 204, 113);
    private static final Color COR_PERIGO = new Color(231, 76, 60);
    private static final Color COR_INFO = new Color(52, 152, 219);
    private static final Color COR_FUNDO = new Color(236, 240, 241);
    private static final Color COR_TEXTO = new Color(44, 62, 80);
    private static final Color COR_BRANCA = new Color(255, 255, 255);
    
    public TelaLogin() {
        super((Frame) null, "Hermes Comercial - Login", true);
        this.usuarioDao = new UsuarioDao();
        inicializarComponentes();
        configurarLayout();
        configurarEventos();
        aplicarTema();
        
        // Aplicar design responsivo
        ResponsiveUI.makeResponsive(this);
        
        // Aplicar fonte padrão a todos os componentes
        FontConfig.aplicarFontePadraoRecursivo(this);
        
        // Configurar tamanho mínimo e preferido
        setMinimumSize(new Dimension(400, 500));
        setPreferredSize(new Dimension(450, 600));
        pack();
        setLocationRelativeTo(null);
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
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 32));
        lblTitulo.setForeground(COR_PRIMARIA);
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        
        lblSubtitulo = new JLabel("Sistema PDV - Ponto de Venda");
        lblSubtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        lblSubtitulo.setForeground(COR_SECUNDARIA);
        lblSubtitulo.setHorizontalAlignment(SwingConstants.CENTER);
        
        lblLogin = new JLabel("Login:");
        lblLogin.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblLogin.setForeground(COR_TEXTO);
        
        lblSenha = new JLabel("Senha:");
        lblSenha.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblSenha.setForeground(COR_TEXTO);
        
        lblVersao = new JLabel("v1.0.0");
        lblVersao.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        lblVersao.setForeground(Color.GRAY);
        lblVersao.setHorizontalAlignment(SwingConstants.CENTER);
        
        // Campos de texto
        txtLogin = new JTextField(20);
        txtLogin.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        txtLogin.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(COR_PRIMARIA, 1),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        
        txtSenha = new JPasswordField(20);
        txtSenha.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        txtSenha.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(COR_PRIMARIA, 1),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        
        // Checkboxes
        chkMostrarSenha = new JCheckBox("Mostrar senha");
        chkMostrarSenha.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        chkMostrarSenha.setForeground(COR_TEXTO);
        chkMostrarSenha.setOpaque(false);
        
        chkLembrar = new JCheckBox("Lembrar usuário");
        chkLembrar.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        chkLembrar.setForeground(COR_TEXTO);
        chkLembrar.setOpaque(false);
        
        // Botões
        btEntrar = criarBotao("Entrar", COR_SUCESSO);
        btCancelar = criarBotao("Cancelar", COR_PERIGO);
        btConfig = criarBotao("Config", COR_INFO);
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
        
    logger.info("Tentativa de autenticação iniciada");
    logger.debug("Login informado: " + login);
    logger.debug("Senha informada: " + (senha != null && !senha.isEmpty() ? "***" : "VAZIA"));
        
    if (login.isEmpty()) {
        logger.warn("Tentativa de login com campo de login vazio");
        mostrarMensagem("Por favor, informe o login.", JOptionPane.WARNING_MESSAGE);
        txtLogin.requestFocus();
        return;
    }
        
    if (senha.isEmpty()) {
        logger.warn("Tentativa de login com campo de senha vazio para usuário: " + login);
        mostrarMensagem("Por favor, informe a senha.", JOptionPane.WARNING_MESSAGE);
        txtSenha.requestFocus();
        return;
    }
        
    try {
        logger.debug("Verificando credenciais no banco de dados para usuário: " + login);
        usuarioAutenticado = usuarioDao.autenticar(login, senha);
            
        if (usuarioAutenticado != null) {
            logger.info("Usuário encontrado: " + usuarioAutenticado.getNome() + " (ID: " + usuarioAutenticado.getId() + ")");
            logger.debug("Status do usuário: " + (usuarioAutenticado.getAtivo() ? "ATIVO" : "INATIVO"));
                
            if (!usuarioAutenticado.getAtivo()) {
                logger.warn("Tentativa de login com usuário INATIVO: " + login);
                mostrarMensagem("Usuário inativo. Contate o administrador.", JOptionPane.ERROR_MESSAGE);
                txtSenha.setText("");
                txtLogin.requestFocus();
            } else {
                logger.info("Autenticação bem-sucedida para usuário: " + usuarioAutenticado.getNome());
                mostrarMensagem("Bem-vindo, " + usuarioAutenticado.getNome() + "!", JOptionPane.INFORMATION_MESSAGE);
                    
                // Abrir tela principal
                TelaPrincipal telaPrincipal = new TelaPrincipal(usuarioAutenticado);
                telaPrincipal.setVisible(true);
                    
                dispose();
            }
        } else {
            logger.warn("Falha na autenticação - usuário ou senha inválidos: " + login);
            mostrarMensagem("Login ou senha inválidos.", JOptionPane.ERROR_MESSAGE);
            txtSenha.setText("");
            txtLogin.requestFocus();
        }
            
    } catch (Exception e) {
        logger.error("Erro ao autenticar usuário '" + login + "': " + e.getMessage(), e);
        mostrarMensagem("Erro ao autenticar. Tente novamente.", JOptionPane.ERROR_MESSAGE);
    }
    }
    
    private void mostrarMensagem(String mensagem, int tipo) {
        JOptionPane.showMessageDialog(this, mensagem, "Login", tipo);
    }
    
    private JButton criarBotao(String texto, Color cor) {
        JButton botao = new JButton(texto) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Desenhar fundo arredondado
                g2d.setColor(getBackground());
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                
                // Desenhar texto
                g2d.setColor(getForeground());
                g2d.setFont(getFont());
                FontMetrics fm = g2d.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(getText())) / 2;
                int y = (getHeight() + fm.getAscent()) / 2 - fm.getDescent();
                g2d.drawString(getText(), x, y);
                
                g2d.dispose();
            }
        };
        
        botao.setFont(new Font("Segoe UI", Font.BOLD, 16));
        botao.setForeground(COR_BRANCA);
        botao.setBackground(cor);
        botao.setBorder(BorderFactory.createEmptyBorder(12, 30, 12, 30));
        botao.setFocusPainted(false);
        botao.setContentAreaFilled(false);
        botao.setOpaque(false);
        botao.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Efeito hover mais suave
        botao.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                botao.setBackground(cor.brighter());
                botao.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                botao.setBackground(cor);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                botao.setBackground(cor.darker());
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                botao.setBackground(cor.brighter());
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
    
    private void mostrarConfiguracoes() {
        JOptionPane.showMessageDialog(this, 
            "Configurações do Sistema:\n\n" +
            "Banco de Dados: PostgreSQL\n" +
            "Servidor: localhost:5432\n" +
            "Database: hermes_pdv\n\n" +
            "Para alterar configurações,\n" +
            "edite o arquivo config.properties", 
            "Configurações", 
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            TelaLogin telaLogin = new TelaLogin();
            telaLogin.setVisible(true);
        });
    }
}
