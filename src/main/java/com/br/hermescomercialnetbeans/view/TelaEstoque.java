package com.br.hermescomercialnetbeans.view;

import com.br.hermescomercialnetbeans.dao.ProdutoDao;
import com.br.hermescomercialnetbeans.model.Produto;
import org.apache.logging.log4j.LogManager;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.io.font.constants.StandardFonts;

/**
 * Tela elegante para Controle de Estoque
 * @author marcos
 */
public class TelaEstoque extends JInternalFrame {
    
    private static final org.apache.logging.log4j.Logger logger = LogManager.getLogger(TelaEstoque.class);
    
    // Componentes da interface
    private JPanel panelPrincipal;
    private JPanel panelFiltros;
    private JPanel panelEstatisticas;
    private JPanel panelBotoes;
    private JScrollPane scrollPaneTabela;
    
    // Filtros e pesquisa
    private JTextField txtPesquisa;
    private JComboBox<String> comboCategoria;
    private JComboBox<String> comboStatus;
    private JCheckBox chkEstoqueBaixo;
    private JButton btPesquisar;
    private JButton btLimpar;
    
    // Estatísticas
    private JLabel lblTotalProdutos;
    private JLabel lblValorTotalEstoque;
    private JLabel lblProdutosEstoqueBaixo;
    private JLabel lblProdutosSemEstoque;
    
    // Tabela
    private JTable tabelaEstoque;
    private DefaultTableModel modeloTabela;
    
    // Botões de ação
    private JButton btEntradaEstoque;
    private JButton btSaidaEstoque;
    private JButton btAjustarEstoque;
    private JButton btRelatorio;
    private JButton btAtualizarPrecos;
    
    // DAO
    private ProdutoDao produtoDao;
    
    // Cores do tema
    private static final Color COR_PRIMARIA = new Color(41, 128, 185);
    private static final Color COR_SUCESSO = new Color(39, 174, 96);
    private static final Color COR_PERIGO = new Color(231, 76, 60);
    private static final Color COR_ADVERTENCIA = new Color(243, 156, 18);
    private static final Color COR_FUNDO = new Color(236, 240, 241);
    private static final Color COR_TEXTO = new Color(44, 62, 80);
    
    public TelaEstoque() {
        super("Controle de Estoque");
        this.produtoDao = new ProdutoDao();
        
        inicializarComponentes();
        configurarLayout();
        configurarEventos();
        carregarEstoque();
    }
    
    private void inicializarComponentes() {
        // Painéis principais
        panelPrincipal = new JPanel(new BorderLayout());
        panelFiltros = new JPanel(new GridBagLayout());
        panelEstatisticas = new JPanel(new GridLayout(2, 2, 10, 5));
        panelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        
        // Filtros
        txtPesquisa = criarCampoTexto("Pesquisar produto...", 25);
        comboCategoria = new JComboBox<>(new String[]{
            "Todas", "Alimentos", "Bebidas", "Limpeza", "Eletrônicos", "Vestuário", "Outros"
        });
        comboCategoria.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        
        comboStatus = new JComboBox<>(new String[]{
            "Todos", "Ativos", "Inativos"
        });
        comboStatus.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        
        chkEstoqueBaixo = new JCheckBox("Estoque Baixo (< 10)");
        chkEstoqueBaixo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        chkEstoqueBaixo.setOpaque(false);
        
        btPesquisar = criarBotao("Pesquisar", COR_PRIMARIA);
        btLimpar = criarBotao("Limpar", Color.GRAY);
        
        // Estatísticas
        lblTotalProdutos = new JLabel("Total Produtos: 0");
        lblTotalProdutos.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblTotalProdutos.setForeground(COR_PRIMARIA);
        
        lblValorTotalEstoque = new JLabel("Valor Total: R$ 0,00");
        lblValorTotalEstoque.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblValorTotalEstoque.setForeground(COR_SUCESSO);
        
        lblProdutosEstoqueBaixo = new JLabel("Estoque Baixo: 0");
        lblProdutosEstoqueBaixo.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblProdutosEstoqueBaixo.setForeground(COR_ADVERTENCIA);
        
        lblProdutosSemEstoque = new JLabel("Sem Estoque: 0");
        lblProdutosSemEstoque.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblProdutosSemEstoque.setForeground(COR_PERIGO);
        
        // Botões de ação
        btEntradaEstoque = criarBotao("Entrada Estoque", COR_SUCESSO);
        btSaidaEstoque = criarBotao("Saída Estoque", COR_PERIGO);
        btAjustarEstoque = criarBotao("Ajustar Estoque", COR_ADVERTENCIA);
        btRelatorio = criarBotao("Relatório", COR_PRIMARIA);
        btAtualizarPrecos = criarBotao("Atualizar Preços", COR_PRIMARIA);
        
        // Tabela
        modeloTabela = new DefaultTableModel();
        modeloTabela.addColumn("Código");
        modeloTabela.addColumn("Nome");
        modeloTabela.addColumn("Categoria");
        modeloTabela.addColumn("Estoque");
        modeloTabela.addColumn("Estoque Mín");
        modeloTabela.addColumn("Preço Compra");
        modeloTabela.addColumn("Preço Venda");
        modeloTabela.addColumn("Valor Total");
        modeloTabela.addColumn("Status");
        
        tabelaEstoque = new JTable(modeloTabela);
        tabelaEstoque.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        tabelaEstoque.setRowHeight(25);
        tabelaEstoque.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        tabelaEstoque.getTableHeader().setBackground(COR_PRIMARIA);
        tabelaEstoque.getTableHeader().setForeground(Color.WHITE);
        
        scrollPaneTabela = new JScrollPane(tabelaEstoque);
        scrollPaneTabela.setPreferredSize(new Dimension(900, 400));
        scrollPaneTabela.setBorder(BorderFactory.createLineBorder(COR_PRIMARIA, 1));
    }
    
    private void configurarLayout() {
        // Configurar painel de filtros
        panelFiltros.setBorder(BorderFactory.createTitledBorder("Filtros e Pesquisa"));
        panelFiltros.setBackground(Color.WHITE);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        gbc.gridx = 0; gbc.gridy = 0;
        panelFiltros.add(new JLabel("Pesquisa:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 2;
        panelFiltros.add(txtPesquisa, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1;
        gbc.gridwidth = 1;
        panelFiltros.add(new JLabel("Categoria:"), gbc);
        gbc.gridx = 1;
        panelFiltros.add(comboCategoria, gbc);
        
        gbc.gridx = 2;
        panelFiltros.add(new JLabel("Status:"), gbc);
        gbc.gridx = 3;
        panelFiltros.add(comboStatus, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2;
        gbc.gridwidth = 2;
        panelFiltros.add(chkEstoqueBaixo, gbc);
        
        gbc.gridx = 2; gbc.gridy = 2;
        panelFiltros.add(btPesquisar, gbc);
        gbc.gridx = 3;
        panelFiltros.add(btLimpar, gbc);
        
        // Configurar painel de estatísticas
        panelEstatisticas.setBorder(BorderFactory.createTitledBorder("Estatísticas do Estoque"));
        panelEstatisticas.setBackground(Color.WHITE);
        panelEstatisticas.add(lblTotalProdutos);
        panelEstatisticas.add(lblValorTotalEstoque);
        panelEstatisticas.add(lblProdutosEstoqueBaixo);
        panelEstatisticas.add(lblProdutosSemEstoque);
        
        // Configurar painel de botões
        panelBotoes.setBorder(BorderFactory.createTitledBorder("Operações"));
        panelBotoes.setBackground(Color.WHITE);
        panelBotoes.add(btEntradaEstoque);
        panelBotoes.add(btSaidaEstoque);
        panelBotoes.add(btAjustarEstoque);
        panelBotoes.add(btRelatorio);
        panelBotoes.add(btAtualizarPrecos);
        
        // Montar painel superior
        JPanel panelTopo = new JPanel(new BorderLayout());
        panelTopo.add(panelFiltros, BorderLayout.CENTER);
        panelTopo.add(panelEstatisticas, BorderLayout.SOUTH);
        
        // Montar painel principal
        JPanel panelCentro = new JPanel(new BorderLayout());
        panelCentro.add(panelTopo, BorderLayout.NORTH);
        panelCentro.add(scrollPaneTabela, BorderLayout.CENTER);
        panelCentro.add(panelBotoes, BorderLayout.SOUTH);
        
        panelPrincipal.add(panelCentro, BorderLayout.CENTER);
        
        // Configurar o frame
        this.setContentPane(panelPrincipal);
        this.setSize(1000, 700);
        this.setClosable(true);
        this.setMaximizable(true);
        this.setIconifiable(true);
        this.setResizable(true);
    }
    
    private void configurarEventos() {
        // Eventos dos botões
        btPesquisar.addActionListener(e -> pesquisarProdutos());
        btLimpar.addActionListener(e -> limparFiltros());
        btEntradaEstoque.addActionListener(e -> entradaEstoque());
        btSaidaEstoque.addActionListener(e -> saidaEstoque());
        btAjustarEstoque.addActionListener(e -> ajustarEstoque());
        btRelatorio.addActionListener(e -> gerarRelatorio());
        btAtualizarPrecos.addActionListener(e -> atualizarPrecos());
        
        // Evento da tabela
        tabelaEstoque.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    editarProduto();
                }
            }
        });
        
        // Evento de pesquisa em tempo real
        txtPesquisa.addActionListener(e -> pesquisarProdutos());
    }
    
    private void carregarEstoque() {
        try {
            List<Produto> produtos = produtoDao.listar();
            atualizarTabela(produtos);
            atualizarEstatisticas(produtos);
        } catch (Exception e) {
            logger.error("Erro ao carregar estoque: " + e.getMessage(), e);
            JOptionPane.showMessageDialog(this, "Erro ao carregar estoque!", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void pesquisarProdutos() {
        try {
            List<Produto> produtos = produtoDao.listar();
            String textoPesquisa = txtPesquisa.getText().trim().toLowerCase();
            String categoria = (String) comboCategoria.getSelectedItem();
            String status = (String) comboStatus.getSelectedItem();
            boolean estoqueBaixo = chkEstoqueBaixo.isSelected();
            
            // Filtrar produtos
            produtos = produtos.stream()
                .filter(p -> {
                    // Filtro por texto
                    if (!textoPesquisa.isEmpty()) {
                        boolean matchNome = p.getNome().toLowerCase().contains(textoPesquisa);
                        boolean matchCodigo = p.getCodigo().toLowerCase().contains(textoPesquisa);
                        if (!matchNome && !matchCodigo) return false;
                    }
                    
                    // Filtro por categoria
                    if (!categoria.equals("Todas")) {
                        if (p.getCategoria() == null || !p.getCategoria().equalsIgnoreCase(categoria)) {
                            return false;
                        }
                    }
                    
                    // Filtro por status
                    if (!status.equals("Todos")) {
                        boolean ativo = p.getAtivo() != null ? p.getAtivo() : true;
                        if (status.equals("Ativos") && !ativo) return false;
                        if (status.equals("Inativos") && ativo) return false;
                    }
                    
                    // Filtro por estoque baixo
                    if (estoqueBaixo) {
                        int estoqueMin = p.getEstoqueMinimo() != null ? p.getEstoqueMinimo() : 10;
                        int estoqueAtual = p.getEstoque() != null ? p.getEstoque() : 0;
                        if (estoqueAtual >= estoqueMin) return false;
                    }
                    
                    return true;
                })
                .toList();
            
            atualizarTabela(produtos);
            atualizarEstatisticas(produtos);
            
        } catch (Exception e) {
            logger.error("Erro ao pesquisar produtos: " + e.getMessage(), e);
            JOptionPane.showMessageDialog(this, "Erro ao pesquisar produtos!", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void limparFiltros() {
        txtPesquisa.setText("");
        comboCategoria.setSelectedIndex(0);
        comboStatus.setSelectedIndex(0);
        chkEstoqueBaixo.setSelected(false);
        carregarEstoque();
    }
    
    private void entradaEstoque() {
        int linhaSelecionada = tabelaEstoque.getSelectedRow();
        if (linhaSelecionada == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um produto para dar entrada no estoque!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        try {
            String codigo = (String) modeloTabela.getValueAt(linhaSelecionada, 0);
            Produto produto = produtoDao.buscarPorCodigo(codigo);
            
            if (produto != null) {
                String quantidadeStr = JOptionPane.showInputDialog(
                    this, 
                    "Informe a quantidade de entrada:", 
                    "Entrada no Estoque", 
                    JOptionPane.QUESTION_MESSAGE
                );
                
                if (quantidadeStr != null && !quantidadeStr.trim().isEmpty()) {
                    try {
                        int quantidade = Integer.parseInt(quantidadeStr.trim());
                        if (quantidade > 0) {
                            int estoqueAtual = produto.getEstoque() != null ? produto.getEstoque() : 0;
                            produto.setEstoque(estoqueAtual + quantidade);
                            produtoDao.atualizar(produto);
                            
                            JOptionPane.showMessageDialog(this, 
                                "Entrada no estoque realizada com sucesso!", 
                                "Sucesso", 
                                JOptionPane.INFORMATION_MESSAGE);
                            
                            carregarEstoque();
                        } else {
                            JOptionPane.showMessageDialog(this, 
                                "Quantidade deve ser positiva!", 
                                "Aviso", 
                                JOptionPane.WARNING_MESSAGE);
                        }
                    } catch (NumberFormatException e) {
                        JOptionPane.showMessageDialog(this, 
                            "Quantidade inválida!", 
                            "Erro", 
                            JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
            
        } catch (Exception e) {
            logger.error("Erro ao dar entrada no estoque: " + e.getMessage(), e);
            JOptionPane.showMessageDialog(this, "Erro ao dar entrada no estoque!", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void saidaEstoque() {
        int linhaSelecionada = tabelaEstoque.getSelectedRow();
        if (linhaSelecionada == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um produto para dar saída no estoque!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        try {
            String codigo = (String) modeloTabela.getValueAt(linhaSelecionada, 0);
            Produto produto = produtoDao.buscarPorCodigo(codigo);
            
            if (produto != null) {
                String quantidadeStr = JOptionPane.showInputDialog(
                    this, 
                    "Informe a quantidade de saída:", 
                    "Saída do Estoque", 
                    JOptionPane.QUESTION_MESSAGE
                );
                
                if (quantidadeStr != null && !quantidadeStr.trim().isEmpty()) {
                    try {
                        int quantidade = Integer.parseInt(quantidadeStr.trim());
                        int estoqueAtual = produto.getEstoque() != null ? produto.getEstoque() : 0;
                        
                        if (quantidade > 0) {
                            if (quantidade <= estoqueAtual) {
                                produto.setEstoque(estoqueAtual - quantidade);
                                produtoDao.atualizar(produto);
                                
                                JOptionPane.showMessageDialog(this, 
                                    "Saída do estoque realizada com sucesso!", 
                                    "Sucesso", 
                                    JOptionPane.INFORMATION_MESSAGE);
                                
                                carregarEstoque();
                            } else {
                                JOptionPane.showMessageDialog(this, 
                                    "Quantidade maior que o estoque disponível!", 
                                    "Aviso", 
                                    JOptionPane.WARNING_MESSAGE);
                            }
                        } else {
                            JOptionPane.showMessageDialog(this, 
                                "Quantidade deve ser positiva!", 
                                "Aviso", 
                                JOptionPane.WARNING_MESSAGE);
                        }
                    } catch (NumberFormatException e) {
                        JOptionPane.showMessageDialog(this, 
                            "Quantidade inválida!", 
                            "Erro", 
                            JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
            
        } catch (Exception e) {
            logger.error("Erro ao dar saída no estoque: " + e.getMessage(), e);
            JOptionPane.showMessageDialog(this, "Erro ao dar saída no estoque!", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void ajustarEstoque() {
        int linhaSelecionada = tabelaEstoque.getSelectedRow();
        if (linhaSelecionada == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um produto para ajustar o estoque!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        try {
            String codigo = (String) modeloTabela.getValueAt(linhaSelecionada, 0);
            Produto produto = produtoDao.buscarPorCodigo(codigo);
            
            if (produto != null) {
                String quantidadeStr = JOptionPane.showInputDialog(
                    this, 
                    "Informe a quantidade correta (ajuste):", 
                    "Ajuste de Estoque", 
                    JOptionPane.QUESTION_MESSAGE
                );
                
                if (quantidadeStr != null && !quantidadeStr.trim().isEmpty()) {
                    try {
                        int quantidade = Integer.parseInt(quantidadeStr.trim());
                        if (quantidade >= 0) {
                            produto.setEstoque(quantidade);
                            produtoDao.atualizar(produto);
                            
                            JOptionPane.showMessageDialog(this, 
                                "Estoque ajustado com sucesso!", 
                                "Sucesso", 
                                JOptionPane.INFORMATION_MESSAGE);
                            
                            carregarEstoque();
                        } else {
                            JOptionPane.showMessageDialog(this, 
                                "Quantidade não pode ser negativa!", 
                                "Aviso", 
                                JOptionPane.WARNING_MESSAGE);
                        }
                    } catch (NumberFormatException e) {
                        JOptionPane.showMessageDialog(this, 
                            "Quantidade inválida!", 
                            "Erro", 
                            JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
            
        } catch (Exception e) {
            logger.error("Erro ao ajustar estoque: " + e.getMessage(), e);
            JOptionPane.showMessageDialog(this, "Erro ao ajustar estoque!", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void editarProduto() {
        int linhaSelecionada = tabelaEstoque.getSelectedRow();
        if (linhaSelecionada == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um produto para editar!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        try {
            String codigo = (String) modeloTabela.getValueAt(linhaSelecionada, 0);
            Produto produto = produtoDao.buscarPorCodigo(codigo);
            
            if (produto != null) {
                // Abrir tela de edição de produto
                TelaProduto telaProduto = new TelaProduto();
                telaProduto.setVisible(true);
                
                // Aqui seria ideal passar o produto para edição
                // mas a TelaProduto não suporta edição direta ainda
            }
            
        } catch (Exception e) {
            logger.error("Erro ao editar produto: " + e.getMessage(), e);
            JOptionPane.showMessageDialog(this, "Erro ao editar produto!", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void gerarRelatorio() {
        try {
            List<Produto> produtos = produtoDao.listar();
            
            // Criar diretório de relatórios se não existir
            String diretorioRelatorios = "C:\\RelatorioSistema";
            File diretorio = new File(diretorioRelatorios);
            if (!diretorio.exists()) {
                diretorio.mkdirs();
                logger.info("Diretório de relatórios criado: " + diretorioRelatorios);
            }
            
            // Nome do arquivo PDF
            String nomeArquivo = "relatorio_estoque_" + 
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + ".pdf";
            String caminhoCompleto = diretorioRelatorios + "\\" + nomeArquivo;
            
            // Criar documento PDF
            PdfWriter writer = new PdfWriter(new FileOutputStream(caminhoCompleto));
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);
            
            // Fontes
            PdfFont fontBold = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD);
            PdfFont fontNormal = PdfFontFactory.createFont(StandardFonts.HELVETICA);
            
            // Título
            Paragraph titulo = new Paragraph("RELATÓRIO DE ESTOQUE")
                .setFont(fontBold)
                .setFontSize(18)
                .setTextAlignment(TextAlignment.CENTER)
                .setMarginBottom(20);
            document.add(titulo);
            
            // Data e hora
            Paragraph dataHora = new Paragraph("Data: " + 
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")))
                .setFont(fontNormal)
                .setFontSize(12)
                .setTextAlignment(TextAlignment.CENTER)
                .setMarginBottom(20);
            document.add(dataHora);
            
            // Tabela de produtos
            Table tabela = new Table(UnitValue.createPercentArray(new float[]{15, 40, 15, 15, 15}))
                .useAllAvailableWidth();
            
            // Cabeçalho da tabela
            tabela.addHeaderCell(new Cell().add(new Paragraph("Código").setFont(fontBold)));
            tabela.addHeaderCell(new Cell().add(new Paragraph("Produto").setFont(fontBold)));
            tabela.addHeaderCell(new Cell().add(new Paragraph("Estoque").setFont(fontBold)));
            tabela.addHeaderCell(new Cell().add(new Paragraph("Preço").setFont(fontBold)));
            tabela.addHeaderCell(new Cell().add(new Paragraph("Valor Total").setFont(fontBold)));
            
            // Dados dos produtos
            BigDecimal valorTotal = BigDecimal.ZERO;
            int estoqueBaixo = 0;
            int semEstoque = 0;
            
            for (Produto produto : produtos) {
                int estoque = produto.getEstoque() != null ? produto.getEstoque() : 0;
                BigDecimal preco = produto.getPreco() != null ? produto.getPreco() : BigDecimal.ZERO;
                BigDecimal valorTotalProduto = preco.multiply(new BigDecimal(estoque));
                
                tabela.addCell(new Cell().add(new Paragraph(produto.getCodigo() != null ? produto.getCodigo() : "").setFont(fontNormal)));
                tabela.addCell(new Cell().add(new Paragraph(produto.getNome() != null ? produto.getNome() : "").setFont(fontNormal)));
                tabela.addCell(new Cell().add(new Paragraph(String.valueOf(estoque)).setFont(fontNormal)));
                tabela.addCell(new Cell().add(new Paragraph(String.format("R$ %.2f", preco)).setFont(fontNormal)));
                tabela.addCell(new Cell().add(new Paragraph(String.format("R$ %.2f", valorTotalProduto)).setFont(fontNormal)));
                
                valorTotal = valorTotal.add(valorTotalProduto);
                
                int estoqueMin = produto.getEstoqueMinimo() != null ? produto.getEstoqueMinimo() : 10;
                if (estoque < estoqueMin) estoqueBaixo++;
                if (estoque == 0) semEstoque++;
            }
            
            document.add(tabela);
            
            // Resumo
            document.add(new Paragraph("\n"));
            Paragraph resumoTitulo = new Paragraph("RESUMO:")
                .setFont(fontBold)
                .setFontSize(14)
                .setMarginBottom(10);
            document.add(resumoTitulo);
            
            Paragraph resumo = new Paragraph()
                .setFont(fontNormal)
                .setFontSize(12)
                .add(String.format("Total de Produtos: %d\n", produtos.size()))
                .add(String.format("Valor Total do Estoque: R$ %.2f\n", valorTotal))
                .add(String.format("Produtos com Estoque Baixo: %d\n", estoqueBaixo))
                .add(String.format("Produtos sem Estoque: %d\n", semEstoque));
            document.add(resumo);
            
            // Fechar documento
            document.close();
            
            JOptionPane.showMessageDialog(this, 
                "Relatório PDF gerado com sucesso!\nArquivo: " + caminhoCompleto, 
                "Sucesso", 
                JOptionPane.INFORMATION_MESSAGE);
            
        } catch (Exception e) {
            logger.error("Erro ao gerar relatório PDF: " + e.getMessage(), e);
            JOptionPane.showMessageDialog(this, "Erro ao gerar relatório PDF: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void atualizarPrecos() {
        int confirmacao = JOptionPane.showConfirmDialog(this, 
            "Deseja atualizar os preços de todos os produtos?\n" +
            "Isso pode levar algum tempo dependendo da quantidade de produtos.", 
            "Atualizar Preços", 
            JOptionPane.YES_NO_OPTION);
            
        if (confirmacao == JOptionPane.YES_OPTION) {
            try {
                List<Produto> produtos = produtoDao.listar();
                
                for (Produto produto : produtos) {
                    // Simulação de atualização de preços
                    // Em um sistema real, poderia buscar de uma API ou arquivo
                    BigDecimal precoAtual = produto.getPreco();
                    if (precoAtual != null && precoAtual.compareTo(BigDecimal.ZERO) > 0) {
                        // Aplicar um reajuste de 5% por exemplo
                        BigDecimal novoPreco = precoAtual.multiply(new BigDecimal("1.05"));
                        produto.setPreco(novoPreco);
                        produtoDao.atualizar(produto);
                    }
                }
                
                JOptionPane.showMessageDialog(this, 
                    String.format("Preços atualizados para %d produtos!", produtos.size()), 
                    "Sucesso", 
                    JOptionPane.INFORMATION_MESSAGE);
                
                carregarEstoque();
                
            } catch (Exception e) {
                logger.error("Erro ao atualizar preços: " + e.getMessage(), e);
                JOptionPane.showMessageDialog(this, "Erro ao atualizar preços!", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void atualizarTabela(List<Produto> produtos) {
        modeloTabela.setRowCount(0);
        
        for (Produto produto : produtos) {
            int estoque = produto.getEstoque() != null ? produto.getEstoque() : 0;
            int estoqueMin = produto.getEstoqueMinimo() != null ? produto.getEstoqueMinimo() : 10;
            BigDecimal precoCompra = produto.getPrecoCompra() != null ? produto.getPrecoCompra() : BigDecimal.ZERO;
            BigDecimal precoVenda = produto.getPreco() != null ? produto.getPreco() : BigDecimal.ZERO;
            BigDecimal valorTotal = precoVenda.multiply(new BigDecimal(estoque));
            
            Object[] linha = {
                produto.getCodigo(),
                produto.getNome(),
                produto.getCategoria() != null ? produto.getCategoria() : "-",
                estoque,
                estoqueMin,
                String.format("R$ %.2f", precoCompra),
                String.format("R$ %.2f", precoVenda),
                String.format("R$ %.2f", valorTotal),
                produto.getAtivo() != null ? (produto.getAtivo() ? "Ativo" : "Inativo") : "Ativo"
            };
            modeloTabela.addRow(linha);
        }
    }
    
    private void atualizarEstatisticas(List<Produto> produtos) {
        int totalProdutos = produtos.size();
        BigDecimal valorTotal = BigDecimal.ZERO;
        int estoqueBaixo = 0;
        int semEstoque = 0;
        
        for (Produto produto : produtos) {
            int estoque = produto.getEstoque() != null ? produto.getEstoque() : 0;
            BigDecimal preco = produto.getPreco() != null ? produto.getPreco() : BigDecimal.ZERO;
            
            valorTotal = valorTotal.add(preco.multiply(new BigDecimal(estoque)));
            
            int estoqueMin = produto.getEstoqueMinimo() != null ? produto.getEstoqueMinimo() : 10;
            if (estoque < estoqueMin) estoqueBaixo++;
            if (estoque == 0) semEstoque++;
        }
        
        lblTotalProdutos.setText("Total Produtos: " + totalProdutos);
        lblValorTotalEstoque.setText("Valor Total: R$ " + String.format("%.2f", valorTotal));
        lblProdutosEstoqueBaixo.setText("Estoque Baixo: " + estoqueBaixo);
        lblProdutosSemEstoque.setText("Sem Estoque: " + semEstoque);
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
