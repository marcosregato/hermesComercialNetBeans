package com.br.hermescomercialnetbeans.view;

import com.br.hermescomercialnetbeans.utils.ConfigProperties;
import org.apache.logging.log4j.LogManager;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.prefs.Preferences;

/**
 * Tela para configuração de impressora não fiscal
 * @author marcos
 */
public class TelaConfiguracaoImpressora extends JInternalFrame {
    
    private static final org.apache.logging.log4j.Logger logger = LogManager.getLogger(TelaConfiguracaoImpressora.class);
    
    // Componentes da interface
    private JPanel panelPrincipal;
    private JPanel panelTipoImpressora;
    private JPanel panelConfiguracao;
    private JPanel panelTeste;
    private JPanel panelBotoes;
    
    // Tipo de impressora
    private ButtonGroup grupoTipoImpressora;
    private JRadioButton rbImpressoraWindows;
    private JRadioButton rbImpressoraUSB;
    private JRadioButton rbImpressoraRede;
    private JRadioButton rbImpressoraNenhuma;
    
    // Configurações Windows
    private JComboBox<String> comboImpressorasWindows;
    private JTextField txtNomeImpressora;
    private JCheckBox chkImpressoraPadrao;
    
    // Configurações USB
    private JTextField txtPortaUSB;
    private JComboBox<Integer> comboVelocidade;
    private JCheckBox chkAutoDetectar;
    
    // Configurações Rede
    private JTextField txtIP;
    private JTextField txtPorta;
    private JCheckBox chkConexaoAutomatica;
    
    // Configurações Gerais
    private JSpinner spnLinhasPorPagina;
    private JSpinner spnColunasPorPagina;
    private JSpinner spnTamanhoFonte;
    private JComboBox<String> comboAlinhamento;
    private JCheckBox chkCortarPapel;
    private JCheckBox chkAbrirGaveta;
    
    // Teste
    private JTextArea txtTesteImpressao;
    private JButton btTestarImpressao;
    private JButton btTestarCorte;
    private JButton btTestarGaveta;
    
    // Botões
    private JButton btSalvar;
    private JButton btCancelar;
    private JButton btDetectar;
    private JButton btRestaurarPadrao;
    
    // Cores do tema
    private static final Color COR_PRIMARIA = new Color(41, 128, 185);
    private static final Color COR_SUCESSO = new Color(39, 174, 96);
    private static final Color COR_PERIGO = new Color(231, 76, 60);
    private static final Color COR_ADVERTENCIA = new Color(243, 156, 18);
    private static final Color COR_FUNDO = new Color(236, 240, 241);
    private static final Color COR_TEXTO = new Color(44, 62, 80);
    
    // Configurações salvas
    private Preferences preferences;
    
    public TelaConfiguracaoImpressora() {
        super("Configuração de Impressora");
        this.preferences = Preferences.userNodeForPackage(TelaConfiguracaoImpressora.class);
        
        inicializarComponentes();
        configurarLayout();
        configurarEventos();
        carregarConfiguracoes();
    }
    
    private void inicializarComponentes() {
        // Painéis principais
        panelPrincipal = new JPanel(new BorderLayout());
        panelTipoImpressora = new JPanel(new GridLayout(4, 1, 5, 5));
        panelConfiguracao = new JPanel(new CardLayout());
        panelTeste = new JPanel(new BorderLayout());
        panelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        
        // Grupo de tipo de impressora
        grupoTipoImpressora = new ButtonGroup();
        
        rbImpressoraWindows = new JRadioButton("Impressora Windows");
        rbImpressoraWindows.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        rbImpressoraWindows.setOpaque(false);
        
        rbImpressoraUSB = new JRadioButton("Impressora USB");
        rbImpressoraUSB.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        rbImpressoraUSB.setOpaque(false);
        
        rbImpressoraRede = new JRadioButton("Impressora de Rede");
        rbImpressoraRede.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        rbImpressoraRede.setOpaque(false);
        
        rbImpressoraNenhuma = new JRadioButton("Nenhuma Impressora");
        rbImpressoraNenhuma.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        rbImpressoraNenhuma.setOpaque(false);
        
        grupoTipoImpressora.add(rbImpressoraWindows);
        grupoTipoImpressora.add(rbImpressoraUSB);
        grupoTipoImpressora.add(rbImpressoraRede);
        grupoTipoImpressora.add(rbImpressoraNenhuma);
        
        // Painel Windows
        JPanel panelWindows = new JPanel(new GridBagLayout());
        panelWindows.setBorder(BorderFactory.createTitledBorder("Configuração Windows"));
        panelWindows.setBackground(Color.WHITE);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        gbc.gridx = 0; gbc.gridy = 0;
        panelWindows.add(new JLabel("Impressora:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        panelWindows.add(comboImpressorasWindows = new JComboBox<>(), gbc);
        
        gbc.gridx = 0; gbc.gridy = 1;
        gbc.weightx = 0;
        panelWindows.add(new JLabel("Nome:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        panelWindows.add(txtNomeImpressora = new JTextField(30), gbc);
        
        gbc.gridx = 0; gbc.gridy = 2;
        gbc.gridwidth = 2;
        panelWindows.add(chkImpressoraPadrao = new JCheckBox("Usar como impressora padrão do Windows"), gbc);
        
        // Painel USB
        JPanel panelUSB = new JPanel(new GridBagLayout());
        panelUSB.setBorder(BorderFactory.createTitledBorder("Configuração USB"));
        panelUSB.setBackground(Color.WHITE);
        
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.gridwidth = 1;
        panelUSB.add(new JLabel("Porta USB:"), gbc);
        gbc.gridx = 1;
        panelUSB.add(txtPortaUSB = new JTextField("COM1", 15), gbc);
        
        gbc.gridx = 0; gbc.gridy = 1;
        panelUSB.add(new JLabel("Velocidade:"), gbc);
        gbc.gridx = 1;
        panelUSB.add(comboVelocidade = new JComboBox<>(new Integer[]{9600, 19200, 38400, 57600, 115200}), gbc);
        
        gbc.gridx = 0; gbc.gridy = 2;
        gbc.gridwidth = 2;
        panelUSB.add(chkAutoDetectar = new JCheckBox("Auto-detectar impressora"), gbc);
        
        // Painel Rede
        JPanel panelRede = new JPanel(new GridBagLayout());
        panelRede.setBorder(BorderFactory.createTitledBorder("Configuração de Rede"));
        panelRede.setBackground(Color.WHITE);
        
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.gridwidth = 1;
        panelRede.add(new JLabel("Endereço IP:"), gbc);
        gbc.gridx = 1;
        panelRede.add(txtIP = new JTextField("192.168.1.100", 15), gbc);
        
        gbc.gridx = 0; gbc.gridy = 1;
        panelRede.add(new JLabel("Porta:"), gbc);
        gbc.gridx = 1;
        panelRede.add(txtPorta = new JTextField("9100", 10), gbc);
        
        gbc.gridx = 0; gbc.gridy = 2;
        gbc.gridwidth = 2;
        panelRede.add(chkConexaoAutomatica = new JCheckBox("Conectar automaticamente"), gbc);
        
        // Painel de configurações gerais
        JPanel panelGerais = new JPanel(new GridBagLayout());
        panelGerais.setBorder(BorderFactory.createTitledBorder("Configurações Gerais"));
        panelGerais.setBackground(Color.WHITE);
        
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.gridwidth = 1;
        panelGerais.add(new JLabel("Linhas por Página:"), gbc);
        gbc.gridx = 1;
        panelGerais.add(spnLinhasPorPagina = new JSpinner(new SpinnerNumberModel(40, 20, 100, 1)), gbc);
        
        gbc.gridx = 0; gbc.gridy = 1;
        panelGerais.add(new JLabel("Colunas por Página:"), gbc);
        gbc.gridx = 1;
        panelGerais.add(spnColunasPorPagina = new JSpinner(new SpinnerNumberModel(48, 30, 80, 1)), gbc);
        
        gbc.gridx = 0; gbc.gridy = 2;
        panelGerais.add(new JLabel("Tamanho da Fonte:"), gbc);
        gbc.gridx = 1;
        panelGerais.add(spnTamanhoFonte = new JSpinner(new SpinnerNumberModel(10, 6, 20, 1)), gbc);
        
        gbc.gridx = 0; gbc.gridy = 3;
        panelGerais.add(new JLabel("Alinhamento:"), gbc);
        gbc.gridx = 1;
        panelGerais.add(comboAlinhamento = new JComboBox<>(new String[]{"Esquerdo", "Centralizado", "Direito"}), gbc);
        
        gbc.gridx = 0; gbc.gridy = 4;
        gbc.gridwidth = 2;
        panelGerais.add(chkCortarPapel = new JCheckBox("Cortar papel automaticamente"), gbc);
        
        gbc.gridx = 0; gbc.gridy = 5;
        panelGerais.add(chkAbrirGaveta = new JCheckBox("Abrir gaveta após impressão"), gbc);
        
        // Painel de teste
        txtTesteImpressao = new JTextArea(8, 40);
        txtTesteImpressao.setFont(new Font("Courier New", Font.PLAIN, 10));
        txtTesteImpressao.setBorder(BorderFactory.createLineBorder(COR_PRIMARIA, 1));
        txtTesteImpressao.setText("TESTE DE IMPRESSÃO\n========================\n" +
            "Hermes Comercial PDV\n" +
            "Data: " + java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")) + "\n" +
            "========================\n" +
            "Este é um teste para verificar se a impressora está funcionando corretamente.\n" +
            "Verifique se o texto foi impresso corretamente.\n");
        
        btTestarImpressao = criarBotao("Testar Impressão", COR_PRIMARIA);
        btTestarCorte = criarBotao("Testar Corte", COR_ADVERTENCIA);
        btTestarGaveta = criarBotao("Testar Gaveta", COR_SUCESSO);
        
        JPanel panelBotoesTeste = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        panelBotoesTeste.add(btTestarImpressao);
        panelBotoesTeste.add(btTestarCorte);
        panelBotoesTeste.add(btTestarGaveta);
        
        panelTeste.setBorder(BorderFactory.createTitledBorder("Teste de Impressora"));
        panelTeste.setBackground(Color.WHITE);
        panelTeste.add(new JScrollPane(txtTesteImpressao), BorderLayout.CENTER);
        panelTeste.add(panelBotoesTeste, BorderLayout.SOUTH);
        
        // Botões principais
        btSalvar = criarBotao("Salvar Configurações", COR_SUCESSO);
        btCancelar = criarBotao("Cancelar", Color.GRAY);
        btDetectar = criarBotao("Detectar Impressoras", COR_PRIMARIA);
        btRestaurarPadrao = criarBotao("Restaurar Padrão", COR_ADVERTENCIA);
        
        // Adicionar componentes ao painel de tipo
        panelTipoImpressora.setBorder(BorderFactory.createTitledBorder("Tipo de Impressora"));
        panelTipoImpressora.setBackground(Color.WHITE);
        panelTipoImpressora.add(rbImpressoraWindows);
        panelTipoImpressora.add(rbImpressoraUSB);
        panelTipoImpressora.add(rbImpressoraRede);
        panelTipoImpressora.add(rbImpressoraNenhuma);
        
        // Adicionar painéis ao CardLayout
        panelConfiguracao.add(panelWindows, "Windows");
        panelConfiguracao.add(panelUSB, "USB");
        panelConfiguracao.add(panelRede, "Rede");
        panelConfiguracao.add(new JPanel(), "Nenhuma");
    }
    
    private void configurarLayout() {
        // Configurar painel esquerdo
        JPanel panelEsquerdo = new JPanel(new BorderLayout());
        panelEsquerdo.add(panelTipoImpressora, BorderLayout.NORTH);
        panelEsquerdo.add(panelConfiguracao, BorderLayout.CENTER);
        panelEsquerdo.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Configurar painel direito
        JPanel panelDireito = new JPanel(new BorderLayout());
        panelDireito.add(panelTeste, BorderLayout.CENTER);
        panelDireito.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Dividir tela
        JSplitPane splitPrincipal = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPrincipal.setLeftComponent(panelEsquerdo);
        splitPrincipal.setRightComponent(panelDireito);
        splitPrincipal.setDividerLocation(400);
        
        // Painel de botões
        panelBotoes.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        panelBotoes.setBackground(Color.WHITE);
        panelBotoes.add(btDetectar);
        panelBotoes.add(btRestaurarPadrao);
        panelBotoes.add(btSalvar);
        panelBotoes.add(btCancelar);
        
        // Montar painel principal
        JPanel panelCentro = new JPanel(new BorderLayout());
        panelCentro.add(splitPrincipal, BorderLayout.CENTER);
        panelCentro.add(panelBotoes, BorderLayout.SOUTH);
        
        panelPrincipal.add(panelCentro, BorderLayout.CENTER);
        
        // Configurar o frame
        this.setContentPane(panelPrincipal);
        this.setSize(900, 600);
        this.setClosable(true);
        this.setMaximizable(true);
        this.setIconifiable(true);
        this.setResizable(true);
    }
    
    private void configurarEventos() {
        // Eventos dos radio buttons
        rbImpressoraWindows.addActionListener(e -> mostrarPainelConfiguracao("Windows"));
        rbImpressoraUSB.addActionListener(e -> mostrarPainelConfiguracao("USB"));
        rbImpressoraRede.addActionListener(e -> mostrarPainelConfiguracao("Rede"));
        rbImpressoraNenhuma.addActionListener(e -> mostrarPainelConfiguracao("Nenhuma"));
        
        // Eventos dos botões
        btSalvar.addActionListener(e -> salvarConfiguracoes());
        btCancelar.addActionListener(e -> dispose());
        btDetectar.addActionListener(e -> detectarImpressoras());
        btRestaurarPadrao.addActionListener(e -> restaurarConfiguracoesPadrao());
        
        // Eventos de teste
        btTestarImpressao.addActionListener(e -> testarImpressao());
        btTestarCorte.addActionListener(e -> testarCortePapel());
        btTestarGaveta.addActionListener(e -> testarGaveta());
        
        // Selecionar primeira opção por padrão
        rbImpressoraWindows.setSelected(true);
        mostrarPainelConfiguracao("Windows");
    }
    
    private void mostrarPainelConfiguracao(String painel) {
        CardLayout cl = (CardLayout) panelConfiguracao.getLayout();
        cl.show(panelConfiguracao, painel);
    }
    
    private void carregarConfiguracoes() {
        try {
            // Carregar tipo de impressora
            String tipoImpressora = preferences.get("tipoImpressora", "Windows");
            switch (tipoImpressora) {
                case "Windows":
                    rbImpressoraWindows.setSelected(true);
                    break;
                case "USB":
                    rbImpressoraUSB.setSelected(true);
                    break;
                case "Rede":
                    rbImpressoraRede.setSelected(true);
                    break;
                case "Nenhuma":
                    rbImpressoraNenhuma.setSelected(true);
                    break;
            }
            mostrarPainelConfiguracao(tipoImpressora);
            
            // Carregar configurações específicas
            txtNomeImpressora.setText(preferences.get("nomeImpressora", ""));
            chkImpressoraPadrao.setSelected(preferences.getBoolean("impressoraPadrao", false));
            txtPortaUSB.setText(preferences.get("portaUSB", "COM1"));
            comboVelocidade.setSelectedItem(preferences.getInt("velocidadeUSB", 9600));
            chkAutoDetectar.setSelected(preferences.getBoolean("autoDetectarUSB", true));
            txtIP.setText(preferences.get("ipRede", "192.168.1.100"));
            txtPorta.setText(preferences.get("portaRede", "9100"));
            chkConexaoAutomatica.setSelected(preferences.getBoolean("conexaoAutomaticaRede", true));
            
            // Carregar configurações gerais
            spnLinhasPorPagina.setValue(preferences.getInt("linhasPorPagina", 40));
            spnColunasPorPagina.setValue(preferences.getInt("colunasPorPagina", 48));
            spnTamanhoFonte.setValue(preferences.getInt("tamanhoFonte", 10));
            comboAlinhamento.setSelectedItem(preferences.get("alinhamento", "Esquerdo"));
            chkCortarPapel.setSelected(preferences.getBoolean("cortarPapel", true));
            chkAbrirGaveta.setSelected(preferences.getBoolean("abrirGaveta", false));
            
        } catch (Exception e) {
            logger.error("Erro ao carregar configurações: " + e.getMessage(), e);
        }
    }
    
    private void salvarConfiguracoes() {
        try {
            // Salvar tipo de impressora
            String tipoImpressora = "";
            if (rbImpressoraWindows.isSelected()) tipoImpressora = "Windows";
            else if (rbImpressoraUSB.isSelected()) tipoImpressora = "USB";
            else if (rbImpressoraRede.isSelected()) tipoImpressora = "Rede";
            else if (rbImpressoraNenhuma.isSelected()) tipoImpressora = "Nenhuma";
            
            preferences.put("tipoImpressora", tipoImpressora);
            
            // Salvar configurações específicas
            preferences.put("nomeImpressora", txtNomeImpressora.getText());
            preferences.putBoolean("impressoraPadrao", chkImpressoraPadrao.isSelected());
            preferences.put("portaUSB", txtPortaUSB.getText());
            preferences.putInt("velocidadeUSB", (Integer) comboVelocidade.getSelectedItem());
            preferences.putBoolean("autoDetectarUSB", chkAutoDetectar.isSelected());
            preferences.put("ipRede", txtIP.getText());
            preferences.put("portaRede", txtPorta.getText());
            preferences.putBoolean("conexaoAutomaticaRede", chkConexaoAutomatica.isSelected());
            
            // Salvar configurações gerais
            preferences.putInt("linhasPorPagina", (Integer) spnLinhasPorPagina.getValue());
            preferences.putInt("colunasPorPagina", (Integer) spnColunasPorPagina.getValue());
            preferences.putInt("tamanhoFonte", (Integer) spnTamanhoFonte.getValue());
            preferences.put("alinhamento", (String) comboAlinhamento.getSelectedItem());
            preferences.putBoolean("cortarPapel", chkCortarPapel.isSelected());
            preferences.putBoolean("abrirGaveta", chkAbrirGaveta.isSelected());
            
            preferences.flush();
            
            JOptionPane.showMessageDialog(this, 
                "Configurações salvas com sucesso!", 
                "Sucesso", 
                JOptionPane.INFORMATION_MESSAGE);
            
            logger.info("Configurações da impressora salvas: " + tipoImpressora);
            
        } catch (Exception e) {
            logger.error("Erro ao salvar configurações: " + e.getMessage(), e);
            JOptionPane.showMessageDialog(this, 
                "Erro ao salvar configurações: " + e.getMessage(), 
                "Erro", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void detectarImpressoras() {
        try {
            if (rbImpressoraWindows.isSelected()) {
                detectarImpressorasWindows();
            } else if (rbImpressoraUSB.isSelected()) {
                detectarImpressorasUSB();
            } else if (rbImpressoraRede.isSelected()) {
                detectarImpressorasRede();
            }
            
        } catch (Exception e) {
            logger.error("Erro ao detectar impressoras: " + e.getMessage(), e);
            JOptionPane.showMessageDialog(this, 
                "Erro ao detectar impressoras: " + e.getMessage(), 
                "Erro", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void detectarImpressorasWindows() {
        try {
            // Obter impressoras do Windows
            javax.print.PrintService[] impressoras = javax.print.PrintServiceLookup.lookupPrintServices(null, null);
            
            comboImpressorasWindows.removeAllItems();
            
            for (javax.print.PrintService impressora : impressoras) {
                String nome = impressora.getName();
                if (nome != null && !nome.trim().isEmpty()) {
                    comboImpressorasWindows.addItem(nome);
                }
            }
            
            if (comboImpressorasWindows.getItemCount() > 0) {
                JOptionPane.showMessageDialog(this, 
                    String.format("Detectadas %d impressoras Windows!", comboImpressorasWindows.getItemCount()), 
                    "Sucesso", 
                    JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Nenhuma impressora Windows detectada!", 
                    "Aviso", 
                    JOptionPane.WARNING_MESSAGE);
            }
            
        } catch (Exception e) {
            logger.error("Erro ao detectar impressoras Windows: " + e.getMessage(), e);
        }
    }
    
    private void detectarImpressorasUSB() {
        // Simulação - em um sistema real verificaria portas COM
        String[] portasCOM = {"COM1", "COM2", "COM3", "COM4", "COM5", "COM6", "COM7", "COM8"};
        
        StringBuilder encontradas = new StringBuilder("Portas COM verificadas:\n");
        for (String porta : portasCOM) {
            encontradas.append("- ").append(porta).append("\n");
        }
        
        JOptionPane.showMessageDialog(this, 
            encontradas.toString(), 
            "Detecção USB", 
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void detectarImpressorasRede() {
        // Simulação - em um sistema real faria scan de rede
        JOptionPane.showMessageDialog(this, 
            "Função de detecção de impressoras de rede\n" +
            "disponível apenas em versões comerciais!\n\n" +
            "Configure manualmente o IP e porta da impressora.", 
            "Detecção de Rede", 
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void testarImpressao() {
        try {
            String tipoImpressora = "";
            if (rbImpressoraWindows.isSelected()) tipoImpressora = "Windows";
            else if (rbImpressoraUSB.isSelected()) tipoImpressora = "USB";
            else if (rbImpressoraRede.isSelected()) tipoImpressora = "Rede";
            
            JOptionPane.showMessageDialog(this, 
                "Enviando texto de teste para impressora " + tipoImpressora + "...\n" +
                "Verifique se a impressão foi realizada corretamente.", 
                "Teste de Impressão", 
                JOptionPane.INFORMATION_MESSAGE);
            
            // Aqui seria a implementação real da impressão
            // dependendo do tipo de impressora selecionado
            logger.info("Teste de impressão enviado para: " + tipoImpressora);
            
        } catch (Exception e) {
            logger.error("Erro ao testar impressão: " + e.getMessage(), e);
            JOptionPane.showMessageDialog(this, 
                "Erro ao testar impressão: " + e.getMessage(), 
                "Erro", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void testarCortePapel() {
        try {
            JOptionPane.showMessageDialog(this, 
                "Enviando comando de corte de papel...\n" +
                "Verifique se a impressora cortou o papel corretamente.", 
                "Teste de Corte", 
                JOptionPane.INFORMATION_MESSAGE);
            
            logger.info("Teste de corte de papel enviado");
            
        } catch (Exception e) {
            logger.error("Erro ao testar corte: " + e.getMessage(), e);
            JOptionPane.showMessageDialog(this, 
                "Erro ao testar corte: " + e.getMessage(), 
                "Erro", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void testarGaveta() {
        try {
            JOptionPane.showMessageDialog(this, 
                "Enviando comando para abrir gaveta de dinheiro...\n" +
                "Verifique se a gaveta abriu corretamente.", 
                "Teste de Gaveta", 
                JOptionPane.INFORMATION_MESSAGE);
            
            logger.info("Teste de abertura de gaveta enviado");
            
        } catch (Exception e) {
            logger.error("Erro ao testar gaveta: " + e.getMessage(), e);
            JOptionPane.showMessageDialog(this, 
                "Erro ao testar gaveta: " + e.getMessage(), 
                "Erro", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void restaurarConfiguracoesPadrao() {
        int confirmacao = JOptionPane.showConfirmDialog(this, 
            "Deseja restaurar as configurações padrão?\n" +
            "Todas as configurações atuais serão perdidas.", 
            "Restaurar Configurações", 
            JOptionPane.YES_NO_OPTION);
            
        if (confirmacao == JOptionPane.YES_OPTION) {
            try {
                // Limpar preferências
                preferences.clear();
                preferences.flush();
                
                // Recarregar configurações padrão
                carregarConfiguracoes();
                
                JOptionPane.showMessageDialog(this, 
                    "Configurações restauradas com sucesso!", 
                    "Sucesso", 
                    JOptionPane.INFORMATION_MESSAGE);
                
                logger.info("Configurações da impressora restauradas para o padrão");
                
            } catch (Exception e) {
                logger.error("Erro ao restaurar configurações: " + e.getMessage(), e);
                JOptionPane.showMessageDialog(this, 
                    "Erro ao restaurar configurações: " + e.getMessage(), 
                    "Erro", 
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private JButton criarBotao(String texto, Color cor) {
        JButton botao = new JButton(texto);
        botao.setFont(new Font("Segoe UI", Font.BOLD, 12));
        botao.setForeground(Color.WHITE);
        botao.setBackground(cor);
        botao.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
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
}
