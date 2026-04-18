package com.br.hermescomercialnetbeans.view;

import com.br.hermescomercialnetbeans.dao.ProdutoDao;
import com.br.hermescomercialnetbeans.dao.VendaDao;
import com.br.hermescomercialnetbeans.dao.ItemVendaDao;
import com.br.hermescomercialnetbeans.dao.PagamentoDao;
import com.br.hermescomercialnetbeans.model.Produto;
import com.br.hermescomercialnetbeans.model.Venda;
import com.br.hermescomercialnetbeans.model.ItemVenda;
import com.br.hermescomercialnetbeans.model.Pagamento;
import com.br.hermescomercialnetbeans.model.Usuario;
import com.br.hermescomercialnetbeans.utils.EmissorCupomFiscal;
import org.apache.logging.log4j.LogManager;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Tela elegante de Ponto de Venda (PDV)
 * @author marcos
 */
public class TelaVendaNova extends JInternalFrame {
    
    private static final org.apache.logging.log4j.Logger logger = LogManager.getLogger(TelaVendaNova.class);
    
    // Componentes da interface
    private JPanel panelPrincipal;
    private JPanel panelTopo;
    private JPanel panelCentro;
    private JPanel panelDireita;
    private JPanel panelInferior;
    
    // Topo - Informações da venda
    private JLabel lblNumeroVenda;
    private JLabel lblDataHora;
    private JLabel lblOperador;
    private JLabel lblCliente;
    
    // Centro - Produtos
    private JTextField txtCodigoBarras;
    private JTextField txtDescricaoProduto;
    private JTextField txtQuantidade;
    private JTextField txtValorUnitario;
    private JTextField txtValorTotal;
    private JButton btAdicionarItem;
    private JButton btPesquisarProduto;
    
    // Tabela de itens
    private JTable tabelaItens;
    private DefaultTableModel modeloTabela;
    private JScrollPane scrollPaneTabela;
    
    // Direita - Resumo
    private JLabel lblSubtotal;
    private JLabel lblDesconto;
    private JLabel lblTotal;
    private JTextField txtDesconto;
    private JTextField txtValorRecebido;
    private JTextField txtTroco;
    
    // Inferior - Pagamento
    private JComboBox<String> comboFormaPagamento;
    private JButton btFinalizarVenda;
    private JButton btCancelarVenda;
    private JButton btEmitirCupom;
    
    // DAOs e modelos
    private ProdutoDao produtoDao;
    private VendaDao vendaDao;
    private ItemVendaDao itemVendaDao;
    private PagamentoDao pagamentoDao;
    private Usuario usuarioLogado;
    
    // Dados da venda
    private List<ItemVenda> itensVenda;
    private Venda vendaAtual;
    private BigDecimal subtotal;
    private BigDecimal total;
    
    // Cores do tema
    private static final Color COR_PRIMARIA = new Color(41, 128, 185);
    private static final Color COR_SUCESSO = new Color(39, 174, 96);
    private static final Color COR_PERIGO = new Color(231, 76, 60);
    private static final Color COR_FUNDO = new Color(236, 240, 241);
    private static final Color COR_TEXTO = new Color(44, 62, 80);
    
    public TelaVendaNova(Usuario usuario) {
        super("Ponto de Venda - PDV");
        this.usuarioLogado = usuario;
        this.produtoDao = new ProdutoDao();
        this.vendaDao = new VendaDao();
        this.itemVendaDao = new ItemVendaDao();
        this.pagamentoDao = new PagamentoDao();
        this.itensVenda = new ArrayList<>();
        
        inicializarComponentes();
        configurarLayout();
        configurarEventos();
        iniciarNovaVenda();
    }
    
    private void inicializarComponentes() {
        // Painéis principais
        panelPrincipal = new JPanel(new BorderLayout());
        panelTopo = new JPanel(new GridLayout(2, 4, 10, 5));
        panelCentro = new JPanel(new BorderLayout());
        panelDireita = new JPanel(new BorderLayout());
        panelInferior = new JPanel(new BorderLayout());
        
        // Topo
        lblNumeroVenda = new JLabel("Venda: #0001");
        lblNumeroVenda.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblNumeroVenda.setForeground(COR_PRIMARIA);
        
        lblDataHora = new JLabel();
        lblDataHora.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        
        lblOperador = new JLabel("Operador: " + (usuarioLogado != null ? usuarioLogado.getNome() : "Sistema"));
        lblOperador.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        
        lblCliente = new JLabel("Cliente: Consumidor Final");
        lblCliente.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        
        // Centro - Produtos
        txtCodigoBarras = criarCampoTexto("Código de Barras", 15);
        txtDescricaoProduto = criarCampoTexto("Descrição do Produto", 30);
        txtQuantidade = criarCampoTexto("1", 8);
        txtValorUnitario = criarCampoTexto("0,00", 10);
        txtValorTotal = criarCampoTexto("0,00", 10);
        txtValorTotal.setEditable(false);
        
        btAdicionarItem = criarBotao("Adicionar", COR_SUCESSO);
        btPesquisarProduto = criarBotao("Pesquisar", COR_PRIMARIA);
        
        // Tabela de itens
        modeloTabela = new DefaultTableModel();
        modeloTabela.addColumn("Código");
        modeloTabela.addColumn("Descrição");
        modeloTabela.addColumn("Qtd");
        modeloTabela.addColumn("Unitário");
        modeloTabela.addColumn("Total");
        
        tabelaItens = new JTable(modeloTabela);
        tabelaItens.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        tabelaItens.setRowHeight(25);
        tabelaItens.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        tabelaItens.getTableHeader().setBackground(COR_PRIMARIA);
        tabelaItens.getTableHeader().setForeground(Color.WHITE);
        
        scrollPaneTabela = new JScrollPane(tabelaItens);
        scrollPaneTabela.setPreferredSize(new Dimension(600, 300));
        
        // Direita - Resumo
        lblSubtotal = new JLabel("Subtotal: R$ 0,00");
        lblSubtotal.setFont(new Font("Segoe UI", Font.BOLD, 14));
        
        txtDesconto = criarCampoTexto("0,00", 10);
        
        lblDesconto = new JLabel("Desconto: R$ 0,00");
        lblDesconto.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        
        lblTotal = new JLabel("TOTAL: R$ 0,00");
        lblTotal.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTotal.setForeground(COR_SUCESSO);
        
        txtValorRecebido = criarCampoTexto("0,00", 10);
        txtTroco = criarCampoTexto("0,00", 10);
        txtTroco.setEditable(false);
        
        // Inferior - Pagamento
        comboFormaPagamento = new JComboBox<>(new String[]{
            "Dinheiro", "Cartão de Crédito", "Cartão de Débito", 
            "PIX", "Transferência", "Cheque"
        });
        comboFormaPagamento.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        
        btFinalizarVenda = criarBotao("Finalizar Venda", COR_SUCESSO);
        btCancelarVenda = criarBotao("Cancelar", COR_PERIGO);
        btEmitirCupom = criarBotao("Emitir Cupom", COR_PRIMARIA);
    }
    
    private void configurarLayout() {
        // Configurar topo
        panelTopo.setBorder(BorderFactory.createTitledBorder("Informações da Venda"));
        panelTopo.setBackground(Color.WHITE);
        panelTopo.add(lblNumeroVenda);
        panelTopo.add(lblDataHora);
        panelTopo.add(lblOperador);
        panelTopo.add(lblCliente);
        
        // Configurar centro - parte superior (entrada de produtos)
        JPanel panelEntrada = new JPanel(new GridBagLayout());
        panelEntrada.setBorder(BorderFactory.createTitledBorder("Adicionar Item"));
        panelEntrada.setBackground(Color.WHITE);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        gbc.gridx = 0; gbc.gridy = 0;
        panelEntrada.add(new JLabel("Código:"), gbc);
        gbc.gridx = 1;
        panelEntrada.add(txtCodigoBarras, gbc);
        gbc.gridx = 2;
        panelEntrada.add(btPesquisarProduto, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1;
        panelEntrada.add(new JLabel("Descrição:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 2;
        panelEntrada.add(txtDescricaoProduto, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2;
        gbc.gridwidth = 1;
        panelEntrada.add(new JLabel("Quantidade:"), gbc);
        gbc.gridx = 1;
        panelEntrada.add(txtQuantidade, gbc);
        gbc.gridx = 2;
        panelEntrada.add(new JLabel("Unitário:"), gbc);
        gbc.gridx = 3;
        panelEntrada.add(txtValorUnitario, gbc);
        
        gbc.gridx = 0; gbc.gridy = 3;
        gbc.gridwidth = 2;
        panelEntrada.add(btAdicionarItem, gbc);
        gbc.gridx = 2; gbc.gridwidth = 2;
        panelEntrada.add(new JLabel("Total:"), gbc);
        gbc.gridx = 3;
        panelEntrada.add(txtValorTotal, gbc);
        
        // Montar centro
        panelCentro.add(panelEntrada, BorderLayout.NORTH);
        panelCentro.add(scrollPaneTabela, BorderLayout.CENTER);
        
        // Configurar direita - resumo
        JPanel panelResumo = new JPanel(new GridBagLayout());
        panelResumo.setBorder(BorderFactory.createTitledBorder("Resumo da Venda"));
        panelResumo.setBackground(Color.WHITE);
        
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.gridwidth = 2;
        panelResumo.add(lblSubtotal, gbc);
        
        gbc.gridy = 1;
        panelResumo.add(new JLabel("Desconto:"), gbc);
        gbc.gridx = 1;
        panelResumo.add(txtDesconto, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2;
        gbc.gridwidth = 2;
        panelResumo.add(lblDesconto, gbc);
        
        gbc.gridy = 3;
        panelResumo.add(lblTotal, gbc);
        
        gbc.gridy = 4;
        panelResumo.add(new JLabel("Valor Recebido:"), gbc);
        gbc.gridx = 1;
        panelResumo.add(txtValorRecebido, gbc);
        
        gbc.gridx = 0; gbc.gridy = 5;
        panelResumo.add(new JLabel("Troco:"), gbc);
        gbc.gridx = 1;
        panelResumo.add(txtTroco, gbc);
        
        panelDireita.add(panelResumo, BorderLayout.CENTER);
        
        // Configurar inferior - pagamento
        JPanel panelPagamento = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        panelPagamento.setBorder(BorderFactory.createTitledBorder("Pagamento"));
        panelPagamento.setBackground(Color.WHITE);
        
        panelPagamento.add(new JLabel("Forma:"));
        panelPagamento.add(comboFormaPagamento);
        panelPagamento.add(btFinalizarVenda);
        panelPagamento.add(btCancelarVenda);
        panelPagamento.add(btEmitirCupom);
        
        panelInferior.add(panelPagamento, BorderLayout.CENTER);
        
        // Montar painel principal
        JSplitPane splitPrincipal = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPrincipal.setLeftComponent(panelCentro);
        splitPrincipal.setRightComponent(panelDireita);
        splitPrincipal.setDividerLocation(700);
        
        panelPrincipal.add(panelTopo, BorderLayout.NORTH);
        panelPrincipal.add(splitPrincipal, BorderLayout.CENTER);
        panelPrincipal.add(panelInferior, BorderLayout.SOUTH);
        
        // Configurar o frame
        this.setContentPane(panelPrincipal);
        this.setSize(1000, 700);
        this.setClosable(true);
        this.setMaximizable(true);
        this.setIconifiable(true);
        this.setResizable(true);
    }
    
    private void configurarEventos() {
        // Evento do código de barras
        txtCodigoBarras.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    pesquisarProdutoPorCodigo();
                }
            }
        });
        
        // Evento do botão pesquisar
        btPesquisarProduto.addActionListener(e -> pesquisarProdutoPorCodigo());
        
        // Evento do botão adicionar
        btAdicionarItem.addActionListener(e -> adicionarItem());
        
        // Evento do campo quantidade
        txtQuantidade.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                calcularValorTotalItem();
            }
        });
        
        // Evento do campo valor unitário
        txtValorUnitario.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                calcularValorTotalItem();
            }
        });
        
        // Evento do campo desconto
        txtDesconto.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                calcularTotais();
                calcularTroco();
            }
        });
        
        // Evento do campo valor recebido
        txtValorRecebido.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                calcularTroco();
            }
        });
        
        // Evento dos botões
        btFinalizarVenda.addActionListener(e -> finalizarVenda());
        btCancelarVenda.addActionListener(e -> cancelarVenda());
        btEmitirCupom.addActionListener(e -> emitirCupomFiscal());
        
        // Timer para atualizar data/hora
        Timer timer = new Timer(1000, e -> atualizarDataHora());
        timer.start();
    }
    
    private void iniciarNovaVenda() {
        vendaAtual = new Venda();
        vendaAtual.setDataHora(LocalDateTime.now());
        vendaAtual.setUsuarioId(usuarioLogado != null ? usuarioLogado.getId() : (long)1);
        
        itensVenda.clear();
        modeloTabela.setRowCount(0);
        
        txtCodigoBarras.setText("");
        txtDescricaoProduto.setText("");
        txtQuantidade.setText("1");
        txtValorUnitario.setText("0,00");
        txtValorTotal.setText("0,00");
        txtDesconto.setText("0,00");
        txtValorRecebido.setText("0,00");
        txtTroco.setText("0,00");
        
        subtotal = BigDecimal.ZERO;
        total = BigDecimal.ZERO;
        
        atualizarDataHora();
        calcularTotais();
        
        txtCodigoBarras.requestFocus();
    }
    
    private void pesquisarProdutoPorCodigo() {
        String codigo = txtCodigoBarras.getText().trim();
        logger.info("Pesquisando produto com código: '" + codigo + "'");
        
        if (codigo.isEmpty()) {
            logger.info("Código vazio, retornando");
            return;
        }
        
        try {
            logger.info("Chamando produtoDao.buscarPorCodigo(" + codigo + ")");
            Produto produto = produtoDao.buscarPorCodigo(codigo);
            logger.info("Resultado da busca: " + (produto != null ? "ENCONTRADO - " + produto.getNome() : "NÃO ENCONTRADO"));
            
            if (produto != null) {
                txtDescricaoProduto.setText(produto.getNome());
                txtValorUnitario.setText(produto.getPreco() != null ? 
                    String.format("%.2f", produto.getPreco()) : "0,00");
                txtQuantidade.requestFocus();
                txtQuantidade.selectAll();
                logger.info("Produto encontrado e campos preenchidos");
            } else {
                JOptionPane.showMessageDialog(this, "Produto não encontrado!", "Aviso", JOptionPane.WARNING_MESSAGE);
                txtCodigoBarras.requestFocus();
                txtCodigoBarras.selectAll();
                logger.info("Produto não encontrado, mensagem exibida");
            }
        } catch (Exception e) {
            logger.error("Erro ao pesquisar produto: " + e.getMessage(), e);
            JOptionPane.showMessageDialog(this, "Erro ao pesquisar produto!", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void adicionarItem() {
        String codigo = txtCodigoBarras.getText().trim();
        String descricao = txtDescricaoProduto.getText().trim();
        String qtdStr = txtQuantidade.getText().trim();
        String unitarioStr = txtValorUnitario.getText().trim();
        
        if (codigo.isEmpty() || descricao.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Informe o produto!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        try {
            int quantidade = Integer.parseInt(qtdStr);
            BigDecimal valorUnitario = new BigDecimal(unitarioStr.replace(",", "."));
            BigDecimal valorTotal = valorUnitario.multiply(new BigDecimal(quantidade));
            
            // Criar item
            ItemVenda item = new ItemVenda();
            item.setCodigo(codigo);
            item.setDescricao(descricao);
            item.setQuantidade(quantidade);
            item.setValorUnitario(valorUnitario.doubleValue());
            item.setValorTotal(valorTotal.doubleValue());
            
            // Adicionar à lista
            itensVenda.add(item);
            
            // Adicionar à tabela
            Object[] linha = {
                codigo, descricao, quantidade, 
                String.format("R$ %.2f", valorUnitario),
                String.format("R$ %.2f", valorTotal)
            };
            modeloTabela.addRow(linha);
            
            // Limpar campos
            txtCodigoBarras.setText("");
            txtDescricaoProduto.setText("");
            txtQuantidade.setText("1");
            txtValorUnitario.setText("0,00");
            txtValorTotal.setText("0,00");
            
            // Recalcular totais
            calcularTotais();
            
            txtCodigoBarras.requestFocus();
            
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Valores inválidos!", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void calcularValorTotalItem() {
        try {
            String qtdStr = txtQuantidade.getText().trim();
            String unitarioStr = txtValorUnitario.getText().trim();
            
            if (!qtdStr.isEmpty() && !unitarioStr.isEmpty()) {
                int quantidade = Integer.parseInt(qtdStr);
                BigDecimal valorUnitario = new BigDecimal(unitarioStr.replace(",", "."));
                BigDecimal valorTotal = valorUnitario.multiply(new BigDecimal(quantidade));
                
                txtValorTotal.setText(String.format("%.2f", valorTotal));
            }
        } catch (NumberFormatException e) {
            txtValorTotal.setText("0,00");
        }
    }
    
    private void calcularTotais() {
        subtotal = BigDecimal.ZERO;
        
        for (ItemVenda item : itensVenda) {
            subtotal = subtotal.add(BigDecimal.valueOf(item.getValorTotal()));
        }
        
        BigDecimal desconto = BigDecimal.ZERO;
        try {
            String descontoStr = txtDesconto.getText().trim().replace(",", ".");
            if (!descontoStr.isEmpty() && !descontoStr.equals("0.00")) {
                desconto = new BigDecimal(descontoStr);
                // Não permitir desconto maior que subtotal
                if (desconto.compareTo(subtotal) > 0) {
                    desconto = BigDecimal.ZERO;
                    txtDesconto.setText("0,00");
                    JOptionPane.showMessageDialog(this, "Desconto não pode ser maior que subtotal!", "Aviso", JOptionPane.WARNING_MESSAGE);
                }
            }
        } catch (NumberFormatException e) {
            desconto = BigDecimal.ZERO;
            txtDesconto.setText("0,00");
        }
        
        total = subtotal.subtract(desconto);
        
        // Formatação brasileira
        lblSubtotal.setText(String.format("Subtotal: R$ %,.2f", subtotal));
        lblDesconto.setText(String.format("Desconto: R$ %,.2f", desconto));
        lblTotal.setText(String.format("TOTAL: R$ %,.2f", total));
    }
    
    private void calcularTroco() {
        try {
            String recebidoStr = txtValorRecebido.getText().trim().replace(",", ".");
            if (!recebidoStr.isEmpty() && !recebidoStr.equals("0.00")) {
                BigDecimal valorRecebido = new BigDecimal(recebidoStr);
                BigDecimal troco = valorRecebido.subtract(total);
                
                if (troco.compareTo(BigDecimal.ZERO) >= 0) {
                    txtTroco.setText(String.format("%,.2f", troco));
                } else {
                    txtTroco.setText("0,00");
                }
            } else {
                txtTroco.setText("0,00");
            }
        } catch (NumberFormatException e) {
            txtTroco.setText("0,00");
        }
    }
    
    private void finalizarVenda() {
        if (itensVenda.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Adicione itens à venda!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        try {
            String formaPagamento = (String) comboFormaPagamento.getSelectedItem();
            BigDecimal valorRecebido = BigDecimal.ZERO;
            
            // Para dinheiro, validar valor recebido
            if ("Dinheiro".equals(formaPagamento)) {
                String recebidoStr = txtValorRecebido.getText().trim().replace(",", ".");
                if (recebidoStr.isEmpty() || recebidoStr.equals("0.00")) {
                    JOptionPane.showMessageDialog(this, "Informe o valor recebido!", "Aviso", JOptionPane.WARNING_MESSAGE);
                    txtValorRecebido.requestFocus();
                    return;
                }
                valorRecebido = new BigDecimal(recebidoStr);
                
                if (valorRecebido.compareTo(total) < 0) {
                    JOptionPane.showMessageDialog(this, "Valor recebido insuficiente!", "Aviso", JOptionPane.WARNING_MESSAGE);
                    txtValorRecebido.requestFocus();
                    return;
                }
            } else {
                // Para outras formas, valor recebido = total
                valorRecebido = total;
            }
            
            // Salvar venda
            vendaAtual.setValorTotal(total.doubleValue());
            vendaAtual.setValorFinal(total.doubleValue());
            vendaAtual.setFormaPagamento(formaPagamento);
            vendaDao.salvar(vendaAtual);
            
            // Salvar itens
            for (ItemVenda item : itensVenda) {
                item.setVendaId(vendaAtual.getId());
                itemVendaDao.salvar(item);
            }
            
            // Salvar pagamento
            Pagamento pagamento = new Pagamento();
            pagamento.setVendaId(vendaAtual.getId());
            pagamento.setValor(valorRecebido.doubleValue());
            pagamento.setValorRecebido(valorRecebido.doubleValue());
            // pagamento.setFormaPagamento(formaPagamento); // Campo string já definido acima
            
            // Calcular troco para dinheiro
            if ("Dinheiro".equals(formaPagamento)) {
                BigDecimal troco = valorRecebido.subtract(total);
                pagamento.setTroco(troco.doubleValue());
            }
            
            pagamentoDao.salvar(pagamento);
            
            JOptionPane.showMessageDialog(this, "Venda finalizada com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            
            // Emitir cupom automaticamente
            emitirCupomFiscal();
            
            // Iniciar nova venda
            iniciarNovaVenda();
            
        } catch (Exception e) {
            logger.error("Erro ao finalizar venda: " + e.getMessage(), e);
            JOptionPane.showMessageDialog(this, "Erro ao finalizar venda: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void cancelarVenda() {
        int confirmacao = JOptionPane.showConfirmDialog(this, 
            "Deseja cancelar esta venda?", 
            "Confirmar Cancelamento", 
            JOptionPane.YES_NO_OPTION);
            
        if (confirmacao == JOptionPane.YES_OPTION) {
            iniciarNovaVenda();
        }
    }
    
    private void emitirCupomFiscal() {
        if (vendaAtual == null || vendaAtual.getId() == null) {
            JOptionPane.showMessageDialog(this, "Finalize uma venda antes de emitir o cupom!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        try {
            String cupom = EmissorCupomFiscal.gerarCupomFiscal(vendaAtual, itensVenda, List.of());
            
            // Exibir cupom em dialog
            JTextArea textArea = new JTextArea(cupom);
            textArea.setEditable(false);
            textArea.setFont(new Font("Monospaced", Font.PLAIN, 10));
            JScrollPane scrollPane = new JScrollPane(textArea);
            scrollPane.setPreferredSize(new Dimension(400, 600));
            
            JOptionPane.showMessageDialog(this, scrollPane, "Cupom Fiscal", JOptionPane.INFORMATION_MESSAGE);
            logger.info("Cupom fiscal gerado com sucesso para venda #" + vendaAtual.getId());
        } catch (Exception e) {
            logger.error("Erro ao emitir cupom: " + e.getMessage(), e);
            JOptionPane.showMessageDialog(this, "Erro ao emitir cupom: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void atualizarDataHora() {
        lblDataHora.setText(LocalDateTime.now().format(
            java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")
        ));
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
