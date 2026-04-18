package com.br.hermescomercialnetbeans.view;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import com.br.hermescomercialnetbeans.model.Usuario;

public class TelaPrincipal extends javax.swing.JFrame {

    private JDesktopPane desktop;
    private Usuario usuarioLogado;
    
    // Cores do tema
    private static final Color COR_PRIMARIA = new Color(52, 152, 219);
    private static final Color COR_TEXTO = new Color(44, 62, 80);

    public TelaPrincipal() {
        initComponents();
        setExtendedState(MAXIMIZED_BOTH);
    }
    
    public TelaPrincipal(Usuario usuario) {
        this.usuarioLogado = usuario;
        initComponents();
        setExtendedState(MAXIMIZED_BOTH);
        setTitle("Hermes Comercial - Sistema PDV - Usuário: " + (usuario != null ? usuario.getNome() : "Sistema"));
    }
    
    private void initComponents() {
        // Desktop com fundo moderno
        desktop = new JDesktopPane() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                
                // Gradiente sutil no fundo
                GradientPaint gradient = new GradientPaint(
                    0, 0, new Color(250, 252, 252),
                    getWidth(), getHeight(), new Color(240, 244, 248)
                );
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
                
                // Grid pattern sutil
                g2d.setColor(new Color(220, 225, 230));
                g2d.setStroke(new BasicStroke(0.5f));
                for (int i = 0; i < getWidth(); i += 50) {
                    g2d.drawLine(i, 0, i, getHeight());
                }
                for (int i = 0; i < getHeight(); i += 50) {
                    g2d.drawLine(0, i, getWidth(), i);
                }
            }
        };
        
        // MenuBar estilizado
        JMenuBar menuBar = criarMenuBar();
        
        // Painel de boas-vindas
        JPanel painelBoasVindas = criarPainelBoasVindas();
        
        // Layout principal
        setLayout(new BorderLayout());
        add(painelBoasVindas, BorderLayout.NORTH);
        add(desktop, BorderLayout.CENTER);
        
        // Configurações da janela
        configurarJanela(menuBar);
    }
    
    private JMenuBar criarMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        menuBar.setBackground(Color.WHITE);
        menuBar.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        menuBar.setPreferredSize(new Dimension(menuBar.getWidth(), 45));
        
        // Menu Cadastros
        JMenu menuCadastro = criarMenu("Cadastros", COR_PRIMARIA);
        JMenuItem itemUsuarios = criarMenuItem("Usuários", "Gerenciar usuários do sistema");
        itemUsuarios.addActionListener(e -> abrirTela(new TelaUsuario()));
        JMenuItem itemProdutos = criarMenuItem("Produtos", "Gerenciar produtos e estoque");
        itemProdutos.addActionListener(e -> abrirTela(new TelaProduto()));
        JMenuItem itemEstoque = criarMenuItem("Estoque", "Controle completo de estoque");
        itemEstoque.addActionListener(e -> abrirTela(new TelaEstoque()));
        menuCadastro.add(itemUsuarios);
        menuCadastro.add(itemProdutos);
        menuCadastro.add(itemEstoque);

        // Menu Configurar
        JMenu menuConfigurar = criarMenu("Configurar", new Color(155, 89, 182));
        JMenuItem itemConfigImpressora = criarMenuItem("Configurar Impressora", "Configurar impressora não fiscal");
        itemConfigImpressora.addActionListener(e -> abrirTela(new TelaConfiguracaoImpressora()));
        menuConfigurar.add(itemConfigImpressora);

        // Menu Vendas
        JMenu menuVenda = criarMenu("Vendas", new Color(46, 204, 113));
        JMenuItem itemPdv = criarMenuItem("Frente de Caixa (PDV)", "Abrir frente de caixa para vendas");
        itemPdv.addActionListener(e -> abrirTela(new TelaVendaNova(usuarioLogado)));
        menuVenda.add(itemPdv);

        // Menu Caixa
        JMenu menuCaixa = criarMenu("Caixa", new Color(230, 126, 34));
        JMenuItem itemControleCaixa = criarMenuItem("Controle de Caixa", "Gerenciar movimentos do caixa");
        itemControleCaixa.addActionListener(e -> abrirTela(new TelaControleCaixa(usuarioLogado)));
        menuCaixa.add(itemControleCaixa);

        // Menu Sair
        JMenu menuSair = criarMenu("Sair", new Color(231, 76, 60));
        JMenuItem itemSair = criarMenuItem("Sair do Sistema", "Encerrar e sair do sistema");
        itemSair.addActionListener(e -> confirmarSaida());
        menuSair.add(itemSair);

        menuBar.add(menuCadastro);
        menuBar.add(menuConfigurar);
        menuBar.add(menuVenda);
        menuBar.add(menuCaixa);
        menuBar.add(Box.createHorizontalGlue());
        
        // Label de status
        JLabel lblStatus = new JLabel("Hermes Comercial PDV v1.0");
        lblStatus.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblStatus.setForeground(Color.GRAY);
        menuBar.add(lblStatus);
        
        return menuBar;
    }
    
    private JMenu criarMenu(String texto, Color cor) {
        JMenu menu = new JMenu(texto);
        menu.setFont(new Font("Segoe UI", Font.BOLD, 12));
        menu.setForeground(cor);
        menu.setBackground(Color.WHITE);
        menu.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        menu.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        menu.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                menu.setBackground(new Color(245, 245, 245));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                menu.setBackground(Color.WHITE);
            }
        });
        
        return menu;
    }
    
    private JMenuItem criarMenuItem(String texto, String tooltip) {
        JMenuItem item = new JMenuItem(texto);
        item.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        item.setForeground(COR_TEXTO);
        item.setBackground(Color.WHITE);
        item.setToolTipText(tooltip);
        item.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
        item.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        item.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                item.setBackground(new Color(52, 152, 219));
                item.setForeground(Color.WHITE);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                item.setBackground(Color.WHITE);
                item.setForeground(COR_TEXTO);
            }
        });
        
        return item;
    }
    
    private JPanel criarPainelBoasVindas() {
        JPanel painel = new JPanel(new BorderLayout());
        painel.setBackground(Color.WHITE);
        painel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(15, 20, 15, 20)
        ));
        
        // Título principal
        JLabel lblTitulo = new JLabel("Hermes Comercial - Sistema PDV");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitulo.setForeground(COR_PRIMARIA);
        
        // Subtítulo
        String usuarioNome = usuarioLogado != null ? usuarioLogado.getNome() : "Operador";
        JLabel lblSubtitulo = new JLabel("Bem-vindo(a), " + usuarioNome + "!");
        lblSubtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblSubtitulo.setForeground(Color.GRAY);
        
        // Data e hora
        JLabel lblDataHora = new JLabel(java.time.LocalDateTime.now().format(
            java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")
        ));
        lblDataHora.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        lblDataHora.setForeground(Color.DARK_GRAY);
        
        // Painel esquerdo
        JPanel painelEsquerdo = new JPanel(new FlowLayout(FlowLayout.LEFT));
        painelEsquerdo.setBackground(Color.WHITE);
        painelEsquerdo.add(lblTitulo);
        painelEsquerdo.add(Box.createHorizontalStrut(20));
        painelEsquerdo.add(lblSubtitulo);
        
        // Painel direito
        JPanel painelDireito = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        painelDireito.setBackground(Color.WHITE);
        painelDireito.add(lblDataHora);
        
        painel.add(painelEsquerdo, BorderLayout.CENTER);
        painel.add(painelDireito, BorderLayout.EAST);
        
        return painel;
    }
    
    private void configurarJanela(JMenuBar menuBar) {
        setJMenuBar(menuBar);
        setTitle("Hermes Comercial - Sistema PDV");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1400, 900);
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(1000, 600));
        
        // Ícone da aplicação (se disponível)
        try {
            setIconImage(createIcon());
        } catch (Exception e) {
            // Ignora se não conseguir carregar ícone
        }
    }
    
    private Image createIcon() {
        // Cria um ícone simples programaticamente
        BufferedImage icon = new BufferedImage(32, 32, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = icon.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Fundo circular
        g2d.setColor(COR_PRIMARIA);
        g2d.fillOval(2, 2, 28, 28);
        
        // Letra H
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Arial", Font.BOLD, 18));
        g2d.drawString("H", 8, 22);
        
        g2d.dispose();
        return icon;
    }
    
    private void confirmarSaida() {
        int option = JOptionPane.showConfirmDialog(
            this,
            "Deseja realmente sair do sistema?",
            "Confirmar Saída",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE
        );
        
        if (option == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }

    private void abrirTela(JInternalFrame frame) {
        if (frame != null) {
            desktop.add(frame);
            frame.setVisible(true);
            try {
                frame.setSelected(true);
            } catch (java.beans.PropertyVetoException e) {
                e.printStackTrace();
            }
        }
    }
}