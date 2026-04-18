package com.br.hermescomercialnetbeans.view;

import com.br.hermescomercialnetbeans.dao.MovimentoCaixaDao;
import com.br.hermescomercialnetbeans.model.MovimentoCaixa;
import com.br.hermescomercialnetbeans.model.Usuario;
import org.apache.logging.log4j.LogManager;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Tela elegante para Controle de Caixa
 * @author marcos
 */
public class TelaControleCaixaNova extends JInternalFrame {
    
    private static final org.apache.logging.log4j.Logger logger = LogManager.getLogger(TelaControleCaixaNova.class);
    
    // Componentes da interface
    private JPanel panelPrincipal;
    private JPanel panelStatus;
    private JPanel panelOperacoes;
    private JPanel panelMovimentos;
    private JScrollPane scrollPaneTabela;
    
    // Status do caixa
    private JLabel lblStatusCaixa;
    private JLabel lblSaldoInicial;
    private JLabel lblSaldoAtual;
    private JLabel lblTotalVendas;
    private JLabel lblTotalSangrias;
    private JLabel lblTotalSuprimentos;
    
    // Operações
    private JTextField txtValor;
    private JTextArea txtObservacao;
    private JButton btAbrirCaixa;
    private JButton btFecharCaixa;
    private JButton btSangria;
    private JButton btSuprimento;
    private JButton btConsultar;
    
    // Tabela
    private JTable tabelaMovimentos;
    private DefaultTableModel modeloTabela;
    
    // DAO e dados
    private MovimentoCaixaDao movimentoCaixaDao;
    private Usuario usuarioLogado;
    private MovimentoCaixa caixaAberto;
    
    // Cores do tema
    private static final Color COR_PRIMARIA = new Color(41, 128, 185);
    private static final Color COR_SUCESSO = new Color(39, 174, 96);
    private static final Color COR_PERIGO = new Color(231, 76, 60);
    private static final Color COR_ADVERTENCIA = new Color(243, 156, 18);
    private static final Color COR_FUNDO = new Color(236, 240, 241);
    private static final Color COR_TEXTO = new Color(44, 62, 80);
    
    public TelaControleCaixaNova(Usuario usuario) {
        super("Controle de Caixa");
        this.usuarioLogado = usuario;
        this.movimentoCaixaDao = new MovimentoCaixaDao();
        
        inicializarComponentes();
        configurarLayout();
        configurarEventos();
        verificarStatusCaixa();
        carregarMovimentos();
    }
    
    private void inicializarComponentes() {
        // Painéis principais
        panelPrincipal = new JPanel(new BorderLayout());
        panelStatus = new JPanel(new GridLayout(2, 4, 10, 5));
        panelOperacoes = new JPanel(new BorderLayout());
        panelMovimentos = new JPanel(new BorderLayout());
        
        // Labels de status
        lblStatusCaixa = new JLabel("Status: FECHADO");
        lblStatusCaixa.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblStatusCaixa.setForeground(COR_PERIGO);
        
        lblSaldoInicial = new JLabel("Saldo Inicial: R$ 0,00");
        lblSaldoInicial.setFont(new Font("Segoe UI", Font.BOLD, 12));
        
        lblSaldoAtual = new JLabel("Saldo Atual: R$ 0,00");
        lblSaldoAtual.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblSaldoAtual.setForeground(COR_SUCESSO);
        
        lblTotalVendas = new JLabel("Vendas: R$ 0,00");
        lblTotalVendas.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        
        lblTotalSangrias = new JLabel("Sangrias: R$ 0,00");
        lblTotalSangrias.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblTotalSangrias.setForeground(COR_PERIGO);
        
        lblTotalSuprimentos = new JLabel("Suprimentos: R$ 0,00");
        lblTotalSuprimentos.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblTotalSuprimentos.setForeground(COR_SUCESSO);
        
        // Campos de operação
        txtValor = criarCampoTexto("0,00", 15);
        txtObservacao = new JTextArea(3, 30);
        txtObservacao.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        txtObservacao.setLineWrap(true);
        txtObservacao.setWrapStyleWord(true);
        txtObservacao.setBorder(BorderFactory.createLineBorder(COR_PRIMARIA, 1));
        
        // Botões
        btAbrirCaixa = criarBotao("Abrir Caixa", COR_SUCESSO);
        btFecharCaixa = criarBotao("Fechar Caixa", COR_PERIGO);
        btSangria = criarBotao("Sangria", COR_PERIGO);
        btSuprimento = criarBotao("Suprimento", COR_SUCESSO);
        btConsultar = criarBotao("Consultar", COR_PRIMARIA);
        
        // Tabela
        modeloTabela = new DefaultTableModel();
        modeloTabela.addColumn("Data/Hora");
        modeloTabela.addColumn("Tipo");
        modeloTabela.addColumn("Valor");
        modeloTabela.addColumn("Usuário");
        modeloTabela.addColumn("Observação");
        
        tabelaMovimentos = new JTable(modeloTabela);
        tabelaMovimentos.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        tabelaMovimentos.setRowHeight(25);
        tabelaMovimentos.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        tabelaMovimentos.getTableHeader().setBackground(COR_PRIMARIA);
        tabelaMovimentos.getTableHeader().setForeground(Color.WHITE);
        
        scrollPaneTabela = new JScrollPane(tabelaMovimentos);
        scrollPaneTabela.setPreferredSize(new Dimension(800, 400));
    }
    
    private void configurarLayout() {
        // Configurar painel de status
        panelStatus.setBorder(BorderFactory.createTitledBorder("Status do Caixa"));
        panelStatus.setBackground(Color.WHITE);
        panelStatus.add(lblStatusCaixa);
        panelStatus.add(lblSaldoInicial);
        panelStatus.add(lblSaldoAtual);
        panelStatus.add(new JLabel("")); // Espaço
        panelStatus.add(lblTotalVendas);
        panelStatus.add(lblTotalSangrias);
        panelStatus.add(lblTotalSuprimentos);
        panelStatus.add(new JLabel("")); // Espaço
        
        // Configurar painel de operações
        JPanel panelCampos = new JPanel(new GridBagLayout());
        panelCampos.setBorder(BorderFactory.createTitledBorder("Operações"));
        panelCampos.setBackground(Color.WHITE);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        gbc.gridx = 0; gbc.gridy = 0;
        panelCampos.add(new JLabel("Valor:"), gbc);
        gbc.gridx = 1;
        panelCampos.add(txtValor, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1;
        gbc.gridwidth = 2;
        panelCampos.add(new JLabel("Observação:"), gbc);
        
        gbc.gridx = 0; gbc.gridy = 2;
        gbc.gridwidth = 2;
        panelCampos.add(new JScrollPane(txtObservacao), gbc);
        
        // Painel de botões
        JPanel panelBotoesOperacao = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        panelBotoesOperacao.setBackground(Color.WHITE);
        panelBotoesOperacao.add(btAbrirCaixa);
        panelBotoesOperacao.add(btFecharCaixa);
        panelBotoesOperacao.add(btSangria);
        panelBotoesOperacao.add(btSuprimento);
        panelBotoesOperacao.add(btConsultar);
        
        panelOperacoes.add(panelCampos, BorderLayout.CENTER);
        panelOperacoes.add(panelBotoesOperacao, BorderLayout.SOUTH);
        
        // Configurar painel de movimentos
        panelMovimentos.setBorder(BorderFactory.createTitledBorder("Movimentos do Caixa"));
        panelMovimentos.setBackground(Color.WHITE);
        panelMovimentos.add(scrollPaneTabela, BorderLayout.CENTER);
        
        // Montar painel principal
        JSplitPane splitPrincipal = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        splitPrincipal.setTopComponent(panelStatus);
        splitPrincipal.setBottomComponent(panelOperacoes);
        splitPrincipal.setDividerLocation(150);
        
        JSplitPane splitHorizontal = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitHorizontal.setLeftComponent(splitPrincipal);
        splitHorizontal.setRightComponent(panelMovimentos);
        splitHorizontal.setDividerLocation(500);
        
        panelPrincipal.add(splitHorizontal, BorderLayout.CENTER);
        
        // Configurar o frame
        this.setContentPane(panelPrincipal);
        this.setSize(1200, 700);
        this.setClosable(true);
        this.setMaximizable(true);
        this.setIconifiable(true);
        this.setResizable(true);
    }
    
    private void configurarEventos() {
        btAbrirCaixa.addActionListener(e -> abrirCaixa());
        btFecharCaixa.addActionListener(e -> fecharCaixa());
        btSangria.addActionListener(e -> realizarSangria());
        btSuprimento.addActionListener(e -> realizarSuprimento());
        btConsultar.addActionListener(e -> consultarMovimentos());
    }
    
    private void verificarStatusCaixa() {
        try {
            caixaAberto = movimentoCaixaDao.buscarCaixaAberto();
            
            if (caixaAberto != null) {
                lblStatusCaixa.setText("Status: ABERTO");
                lblStatusCaixa.setForeground(COR_SUCESSO);
                lblSaldoInicial.setText("Saldo Inicial: R$ " + 
                    String.format("%.2f", caixaAberto.getValor()));
                
                // Calcular saldo atual
                BigDecimal saldoAtual = calcularSaldoAtual();
                lblSaldoAtual.setText("Saldo Atual: R$ " + 
                    String.format("%.2f", saldoAtual));
                
                // Habilitar/desabilitar botões
                btAbrirCaixa.setEnabled(false);
                btFecharCaixa.setEnabled(true);
                btSangria.setEnabled(true);
                btSuprimento.setEnabled(true);
            } else {
                lblStatusCaixa.setText("Status: FECHADO");
                lblStatusCaixa.setForeground(COR_PERIGO);
                lblSaldoInicial.setText("Saldo Inicial: R$ 0,00");
                lblSaldoAtual.setText("Saldo Atual: R$ 0,00");
                
                // Habilitar/desabilitar botões
                btAbrirCaixa.setEnabled(true);
                btFecharCaixa.setEnabled(false);
                btSangria.setEnabled(false);
                btSuprimento.setEnabled(false);
            }
            
            // Calcular totais
            calcularTotais();
            
        } catch (Exception e) {
            logger.error("Erro ao verificar status do caixa: " + e.getMessage(), e);
            JOptionPane.showMessageDialog(this, "Erro ao verificar status do caixa!", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void abrirCaixa() {
        try {
            String valorStr = txtValor.getText().trim();
            if (valorStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Informe o valor inicial do caixa!", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            BigDecimal valorInicial = new BigDecimal(valorStr.replace(",", "."));
            
            if (valorInicial.compareTo(BigDecimal.ZERO) < 0) {
                JOptionPane.showMessageDialog(this, "Valor inicial não pode ser negativo!", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            // Criar movimento de abertura
            MovimentoCaixa movimento = new MovimentoCaixa();
            movimento.setTipoMovimento(MovimentoCaixa.TipoMovimento.ABERTURA);
            movimento.setValor(valorInicial.doubleValue());
            movimento.setUsuarioId(usuarioLogado != null ? usuarioLogado.getId() : 1L);
            movimento.setUsuarioNome(usuarioLogado != null ? usuarioLogado.getNome() : "Sistema");
            movimento.setObservacao(txtObservacao.getText().trim());
            
            movimentoCaixaDao.salvar(movimento);
            
            JOptionPane.showMessageDialog(this, "Caixa aberto com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            
            // Limpar campos
            txtValor.setText("0,00");
            txtObservacao.setText("");
            
            // Atualizar status
            verificarStatusCaixa();
            carregarMovimentos();
            
        } catch (Exception e) {
            logger.error("Erro ao abrir caixa: " + e.getMessage(), e);
            JOptionPane.showMessageDialog(this, "Erro ao abrir caixa: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void fecharCaixa() {
        try {
            int confirmacao = JOptionPane.showConfirmDialog(this, 
                "Deseja fechar o caixa?", 
                "Confirmar Fechamento", 
                JOptionPane.YES_NO_OPTION);
                
            if (confirmacao == JOptionPane.YES_OPTION) {
                BigDecimal saldoAtual = calcularSaldoAtual();
                
                // Criar movimento de fechamento
                MovimentoCaixa movimento = new MovimentoCaixa();
                movimento.setTipoMovimento(MovimentoCaixa.TipoMovimento.FECHAMENTO);
                movimento.setValor(saldoAtual.doubleValue());
                movimento.setUsuarioId(usuarioLogado != null ? usuarioLogado.getId() : 1L);
                movimento.setUsuarioNome(usuarioLogado != null ? usuarioLogado.getNome() : "Sistema");
                movimento.setObservacao("Fechamento automático do caixa");
                
                movimentoCaixaDao.salvar(movimento);
                
                JOptionPane.showMessageDialog(this, 
                    "Caixa fechado com sucesso!\nSaldo Final: R$ " + String.format("%.2f", saldoAtual), 
                    "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                
                // Atualizar status
                verificarStatusCaixa();
                carregarMovimentos();
            }
            
        } catch (Exception e) {
            logger.error("Erro ao fechar caixa: " + e.getMessage(), e);
            JOptionPane.showMessageDialog(this, "Erro ao fechar caixa: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void realizarSangria() {
        try {
            String valorStr = txtValor.getText().trim();
            if (valorStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Informe o valor da sangria!", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            BigDecimal valorSangria = new BigDecimal(valorStr.replace(",", "."));
            
            if (valorSangria.compareTo(BigDecimal.ZERO) <= 0) {
                JOptionPane.showMessageDialog(this, "Valor da sangria deve ser positivo!", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            BigDecimal saldoAtual = calcularSaldoAtual();
            if (valorSangria.compareTo(saldoAtual) > 0) {
                JOptionPane.showMessageDialog(this, "Valor da sangria maior que o saldo atual!", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            int confirmacao = JOptionPane.showConfirmDialog(this, 
                "Deseja realizar uma sangria de R$ " + String.format("%.2f", valorSangria) + "?", 
                "Confirmar Sangria", 
                JOptionPane.YES_NO_OPTION);
                
            if (confirmacao == JOptionPane.YES_OPTION) {
                // Criar movimento de sangria
                MovimentoCaixa movimento = new MovimentoCaixa();
                movimento.setTipoMovimento(MovimentoCaixa.TipoMovimento.SANGRIA);
                movimento.setValor(valorSangria.doubleValue());
                movimento.setUsuarioId(usuarioLogado != null ? usuarioLogado.getId() : 1L);
                movimento.setUsuarioNome(usuarioLogado != null ? usuarioLogado.getNome() : "Sistema");
                movimento.setObservacao(txtObservacao.getText().trim());
                
                movimentoCaixaDao.salvar(movimento);
                
                JOptionPane.showMessageDialog(this, "Sangria realizada com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                
                // Limpar campos
                txtValor.setText("0,00");
                txtObservacao.setText("");
                
                // Atualizar status
                verificarStatusCaixa();
                carregarMovimentos();
            }
            
        } catch (Exception e) {
            logger.error("Erro ao realizar sangria: " + e.getMessage(), e);
            JOptionPane.showMessageDialog(this, "Erro ao realizar sangria: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void realizarSuprimento() {
        try {
            String valorStr = txtValor.getText().trim();
            if (valorStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Informe o valor do suprimento!", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            BigDecimal valorSuprimento = new BigDecimal(valorStr.replace(",", "."));
            
            if (valorSuprimento.compareTo(BigDecimal.ZERO) <= 0) {
                JOptionPane.showMessageDialog(this, "Valor do suprimento deve ser positivo!", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            int confirmacao = JOptionPane.showConfirmDialog(this, 
                "Deseja realizar um suprimento de R$ " + String.format("%.2f", valorSuprimento) + "?", 
                "Confirmar Suprimento", 
                JOptionPane.YES_NO_OPTION);
                
            if (confirmacao == JOptionPane.YES_OPTION) {
                // Criar movimento de suprimento
                MovimentoCaixa movimento = new MovimentoCaixa();
                movimento.setTipoMovimento(MovimentoCaixa.TipoMovimento.SUPRIMENTO);
                movimento.setValor(valorSuprimento.doubleValue());
                movimento.setUsuarioId(usuarioLogado != null ? usuarioLogado.getId() : 1L);
                movimento.setUsuarioNome(usuarioLogado != null ? usuarioLogado.getNome() : "Sistema");
                movimento.setObservacao(txtObservacao.getText().trim());
                
                movimentoCaixaDao.salvar(movimento);
                
                JOptionPane.showMessageDialog(this, "Suprimento realizado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                
                // Limpar campos
                txtValor.setText("0,00");
                txtObservacao.setText("");
                
                // Atualizar status
                verificarStatusCaixa();
                carregarMovimentos();
            }
            
        } catch (Exception e) {
            logger.error("Erro ao realizar suprimento: " + e.getMessage(), e);
            JOptionPane.showMessageDialog(this, "Erro ao realizar suprimento: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void consultarMovimentos() {
        carregarMovimentos();
    }
    
    private void carregarMovimentos() {
        try {
            List<MovimentoCaixa> movimentos = movimentoCaixaDao.listarHoje();
            atualizarTabela(movimentos);
        } catch (Exception e) {
            logger.error("Erro ao carregar movimentos: " + e.getMessage(), e);
            JOptionPane.showMessageDialog(this, "Erro ao carregar movimentos!", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void atualizarTabela(List<MovimentoCaixa> movimentos) {
        modeloTabela.setRowCount(0);
        
        for (MovimentoCaixa movimento : movimentos) {
            Object[] linha = {
                movimento.getDataHora().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")),
                movimento.getTipoMovimento().getDescricao(),
                "R$ " + String.format("%.2f", movimento.getValor()),
                movimento.getUsuarioNome(),
                movimento.getObservacao()
            };
            modeloTabela.addRow(linha);
        }
    }
    
    private BigDecimal calcularSaldoAtual() {
        try {
            if (caixaAberto == null) return BigDecimal.ZERO;
            
            BigDecimal saldo = new BigDecimal(caixaAberto.getValor());
            
            List<MovimentoCaixa> movimentos = movimentoCaixaDao.listarHoje();
            for (MovimentoCaixa movimento : movimentos) {
                if (movimento.getId().equals(caixaAberto.getId())) continue; // Pular abertura
                
                switch (movimento.getTipoMovimento()) {
                    case VENDA:
                        saldo = saldo.add(new BigDecimal(movimento.getValor()));
                        break;
                    case SANGRIA:
                        saldo = saldo.subtract(new BigDecimal(movimento.getValor()));
                        break;
                    case SUPRIMENTO:
                        saldo = saldo.add(new BigDecimal(movimento.getValor()));
                        break;
                    case ABERTURA:
                        saldo = saldo.add(new BigDecimal(movimento.getValor()));
                        break;
                    case FECHAMENTO:
                        saldo = saldo.subtract(new BigDecimal(movimento.getValor()));
                        break;
                }
            }
            
            return saldo;
        } catch (Exception e) {
            logger.error("Erro ao calcular saldo atual: " + e.getMessage(), e);
            return BigDecimal.ZERO;
        }
    }
    
    private void calcularTotais() {
        try {
            List<MovimentoCaixa> movimentos = movimentoCaixaDao.listarHoje();
            
            BigDecimal totalVendas = BigDecimal.ZERO;
            BigDecimal totalSangrias = BigDecimal.ZERO;
            BigDecimal totalSuprimentos = BigDecimal.ZERO;
            BigDecimal totalCancelamentos = BigDecimal.ZERO;
            
            for (MovimentoCaixa movimento : movimentos) {
                BigDecimal valor = new BigDecimal(movimento.getValor());
                
                switch (movimento.getTipoMovimento()) {
                    case VENDA:
                        totalVendas = totalVendas.add(valor);
                        break;
                    case SANGRIA:
                        totalSangrias = totalSangrias.add(valor);
                        break;
                    case SUPRIMENTO:
                        totalSuprimentos = totalSuprimentos.add(valor);
                        break;
                    case CANCELAMENTO:
                        // Não soma cancelamentos nos totais
                        break;
                }
            }
            
            lblTotalVendas.setText("Vendas: R$ " + String.format("%.2f", totalVendas));
            lblTotalSangrias.setText("Sangrias: R$ " + String.format("%.2f", totalSangrias));
            lblTotalSuprimentos.setText("Suprimentos: R$ " + String.format("%.2f", totalSuprimentos));
            
        } catch (Exception e) {
            logger.error("Erro ao calcular totais: " + e.getMessage(), e);
        }
    }
    
    private JTextField criarCampoTexto(String placeholder, int columns) {
        JTextField campo = new JTextField(columns);
        campo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        campo.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(COR_PRIMARIA, 1),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        return campo;
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
