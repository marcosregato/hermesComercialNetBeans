package com.br.hermescomercialnetbeans.view;

import com.br.hermescomercialnetbeans.dao.ProdutoDao;
import com.br.hermescomercialnetbeans.connectionDB.PostgreSQLConnection;
import com.br.hermescomercialnetbeans.model.Produto;
import org.apache.logging.log4j.LogManager;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

/**
 * Tela elegante para gerenciamento de Produtos
 * @author marcos
 */
public class TelaProduto extends JInternalFrame {
    
    private static final org.apache.logging.log4j.Logger logger = LogManager.getLogger(TelaProduto.class);
    
    // Componentes da interface
    private JPanel panelPrincipal;
    private JPanel panelFormulario;
    private JPanel panelBotoes;
    private JPanel panelPesquisa;
    private JScrollPane scrollPaneTabela;
    private JTable tabelaProdutos;
    
    // Campos do formulário
    private JTextField txtNome;
    private JTextField txtCodigo;
    private JTextField txtDescricao;
    private JTextField txtPrecoCompra;
    private JTextField txtPrecoVenda;
    private JTextField txtEstoque;
    private JTextField txtEstoqueMinimo;
    private JTextField txtFornecedor;
    private JCheckBox chkAtivo;
    
    // Botões
    private JButton btNovo;
    private JButton btSalvar;
    private JButton btEditar;
    private JButton btExcluir;
    private JButton btCancelar;
    private JButton btLimpar;
    private JButton btPesquisar;
    
    // Pesquisa
    private JTextField txtPesquisa;
    private JComboBox<String> comboFiltro;
    
    // DAO e modelo
    private ProdutoDao produtoDao;
    private DefaultTableModel modeloTabela;
    private Produto produtoSelecionado;
    
    // Cores do tema
    private static final Color COR_PRIMARIA = new Color(41, 128, 185);
    private static final Color COR_SECUNDARIA = new Color(52, 73, 94);
    private static final Color COR_SUCESSO = new Color(39, 174, 96);
    private static final Color COR_PERIGO = new Color(231, 76, 60);
    
    public TelaProduto() {
        super("Cadastro de Produtos");
        this.produtoDao = new ProdutoDao();
        inicializarComponentes();
        configurarLayout();
        configurarEventos();
        carregarProdutos();
    }
    
    private void inicializarComponentes() {
        // Painéis principais
        panelPrincipal = new JPanel(new BorderLayout());
        panelFormulario = new JPanel(new GridBagLayout());
        panelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        panelPesquisa = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        
        // Campos do formulário
        txtNome = new JTextField(30);
        txtNome.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        txtCodigo = new JTextField(15);
        txtCodigo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        txtDescricao = new JTextField(40);
        txtDescricao.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        txtPrecoCompra = new JTextField(15);
        txtPrecoCompra.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        txtPrecoVenda = new JTextField(15);
        txtPrecoVenda.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        txtEstoque = new JTextField(10);
        txtEstoque.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        txtEstoqueMinimo = new JTextField(10);
        txtEstoqueMinimo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        txtFornecedor = new JTextField(20);
        txtFornecedor.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        
        chkAtivo = new JCheckBox("Ativo");
        chkAtivo.setSelected(true);
        chkAtivo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        chkAtivo.setOpaque(false);
        
        // Botões
        btNovo = criarBotao("Novo", COR_SUCESSO);
        btSalvar = criarBotao("Salvar", COR_PRIMARIA);
        btEditar = criarBotao("Editar", COR_SECUNDARIA);
        btExcluir = criarBotao("Excluir", COR_PERIGO);
        btCancelar = criarBotao("Cancelar", Color.GRAY);
        btLimpar = criarBotao("Limpar", Color.ORANGE);
        btPesquisar = criarBotao("Pesquisar", COR_PRIMARIA);
        
        // Pesquisa
        txtPesquisa = new JTextField(20);
        txtPesquisa.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        comboFiltro = new JComboBox<>(new String[]{"Todos", "Nome", "Código", "Descrição"});
        comboFiltro.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        
        // Tabela
        modeloTabela = new DefaultTableModel();
        modeloTabela.addColumn("ID");
        modeloTabela.addColumn("Código");
        modeloTabela.addColumn("Nome");
        modeloTabela.addColumn("Descrição");
        modeloTabela.addColumn("Preço Venda");
        modeloTabela.addColumn("Estoque");
        modeloTabela.addColumn("Status");
        
        tabelaProdutos = new JTable(modeloTabela);
        tabelaProdutos.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        tabelaProdutos.setRowHeight(25);
        tabelaProdutos.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        tabelaProdutos.getTableHeader().setBackground(COR_PRIMARIA);
        tabelaProdutos.getTableHeader().setForeground(Color.WHITE);
        
        scrollPaneTabela = new JScrollPane(tabelaProdutos);
        scrollPaneTabela.setBorder(BorderFactory.createLineBorder(COR_PRIMARIA, 1));
    }
    
    private void configurarLayout() {
        // Configurar painel de pesquisa
        panelPesquisa.add(new JLabel("Pesquisar:"));
        panelPesquisa.add(txtPesquisa);
        panelPesquisa.add(new JLabel("Filtrar por:"));
        panelPesquisa.add(comboFiltro);
        panelPesquisa.add(btPesquisar);
        panelPesquisa.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panelPesquisa.setBackground(Color.WHITE);
        
        // Configurar painel do formulário
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Linha 1: ID e Nome
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.weightx = 0;
        panelFormulario.add(new JLabel("Nome:"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        panelFormulario.add(txtNome, gbc);
        
        // Linha 2: Código e Descrição
        gbc.gridx = 0; gbc.gridy = 1;
        gbc.weightx = 0;
        panelFormulario.add(new JLabel("Código:"), gbc);
        gbc.gridx = 1;
        panelFormulario.add(txtCodigo, gbc);
        gbc.gridx = 2;
        panelFormulario.add(new JLabel("Descrição:"), gbc);
        gbc.gridx = 3;
        gbc.weightx = 1.0;
        gbc.gridwidth = 2;
        panelFormulario.add(txtDescricao, gbc);
        
        // Linha 3: Preços
        gbc.gridx = 0; gbc.gridy = 2;
        gbc.weightx = 0;
        gbc.gridwidth = 1;
        panelFormulario.add(new JLabel("Preço Compra:"), gbc);
        gbc.gridx = 1;
        panelFormulario.add(txtPrecoCompra, gbc);
        gbc.gridx = 2;
        panelFormulario.add(new JLabel("Preço Venda:"), gbc);
        gbc.gridx = 3;
        panelFormulario.add(txtPrecoVenda, gbc);
        
        // Linha 4: Estoque
        gbc.gridx = 0; gbc.gridy = 3;
        panelFormulario.add(new JLabel("Estoque:"), gbc);
        gbc.gridx = 1;
        panelFormulario.add(txtEstoque, gbc);
        gbc.gridx = 2;
        panelFormulario.add(new JLabel("Estoque Mínimo:"), gbc);
        gbc.gridx = 3;
        panelFormulario.add(txtEstoqueMinimo, gbc);
        
        // Linha 5: Fornecedor e Ativo
        gbc.gridx = 0; gbc.gridy = 4;
        panelFormulario.add(new JLabel("Fornecedor:"), gbc);
        gbc.gridx = 1;
        gbc.gridwidth = 2;
        panelFormulario.add(txtFornecedor, gbc);
        gbc.gridx = 3;
        gbc.gridwidth = 1;
        panelFormulario.add(chkAtivo, gbc);
        
        // Configurar painel de botões
        panelBotoes.add(btNovo);
        panelBotoes.add(btSalvar);
        panelBotoes.add(btEditar);
        panelBotoes.add(btExcluir);
        panelBotoes.add(btCancelar);
        panelBotoes.add(btLimpar);
        panelBotoes.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        panelBotoes.setBackground(Color.WHITE);
        
        // Painel do formulário completo
        JPanel panelFormCompleto = new JPanel(new BorderLayout());
        panelFormCompleto.add(panelFormulario, BorderLayout.CENTER);
        panelFormCompleto.add(panelBotoes, BorderLayout.SOUTH);
        panelFormCompleto.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(COR_PRIMARIA, 1),
            "Dados do Produto",
            0, 0, new Font("Segoe UI", Font.BOLD, 12), COR_PRIMARIA
        ));
        panelFormCompleto.setBackground(Color.WHITE);
        
        // Dividir a tela
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        splitPane.setTopComponent(panelFormCompleto);
        splitPane.setBottomComponent(scrollPaneTabela);
        splitPane.setDividerLocation(250);
        
        // Adicionar tudo ao painel principal
        panelPrincipal.add(panelPesquisa, BorderLayout.NORTH);
        panelPrincipal.add(splitPane, BorderLayout.CENTER);
        
        // Configurar o frame
        this.setContentPane(panelPrincipal);
        this.setSize(900, 600);
        this.setClosable(true);
        this.setMaximizable(true);
        this.setIconifiable(true);
        this.setResizable(true);
    }
    
    private void configurarEventos() {
        // Eventos dos botões
        btNovo.addActionListener(e -> novoProduto());
        btSalvar.addActionListener(e -> salvarProduto());
        btEditar.addActionListener(e -> editarProduto());
        btExcluir.addActionListener(e -> excluirProduto());
        btCancelar.addActionListener(e -> cancelarEdicao());
        btLimpar.addActionListener(e -> limparFormulario());
        btPesquisar.addActionListener(e -> pesquisarProdutos());
        
        // Evento da tabela
        tabelaProdutos.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    selecionarProdutoDaTabela();
                }
            }
        });
        
        // Evento de pesquisa em tempo real
        txtPesquisa.addActionListener(e -> pesquisarProdutos());
    }
    
    private void novoProduto() {
        limparFormulario();
        produtoSelecionado = null;
        txtNome.requestFocus();
    }
    
    private void salvarProduto() {
        try {
            if (!validarFormulario()) {
                return;
            }
            
            Produto produto = criarProdutoDoFormulario();
            
            if (produto.getId() == null || produto.getId() == 0) {
                // Novo produto
                produtoDao.salvar(produto);
                JOptionPane.showMessageDialog(this, "Produto salvo com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            } else {
                // Atualização
                produtoDao.atualizar(produto);
                JOptionPane.showMessageDialog(this, "Produto atualizado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            }
            
            limparFormulario();
            carregarProdutos();
            
        } catch (Exception e) {
            logger.error("Erro ao salvar produto: " + e.getMessage(), e);
            JOptionPane.showMessageDialog(this, "Erro ao salvar produto: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void editarProduto() {
        int linhaSelecionada = tabelaProdutos.getSelectedRow();
        if (linhaSelecionada == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um produto para editar.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        try {
            Integer id = (Integer) modeloTabela.getValueAt(linhaSelecionada, 0);
            Produto produto = produtoDao.buscarPorId(id);
            
            if (produto != null) {
                preencherFormulario(produto);
                produtoSelecionado = produto;
            }
            
        } catch (Exception e) {
            logger.error("Erro ao carregar produto: " + e.getMessage(), e);
            JOptionPane.showMessageDialog(this, "Erro ao carregar produto: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void excluirProduto() {
        int linhaSelecionada = tabelaProdutos.getSelectedRow();
        if (linhaSelecionada == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um produto para excluir.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int confirmacao = JOptionPane.showConfirmDialog(this, 
            "Tem certeza que deseja excluir este produto?", 
            "Confirmar Exclusão", 
            JOptionPane.YES_NO_OPTION);
            
        if (confirmacao == JOptionPane.YES_OPTION) {
            try {
                Integer id = (Integer) modeloTabela.getValueAt(linhaSelecionada, 0);
                
                // Verificar se produto está sendo usado em vendas antes de tentar excluir
                String checkQuery = "SELECT COUNT(*) FROM item_venda WHERE produto_id = ?";
                try (PreparedStatement ps = PostgreSQLConnection.getConnection().prepareStatement(checkQuery)) {
                    ps.setInt(1, id);
                    ResultSet rs = ps.executeQuery();
                    if (rs.next() && rs.getInt(1) > 0) {
                        // Produto está em uso, oferecer desativar
                        int opcao = JOptionPane.showOptionDialog(this,
                            "Este produto não pode ser excluído pois está registrado em vendas.\n\n" +
                            "Deseja desativá-lo em vez de excluir?\n" +
                            "Produtos desativados não aparecerão mais nas vendas.",
                            "Produto em Uso",
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.QUESTION_MESSAGE,
                            null,
                            new String[]{"Desativar Produto", "Cancelar"},
                            "Cancelar");
                            
                        if (opcao == JOptionPane.YES_OPTION) {
                            Produto produto = produtoDao.buscarPorId(id);
                            if (produto != null) {
                                produto.setAtivo(false);
                                produtoDao.atualizar(produto);
                                
                                JOptionPane.showMessageDialog(this, "Produto desativado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                                limparFormulario();
                                carregarProdutos();
                            }
                        }
                        return; // Sair do método sem tentar excluir
                    }
                }
                
                // Se não estiver em uso, pode excluir normalmente
                produtoDao.remover(id);
                
                JOptionPane.showMessageDialog(this, "Produto excluído com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                limparFormulario();
                carregarProdutos();
                
            } catch (Exception e) {
                logger.error("Erro ao excluir produto: " + e.getMessage(), e);
                JOptionPane.showMessageDialog(this, "Erro ao excluir produto: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void cancelarEdicao() {
        limparFormulario();
        produtoSelecionado = null;
    }
    
    private void limparFormulario() {
        txtNome.setText("");
        txtCodigo.setText("");
        txtDescricao.setText("");
        txtPrecoCompra.setText("");
        txtPrecoVenda.setText("");
        txtEstoque.setText("");
        txtEstoqueMinimo.setText("");
        txtFornecedor.setText("");
        chkAtivo.setSelected(true);
        
        txtNome.requestFocus();
    }
    
    private void pesquisarProdutos() {
        try {
            List<Produto> produtos;
            String textoPesquisa = txtPesquisa.getText().trim();
            String filtro = (String) comboFiltro.getSelectedItem();
            
            if (textoPesquisa.isEmpty()) {
                produtos = produtoDao.listar();
            } else {
                produtos = produtoDao.listar(); // Implementar método de pesquisa específico
                
                // Filtrar localmente (implementar pesquisa no DAO seria melhor)
                String filtroLower = textoPesquisa.toLowerCase();
                produtos = produtos.stream()
                    .filter(p -> {
                        switch (filtro) {
                            case "Nome":
                                return p.getNome().toLowerCase().contains(filtroLower);
                            case "Código":
                                return p.getCodigo().toLowerCase().contains(filtroLower);
                            case "Descrição":
                                return p.getDescricao().toLowerCase().contains(filtroLower);
                            default:
                                return p.getNome().toLowerCase().contains(filtroLower) ||
                                       p.getCodigo().toLowerCase().contains(filtroLower) ||
                                       p.getDescricao().toLowerCase().contains(filtroLower);
                        }
                    })
                    .toList();
            }
            
            atualizarTabela(produtos);
            
        } catch (Exception e) {
            logger.error("Erro ao pesquisar produtos: " + e.getMessage(), e);
            JOptionPane.showMessageDialog(this, "Erro ao pesquisar produtos: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void selecionarProdutoDaTabela() {
        editarProduto();
    }
    
    private void carregarProdutos() {
        try {
            List<Produto> produtos = produtoDao.listar();
            atualizarTabela(produtos);
        } catch (Exception e) {
            logger.error("Erro ao carregar produtos: " + e.getMessage(), e);
            JOptionPane.showMessageDialog(this, "Erro ao carregar produtos: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void atualizarTabela(List<Produto> produtos) {
        modeloTabela.setRowCount(0);
        
        for (Produto produto : produtos) {
            Object[] linha = {
                produto.getId(),
                produto.getCodigo(),
                produto.getNome(),
                produto.getDescricao(),
                "R$ " + String.format("%.2f", produto.getPreco()),
                produto.getEstoque(),
                produto.getAtivo() ? "Ativo" : "Inativo"
            };
            modeloTabela.addRow(linha);
        }
    }
    
    private boolean validarFormulario() {
        if (txtNome.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "O nome é obrigatório.", "Validação", JOptionPane.WARNING_MESSAGE);
            txtNome.requestFocus();
            return false;
        }
        
        if (txtCodigo.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "O código é obrigatório.", "Validação", JOptionPane.WARNING_MESSAGE);
            txtCodigo.requestFocus();
            return false;
        }
        
        try {
            if (!txtPrecoVenda.getText().trim().isEmpty()) {
                new BigDecimal(txtPrecoVenda.getText().replace(",", "."));
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Preço de venda inválido.", "Validação", JOptionPane.WARNING_MESSAGE);
            txtPrecoVenda.requestFocus();
            return false;
        }
        
        try {
            if (!txtEstoque.getText().trim().isEmpty()) {
                Integer.parseInt(txtEstoque.getText());
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Estoque inválido.", "Validação", JOptionPane.WARNING_MESSAGE);
            txtEstoque.requestFocus();
            return false;
        }
        
        return true;
    }
    
    private Produto criarProdutoDoFormulario() {
        Produto produto = new Produto();
        
        // Se há um produto selecionado, usamos seu ID para atualização
        if (produtoSelecionado != null) {
            produto.setId(produtoSelecionado.getId());
        }
        
        produto.setNome(txtNome.getText().trim());
        produto.setCodigo(txtCodigo.getText().trim());
        produto.setDescricao(txtDescricao.getText().trim());
        
        try {
            produto.setPreco(new BigDecimal(txtPrecoVenda.getText().replace(",", ".")));
        } catch (NumberFormatException e) {
            produto.setPreco(BigDecimal.ZERO);
        }
        
        try {
            produto.setEstoque(Integer.parseInt(txtEstoque.getText().trim()));
        } catch (NumberFormatException e) {
            produto.setEstoque(0);
        }
        
        produto.setAtivo(chkAtivo.isSelected());
        
        return produto;
    }
    
    private void preencherFormulario(Produto produto) {
        txtNome.setText(produto.getNome());
        txtCodigo.setText(produto.getCodigo());
        txtDescricao.setText(produto.getDescricao());
        txtPrecoVenda.setText(produto.getPreco() != null ? produto.getPreco().toString() : "");
        txtEstoque.setText(produto.getEstoque() != null ? produto.getEstoque().toString() : "");
        chkAtivo.setSelected(produto.getAtivo());
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
