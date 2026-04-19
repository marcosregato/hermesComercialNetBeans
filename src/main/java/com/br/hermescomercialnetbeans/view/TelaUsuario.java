package com.br.hermescomercialnetbeans.view;

import com.br.hermescomercialnetbeans.model.Usuario;
import com.br.hermescomercialnetbeans.dao.UsuarioDao;
import org.apache.logging.log4j.LogManager;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

/**
 * Tela para gerenciamento de usuários do sistema PDV
 * Suporta Funcionários, Clientes e Fornecedores
 * 
 * @author marcos
 */
public class TelaUsuario extends JInternalFrame {
    
    private static final org.apache.logging.log4j.Logger logger = LogManager.getLogger(TelaUsuario.class);
    
    // Cores dos botões
    private static final Color COR_PRIMARIA = new Color(52, 152, 219);
    private static final Color COR_SUCESSO = new Color(46, 204, 113);
    private static final Color COR_PERIGO = new Color(231, 76, 60);
    private static final Color COR_SECUNDARIA = new Color(155, 89, 182);
    
    // Componentes da interface
    private JPanel panelPrincipal;
    private JPanel panelFormulario;
    private JPanel panelBotoes;
    private JPanel panelFiltros;
    private JScrollPane scrollPaneTabela;
    private JTable tabelaUsuarios;
    
    // Campos do formulário básicos
    private JTextField txtId;
    private JTextField txtNome;
    private JTextField txtLogin;
    private JPasswordField txtSenha;
    private JTextField txtEmail;
    private JTextField txtCargo;
    private JComboBox<String> comboNivelAcesso;
    private JComboBox<String> comboTipoUsuario;
    private JCheckBox chkAtivo;
    
    // Campos específicos de Funcionário
    private JTextField txtMatricula;
    private JTextField txtDepartamento;
    private JTextField txtSalario;
    private JTextField txtDataAdmissao;
    private JTextField txtDataDemissao;
    
    // Campos específicos de Cliente
    private JTextField txtCpf;
    private JTextField txtDataNascimento;
    private JTextField txtTelefone;
    private JTextField txtEndereco;
    private JTextField txtBairro;
    private JTextField txtCidade;
    private JTextField txtEstado;
    private JTextField txtCep;
    private JTextField txtLimiteCredito;
    private JTextField txtPontosFidelidade;
    
    // Campos específicos de Fornecedor
    private JTextField txtCnpj;
    private JTextField txtRazaoSocial;
    private JTextField txtNomeFantasia;
    private JTextField txtInscricaoEstadual;
    private JTextField txtTelefoneContato;
    private JTextField txtEmailContato;
    private JTextField txtEnderecoFornecedor;
    private JTextField txtCondicoesPagamento;
    private JTextField txtPrazoEntrega;
    
    // Botões
    private JButton btNovo;
    private JButton btSalvar;
    private JButton btEditar;
    private JButton btExcluir;
    private JButton btCancelar;
    private JButton btLimpar;
    private JButton btBuscar;
    
    // Filtros
    private JTextField txtFiltroNome;
    private JTextField txtFiltroCpfCnpj;
    private JComboBox<String> comboFiltroTipo;
    private JCheckBox chkFiltroAtivos;
    
    // Outros componentes
    private JTabbedPane tabbedPaneCampos;
    private JPanel panelCamposBasicos;
    private JPanel panelCamposFuncionario;
    private JPanel panelCamposCliente;
    private JPanel panelCamposFornecedor;
    
    // DAO e modelo
    private UsuarioDao usuarioDao;
    private DefaultTableModel modeloTabela;
    private Usuario usuarioSelecionado;
    
    public TelaUsuario() {
        super("Gerenciamento de Usuários");
        this.usuarioDao = new UsuarioDao();
        inicializarComponentes();
        configurarLayout();
        configurarEventos();
        carregarUsuarios();
    }
    
    private void inicializarComponentes() {
        // Painéis principais
        panelPrincipal = new JPanel(new BorderLayout());
        panelFormulario = new JPanel(new BorderLayout());
        panelBotoes = new JPanel(new FlowLayout());
        panelFiltros = new JPanel(new FlowLayout());
        
        // Campos básicos
        txtId = new JTextField(10);
        txtId.setEditable(false);
        txtNome = new JTextField(25);
        txtLogin = new JTextField(20);
        txtSenha = new JPasswordField(15);
        txtEmail = new JTextField(20);
        txtCargo = new JTextField(18);
        comboNivelAcesso = new JComboBox<>(new String[]{"ADMIN", "GERENTE", "OPERADOR", "VISUALIZADOR"});
        comboTipoUsuario = new JComboBox<>(new String[]{"FUNCIONARIO", "CLIENTE", "FORNECEDOR"});
        chkAtivo = new JCheckBox("Ativo");
        chkAtivo.setSelected(true);
        
        // Campos de Funcionário
        txtMatricula = new JTextField(15);
        txtDepartamento = new JTextField(20);
        txtSalario = new JTextField(15);
        txtDataAdmissao = new JTextField(15);
        txtDataDemissao = new JTextField(15);
        
        // Campos de Cliente
        txtCpf = new JTextField(15);
        txtDataNascimento = new JTextField(15);
        txtTelefone = new JTextField(20);
        txtEndereco = new JTextField(40);
        txtBairro = new JTextField(20);
        txtCidade = new JTextField(20);
        txtEstado = new JTextField(10);
        txtCep = new JTextField(15);
        txtLimiteCredito = new JTextField(15);
        txtPontosFidelidade = new JTextField(10);
        
        // Campos de Fornecedor
        txtCnpj = new JTextField(15);
        txtRazaoSocial = new JTextField(30);
        txtNomeFantasia = new JTextField(30);
        txtInscricaoEstadual = new JTextField(20);
        txtTelefoneContato = new JTextField(20);
        txtEmailContato = new JTextField(30);
        txtEnderecoFornecedor = new JTextField(40);
        txtCondicoesPagamento = new JTextField(30);
        txtPrazoEntrega = new JTextField(10);
        
        // Botões coloridos
        btNovo = criarBotao("Novo", COR_SUCESSO);
        btSalvar = criarBotao("Salvar", COR_PRIMARIA);
        btEditar = criarBotao("Editar", COR_SECUNDARIA);
        btExcluir = criarBotao("Excluir", COR_PERIGO);
        btCancelar = criarBotao("Cancelar", Color.GRAY);
        btLimpar = criarBotao("Limpar", Color.ORANGE);
        btBuscar = criarBotao("Buscar", COR_PRIMARIA);
        
        // Filtros
        txtFiltroNome = new JTextField(20);
        txtFiltroCpfCnpj = new JTextField(18);
        comboFiltroTipo = new JComboBox<>(new String[]{"TODOS", "FUNCIONARIO", "CLIENTE", "FORNECEDOR"});
        chkFiltroAtivos = new JCheckBox("Apenas Ativos");
        chkFiltroAtivos.setSelected(true);
        
        // Tabela
        modeloTabela = new DefaultTableModel();
        modeloTabela.addColumn("ID");
        modeloTabela.addColumn("Nome");
        modeloTabela.addColumn("Login");
        modeloTabela.addColumn("Tipo");
        modeloTabela.addColumn("Nível");
        modeloTabela.addColumn("Ativo");
        
        tabelaUsuarios = new JTable(modeloTabela);
        scrollPaneTabela = new JScrollPane(tabelaUsuarios);
        
        // TabbedPane para campos específicos
        tabbedPaneCampos = new JTabbedPane();
        panelCamposBasicos = new JPanel(new GridLayout(0, 2, 5, 5));
        panelCamposFuncionario = new JPanel(new GridLayout(0, 2, 5, 5));
        panelCamposCliente = new JPanel(new GridLayout(0, 2, 5, 5));
        panelCamposFornecedor = new JPanel(new GridLayout(0, 2, 5, 5));
    }
    
    private void configurarLayout() {
        // Configurar painel de filtros com estilo melhor
        panelFiltros.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panelFiltros.setBackground(Color.WHITE);
        panelFiltros.add(new JLabel("Nome:"));
        panelFiltros.add(txtFiltroNome);
        panelFiltros.add(new JLabel("CPF/CNPJ:"));
        panelFiltros.add(txtFiltroCpfCnpj);
        panelFiltros.add(new JLabel("Tipo:"));
        panelFiltros.add(comboFiltroTipo);
        panelFiltros.add(chkFiltroAtivos);
        panelFiltros.add(btBuscar);
        
        // Configurar painel do formulário com GridBagLayout
        panelCamposBasicos.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Linha 1: ID e Nome
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.weightx = 0;
        panelCamposBasicos.add(new JLabel("ID:"), gbc);
        gbc.gridx = 1;
        panelCamposBasicos.add(txtId, gbc);
        gbc.gridx = 2;
        panelCamposBasicos.add(new JLabel("Nome:"), gbc);
        gbc.gridx = 3;
        gbc.weightx = 1.0;
        panelCamposBasicos.add(txtNome, gbc);
        
        // Linha 2: Login e Senha
        gbc.gridx = 0; gbc.gridy = 1;
        gbc.weightx = 0;
        panelCamposBasicos.add(new JLabel("Login:"), gbc);
        gbc.gridx = 1;
        panelCamposBasicos.add(txtLogin, gbc);
        gbc.gridx = 2;
        panelCamposBasicos.add(new JLabel("Senha:"), gbc);
        gbc.gridx = 3;
        gbc.weightx = 1.0;
        panelCamposBasicos.add(txtSenha, gbc);
        
        // Linha 3: Email e Cargo
        gbc.gridx = 0; gbc.gridy = 2;
        gbc.weightx = 0;
        panelCamposBasicos.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1;
        gbc.gridwidth = 3;
        panelCamposBasicos.add(txtEmail, gbc);
        gbc.gridx = 0; gbc.gridy = 3;
        gbc.gridwidth = 1;
        panelCamposBasicos.add(new JLabel("Cargo:"), gbc);
        gbc.gridx = 1;
        panelCamposBasicos.add(txtCargo, gbc);
        
        // Linha 4: Nível Acesso e Tipo Usuário
        gbc.gridx = 0; gbc.gridy = 4;
        gbc.weightx = 0;
        panelCamposBasicos.add(new JLabel("Nível Acesso:"), gbc);
        gbc.gridx = 1;
        panelCamposBasicos.add(comboNivelAcesso, gbc);
        gbc.gridx = 2;
        panelCamposBasicos.add(new JLabel("Tipo Usuário:"), gbc);
        gbc.gridx = 3;
        gbc.weightx = 1.0;
        panelCamposBasicos.add(comboTipoUsuario, gbc);
        
        // Linha 5: Checkbox Ativo
        gbc.gridx = 0; gbc.gridy = 5;
        gbc.gridwidth = 4;
        gbc.weightx = 0;
        panelCamposBasicos.add(chkAtivo, gbc);
        
        // Configurar campos de Funcionário com GridBagLayout
        panelCamposFuncionario.setLayout(new GridBagLayout());
        GridBagConstraints gbcFunc = new GridBagConstraints();
        gbcFunc.insets = new Insets(5, 5, 5, 5);
        gbcFunc.fill = GridBagConstraints.HORIZONTAL;
        
        gbcFunc.gridx = 0; gbcFunc.gridy = 0;
        gbcFunc.weightx = 0;
        panelCamposFuncionario.add(new JLabel("Matrícula:"), gbcFunc);
        gbcFunc.gridx = 1;
        panelCamposFuncionario.add(txtMatricula, gbcFunc);
        gbcFunc.gridx = 2;
        panelCamposFuncionario.add(new JLabel("Departamento:"), gbcFunc);
        gbcFunc.gridx = 3;
        gbcFunc.weightx = 1.0;
        panelCamposFuncionario.add(txtDepartamento, gbcFunc);
        
        gbcFunc.gridx = 0; gbcFunc.gridy = 1;
        gbcFunc.weightx = 0;
        panelCamposFuncionario.add(new JLabel("Salário:"), gbcFunc);
        gbcFunc.gridx = 1;
        panelCamposFuncionario.add(txtSalario, gbcFunc);
        gbcFunc.gridx = 2;
        panelCamposFuncionario.add(new JLabel("Data Admissão:"), gbcFunc);
        gbcFunc.gridx = 3;
        panelCamposFuncionario.add(txtDataAdmissao, gbcFunc);
        
        gbcFunc.gridx = 0; gbcFunc.gridy = 2;
        gbcFunc.gridwidth = 2;
        panelCamposFuncionario.add(new JLabel("Data Demissão:"), gbcFunc);
        gbcFunc.gridx = 2;
        gbcFunc.gridwidth = 2;
        panelCamposFuncionario.add(txtDataDemissao, gbcFunc);
        
        // Configurar campos de Cliente com GridBagLayout
        panelCamposCliente.setLayout(new GridBagLayout());
        GridBagConstraints gbcCli = new GridBagConstraints();
        gbcCli.insets = new Insets(3, 5, 3, 5);
        gbcCli.fill = GridBagConstraints.HORIZONTAL;
        
        gbcCli.gridx = 0; gbcCli.gridy = 0;
        gbcCli.weightx = 0;
        panelCamposCliente.add(new JLabel("CPF:"), gbcCli);
        gbcCli.gridx = 1;
        panelCamposCliente.add(txtCpf, gbcCli);
        gbcCli.gridx = 2;
        panelCamposCliente.add(new JLabel("Data Nascimento:"), gbcCli);
        gbcCli.gridx = 3;
        gbcCli.weightx = 1.0;
        panelCamposCliente.add(txtDataNascimento, gbcCli);
        
        gbcCli.gridx = 0; gbcCli.gridy = 1;
        gbcCli.weightx = 0;
        panelCamposCliente.add(new JLabel("Telefone:"), gbcCli);
        gbcCli.gridx = 1;
        panelCamposCliente.add(txtTelefone, gbcCli);
        gbcCli.gridx = 2;
        panelCamposCliente.add(new JLabel("CEP:"), gbcCli);
        gbcCli.gridx = 3;
        panelCamposCliente.add(txtCep, gbcCli);
        
        gbcCli.gridx = 0; gbcCli.gridy = 2;
        gbcCli.gridwidth = 4;
        gbcCli.weightx = 1.0;
        panelCamposCliente.add(new JLabel("Endereço:"), gbcCli);
        gbcCli.gridx = 0; gbcCli.gridy = 3;
        panelCamposCliente.add(txtEndereco, gbcCli);
        
        gbcCli.gridx = 0; gbcCli.gridy = 4;
        gbcCli.gridwidth = 1;
        gbcCli.weightx = 0;
        panelCamposCliente.add(new JLabel("Bairro:"), gbcCli);
        gbcCli.gridx = 1;
        panelCamposCliente.add(txtBairro, gbcCli);
        gbcCli.gridx = 2;
        panelCamposCliente.add(new JLabel("Cidade:"), gbcCli);
        gbcCli.gridx = 3;
        gbcCli.weightx = 1.0;
        panelCamposCliente.add(txtCidade, gbcCli);
        
        gbcCli.gridx = 0; gbcCli.gridy = 5;
        gbcCli.weightx = 0;
        panelCamposCliente.add(new JLabel("Estado:"), gbcCli);
        gbcCli.gridx = 1;
        panelCamposCliente.add(txtEstado, gbcCli);
        gbcCli.gridx = 2;
        panelCamposCliente.add(new JLabel("Limite Crédito:"), gbcCli);
        gbcCli.gridx = 3;
        panelCamposCliente.add(txtLimiteCredito, gbcCli);
        
        gbcCli.gridx = 0; gbcCli.gridy = 6;
        gbcCli.gridwidth = 2;
        panelCamposCliente.add(new JLabel("Pontos Fidelidade:"), gbcCli);
        gbcCli.gridx = 2;
        gbcCli.gridwidth = 2;
        panelCamposCliente.add(txtPontosFidelidade, gbcCli);
        
        // Configurar campos de Fornecedor com GridBagLayout
        panelCamposFornecedor.setLayout(new GridBagLayout());
        GridBagConstraints gbcForn = new GridBagConstraints();
        gbcForn.insets = new Insets(3, 5, 3, 5);
        gbcForn.fill = GridBagConstraints.HORIZONTAL;
        
        gbcForn.gridx = 0; gbcForn.gridy = 0;
        gbcForn.weightx = 0;
        panelCamposFornecedor.add(new JLabel("CNPJ:"), gbcForn);
        gbcForn.gridx = 1;
        panelCamposFornecedor.add(txtCnpj, gbcForn);
        gbcForn.gridx = 2;
        panelCamposFornecedor.add(new JLabel("Razão Social:"), gbcForn);
        gbcForn.gridx = 3;
        gbcForn.weightx = 1.0;
        panelCamposFornecedor.add(txtRazaoSocial, gbcForn);
        
        gbcForn.gridx = 0; gbcForn.gridy = 1;
        gbcForn.weightx = 0;
        panelCamposFornecedor.add(new JLabel("Nome Fantasia:"), gbcForn);
        gbcForn.gridx = 1;
        gbcForn.gridwidth = 3;
        panelCamposFornecedor.add(txtNomeFantasia, gbcForn);
        
        gbcForn.gridx = 0; gbcForn.gridy = 2;
        gbcForn.gridwidth = 1;
        panelCamposFornecedor.add(new JLabel("Inscrição Estadual:"), gbcForn);
        gbcForn.gridx = 1;
        panelCamposFornecedor.add(txtInscricaoEstadual, gbcForn);
        gbcForn.gridx = 2;
        panelCamposFornecedor.add(new JLabel("Telefone Contato:"), gbcForn);
        gbcForn.gridx = 3;
        gbcForn.weightx = 1.0;
        panelCamposFornecedor.add(txtTelefoneContato, gbcForn);
        
        gbcForn.gridx = 0; gbcForn.gridy = 3;
        gbcForn.gridwidth = 4;
        gbcForn.weightx = 1.0;
        panelCamposFornecedor.add(new JLabel("Email Contato:"), gbcForn);
        gbcForn.gridx = 0; gbcForn.gridy = 4;
        panelCamposFornecedor.add(txtEmailContato, gbcForn);
        
        gbcForn.gridx = 0; gbcForn.gridy = 5;
        gbcForn.gridwidth = 4;
        gbcForn.weightx = 1.0;
        panelCamposFornecedor.add(new JLabel("Endereço:"), gbcForn);
        gbcForn.gridx = 0; gbcForn.gridy = 6;
        panelCamposFornecedor.add(txtEnderecoFornecedor, gbcForn);
        
        gbcForn.gridx = 0; gbcForn.gridy = 7;
        gbcForn.gridwidth = 2;
        gbcForn.weightx = 0;
        panelCamposFornecedor.add(new JLabel("Condições Pagamento:"), gbcForn);
        gbcForn.gridx = 2;
        panelCamposFornecedor.add(new JLabel("Prazo Entrega (dias):"), gbcForn);
        gbcForn.gridx = 3;
        gbcForn.weightx = 1.0;
        panelCamposFornecedor.add(txtPrazoEntrega, gbcForn);
        
        gbcForn.gridx = 0; gbcForn.gridy = 8;
        gbcForn.gridx = 1;
        gbcForn.gridwidth = 3;
        panelCamposFornecedor.add(txtCondicoesPagamento, gbcForn);
        
        // Adicionar abas ao TabbedPane
        tabbedPaneCampos.addTab("Dados Básicos", panelCamposBasicos);
        tabbedPaneCampos.addTab("Funcionário", panelCamposFuncionario);
        tabbedPaneCampos.addTab("Cliente", panelCamposCliente);
        tabbedPaneCampos.addTab("Fornecedor", panelCamposFornecedor);
        
        // Configurar painel de botões com estilo melhor
        panelBotoes.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panelBotoes.setBackground(Color.LIGHT_GRAY);
        panelBotoes.add(btNovo);
        panelBotoes.add(btSalvar);
        panelBotoes.add(btEditar);
        panelBotoes.add(btExcluir);
        panelBotoes.add(btCancelar);
        panelBotoes.add(btLimpar);
        
        // Configurar painel do formulário com borda
        panelFormulario.setBorder(BorderFactory.createTitledBorder("Dados do Usuário"));
        panelFormulario.add(tabbedPaneCampos, BorderLayout.CENTER);
        panelFormulario.add(panelBotoes, BorderLayout.SOUTH);
        
        // Configurar tabela com borda e tamanho fixo
        scrollPaneTabela.setBorder(BorderFactory.createTitledBorder("Lista de Usuários"));
        scrollPaneTabela.setPreferredSize(new Dimension(850, 350));
        scrollPaneTabela.setMinimumSize(new Dimension(850, 350));
        scrollPaneTabela.setMaximumSize(new Dimension(850, 350));
        
        // Configurar tamanho da tabela
        tabelaUsuarios.setPreferredScrollableViewportSize(new Dimension(830, 330));
        
        // Dividir a tela
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        splitPane.setTopComponent(panelFormulario);
        splitPane.setBottomComponent(scrollPaneTabela);
        splitPane.setDividerLocation(380);
        splitPane.setResizeWeight(0.65);
        splitPane.setEnabled(false); // Desabilitar redimensionamento para manter fixo
        
        // Adicionar tudo ao painel principal
        panelPrincipal.add(panelFiltros, BorderLayout.NORTH);
        panelPrincipal.add(splitPane, BorderLayout.CENTER);
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        
        // Configurar o frame
        this.setContentPane(panelPrincipal);
        this.setSize(900, 600);
        this.setClosable(true);
        this.setMaximizable(true);
        this.setIconifiable(true);
        this.setResizable(true);
        
        // Centralizar
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation((screenSize.width - this.getWidth()) / 2, (screenSize.height - this.getHeight()) / 2);
    }
    
    private void configurarEventos() {
        // Evento do comboTipoUsuario para mostrar/ocultar abas
        comboTipoUsuario.addActionListener(e -> atualizarAbasPorTipo());
        
        // Eventos dos botões
        btNovo.addActionListener(e -> novoUsuario());
        btSalvar.addActionListener(e -> salvarUsuario());
        btEditar.addActionListener(e -> editarUsuario());
        btExcluir.addActionListener(e -> excluirUsuario());
        btCancelar.addActionListener(e -> cancelarEdicao());
        btLimpar.addActionListener(e -> limparFormulario());
        btBuscar.addActionListener(e -> buscarUsuarios());
        
        // Evento da tabela
        tabelaUsuarios.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    selecionarUsuarioDaTabela();
                }
            }
        });
        
        // Inicializar estado das abas
        atualizarAbasPorTipo();
    }
    
    private void atualizarAbasPorTipo() {
        String tipo = (String) comboTipoUsuario.getSelectedItem();
        
        // Habilitar/desabilitar abas baseadas no tipo
        tabbedPaneCampos.setEnabledAt(1, Usuario.TIPO_FUNCIONARIO.equals(tipo)); // Funcionário
        tabbedPaneCampos.setEnabledAt(2, Usuario.TIPO_CLIENTE.equals(tipo)); // Cliente
        tabbedPaneCampos.setEnabledAt(3, Usuario.TIPO_FORNECEDOR.equals(tipo)); // Fornecedor
        
        // Limpar campos das abas desabilitadas
        if (!Usuario.TIPO_FUNCIONARIO.equals(tipo)) {
            limparCamposFuncionario();
        }
        if (!Usuario.TIPO_CLIENTE.equals(tipo)) {
            limparCamposCliente();
        }
        if (!Usuario.TIPO_FORNECEDOR.equals(tipo)) {
            limparCamposFornecedor();
        }
    }
    
    private void novoUsuario() {
        limparFormulario();
        usuarioSelecionado = null;
        txtNome.requestFocus();
    }
    
    private void salvarUsuario() {
        try {
            if (!validarFormulario()) {
                return;
            }
            
            Usuario usuario = criarUsuarioDoFormulario();
            
            if (usuario.getId() == null || usuario.getId() == 0) {
                // Novo usuário
                usuarioDao.salvar(usuario);
                JOptionPane.showMessageDialog(this, "Usuário salvo com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            } else {
                // Atualização
                usuarioDao.atualizar(usuario);
                JOptionPane.showMessageDialog(this, "Usuário atualizado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            }
            
            limparFormulario();
            carregarUsuarios();
            
        } catch (Exception e) {
            logger.error("Erro ao salvar usuário: " + e.getMessage(), e);
            JOptionPane.showMessageDialog(this, "Erro ao salvar usuário: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void editarUsuario() {
        int linhaSelecionada = tabelaUsuarios.getSelectedRow();
        if (linhaSelecionada == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um usuário para editar.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        try {
            Integer id = (Integer) modeloTabela.getValueAt(linhaSelecionada, 0);
            Usuario usuario = usuarioDao.buscarPorId(id);
            
            if (usuario != null) {
                preencherFormulario(usuario);
                usuarioSelecionado = usuario;
            }
            
        } catch (Exception e) {
            logger.error("Erro ao carregar usuário: " + e.getMessage(), e);
            JOptionPane.showMessageDialog(this, "Erro ao carregar usuário: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void excluirUsuario() {
        int linhaSelecionada = tabelaUsuarios.getSelectedRow();
        if (linhaSelecionada == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um usuário para excluir.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int confirmacao = JOptionPane.showConfirmDialog(this, 
            "Tem certeza que deseja excluir este usuário?", 
            "Confirmar Exclusão", 
            JOptionPane.YES_NO_OPTION);
            
        if (confirmacao == JOptionPane.YES_OPTION) {
            try {
                Integer id = (Integer) modeloTabela.getValueAt(linhaSelecionada, 0);
                usuarioDao.remover(id);
                
                JOptionPane.showMessageDialog(this, "Usuário excluído com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                limparFormulario();
                carregarUsuarios();
                
            } catch (Exception e) {
                logger.error("Erro ao excluir usuário: " + e.getMessage(), e);
                JOptionPane.showMessageDialog(this, "Erro ao excluir usuário: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void cancelarEdicao() {
        limparFormulario();
        usuarioSelecionado = null;
    }
    
    private void limparFormulario() {
        txtId.setText("");
        txtNome.setText("");
        txtLogin.setText("");
        txtSenha.setText("");
        txtEmail.setText("");
        txtCargo.setText("");
        comboNivelAcesso.setSelectedIndex(0);
        comboTipoUsuario.setSelectedIndex(0);
        chkAtivo.setSelected(true);
        
        limparCamposFuncionario();
        limparCamposCliente();
        limparCamposFornecedor();
        
        atualizarAbasPorTipo();
    }
    
    private void limparCamposFuncionario() {
        txtMatricula.setText("");
        txtDepartamento.setText("");
        txtSalario.setText("");
        txtDataAdmissao.setText("");
        txtDataDemissao.setText("");
    }
    
    private void limparCamposCliente() {
        txtCpf.setText("");
        txtDataNascimento.setText("");
        txtTelefone.setText("");
        txtEndereco.setText("");
        txtBairro.setText("");
        txtCidade.setText("");
        txtEstado.setText("");
        txtCep.setText("");
        txtLimiteCredito.setText("");
        txtPontosFidelidade.setText("");
    }
    
    private void limparCamposFornecedor() {
        txtCnpj.setText("");
        txtRazaoSocial.setText("");
        txtNomeFantasia.setText("");
        txtInscricaoEstadual.setText("");
        txtTelefoneContato.setText("");
        txtEmailContato.setText("");
        txtEnderecoFornecedor.setText("");
        txtCondicoesPagamento.setText("");
        txtPrazoEntrega.setText("");
    }
    
    private void buscarUsuarios() {
        try {
            List<Usuario> usuarios;
            String filtroNome = txtFiltroNome.getText().trim();
            String filtroCpfCnpj = txtFiltroCpfCnpj.getText().trim();
            String filtroTipo = (String) comboFiltroTipo.getSelectedItem();
            boolean apenasAtivos = chkFiltroAtivos.isSelected();
            
            // Buscar por CPF/CNPJ se preenchido
            if (!filtroCpfCnpj.isEmpty()) {
                Usuario usuario = null;
                if ("CLIENTE".equals(filtroTipo)) {
                    usuario = usuarioDao.buscarClientePorCpf(filtroCpfCnpj);
                } else if ("FORNECEDOR".equals(filtroTipo)) {
                    usuario = usuarioDao.buscarFornecedorPorCnpj(filtroCpfCnpj);
                } else {
                    // Buscar em todos os tipos
                    usuario = usuarioDao.buscarClientePorCpf(filtroCpfCnpj);
                    if (usuario == null) {
                        usuario = usuarioDao.buscarFornecedorPorCnpj(filtroCpfCnpj);
                    }
                }
                
                if (usuario != null) {
                    usuarios = List.of(usuario);
                } else {
                    usuarios = List.of();
                }
            } else {
                // Buscar normal por tipo
                if ("TODOS".equals(filtroTipo)) {
                    usuarios = apenasAtivos ? usuarioDao.buscarAtivos() : usuarioDao.listar();
                } else {
                    usuarios = usuarioDao.buscarPorTipo(filtroTipo);
                }
                
                // Filtrar por nome se necessário
                if (!filtroNome.isEmpty()) {
                    usuarios = usuarios.stream()
                        .filter(u -> u.getNome().toLowerCase().contains(filtroNome.toLowerCase()))
                        .toList();
                }
            }
            
            atualizarTabela(usuarios);
            
        } catch (Exception e) {
            logger.error("Erro ao buscar usuários: " + e.getMessage(), e);
            JOptionPane.showMessageDialog(this, "Erro ao buscar usuários: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void selecionarUsuarioDaTabela() {
        editarUsuario();
    }
    
    private void carregarUsuarios() {
        try {
            List<Usuario> usuarios = usuarioDao.listar();
            atualizarTabela(usuarios);
        } catch (Exception e) {
            logger.error("Erro ao carregar usuários: " + e.getMessage(), e);
            JOptionPane.showMessageDialog(this, "Erro ao carregar usuários: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void atualizarTabela(List<Usuario> usuarios) {
        modeloTabela.setRowCount(0);
        
        for (Usuario usuario : usuarios) {
            Object[] linha = {
                usuario.getId(),
                usuario.getNome(),
                usuario.getLogin(),
                usuario.getDescricaoTipoUsuario(),
                usuario.getDescricaoNivelAcesso(),
                usuario.getAtivo() ? "Sim" : "Não"
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
        
        if (txtLogin.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "O login é obrigatório.", "Validação", JOptionPane.WARNING_MESSAGE);
            txtLogin.requestFocus();
            return false;
        }
        
        String tipo = (String) comboTipoUsuario.getSelectedItem();
        
        if (Usuario.TIPO_FUNCIONARIO.equals(tipo)) {
            if (txtMatricula.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "A matrícula é obrigatória para funcionários.", "Validação", JOptionPane.WARNING_MESSAGE);
                tabbedPaneCampos.setSelectedIndex(1);
                txtMatricula.requestFocus();
                return false;
            }
        } else if (Usuario.TIPO_CLIENTE.equals(tipo)) {
            if (txtCpf.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "O CPF é obrigatório para clientes.", "Validação", JOptionPane.WARNING_MESSAGE);
                tabbedPaneCampos.setSelectedIndex(2);
                txtCpf.requestFocus();
                return false;
            }
        } else if (Usuario.TIPO_FORNECEDOR.equals(tipo)) {
            if (txtCnpj.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "O CNPJ é obrigatório para fornecedores.", "Validação", JOptionPane.WARNING_MESSAGE);
                tabbedPaneCampos.setSelectedIndex(3);
                txtCnpj.requestFocus();
                return false;
            }
        }
        
        return true;
    }
    
    private Usuario criarUsuarioDoFormulario() {
        Usuario usuario = new Usuario();
        
        // Se há um usuário selecionado, usamos seu ID para atualização
        if (usuarioSelecionado != null) {
            usuario.setId(usuarioSelecionado.getId());
        } else if (!txtId.getText().trim().isEmpty()) {
            try {
                usuario.setId(Integer.parseInt(txtId.getText()));
            } catch (NumberFormatException e) {
                // Ignorar erro, será tratado como novo usuário
            }
        }
        
        // Campos básicos
        usuario.setNome(txtNome.getText().trim());
        usuario.setLogin(txtLogin.getText().trim());
        usuario.setSenha(new String(txtSenha.getPassword()));
        usuario.setEmail(txtEmail.getText().trim());
        usuario.setCargo(txtCargo.getText().trim());
        usuario.setNivelAcesso((String) comboNivelAcesso.getSelectedItem());
        usuario.setTipoUsuario((String) comboTipoUsuario.getSelectedItem());
        usuario.setAtivo(chkAtivo.isSelected());
        
        // Campos específicos de Funcionário
        usuario.setMatricula(txtMatricula.getText().trim());
        usuario.setDepartamento(txtDepartamento.getText().trim());
        try {
            usuario.setSalario(txtSalario.getText().trim().isEmpty() ? null : Double.parseDouble(txtSalario.getText().replace(",", ".")));
        } catch (NumberFormatException e) {
            usuario.setSalario(null);
        }
        usuario.setDataAdmissao(txtDataAdmissao.getText().trim().isEmpty() ? null : java.time.LocalDateTime.parse(txtDataAdmissao.getText() + "T00:00:00"));
        usuario.setDataDemissao(txtDataDemissao.getText().trim().isEmpty() ? null : txtDataDemissao.getText().trim());
        
        // Campos específicos de Cliente
        usuario.setCpf(txtCpf.getText().trim());
        usuario.setDataNascimento(txtDataNascimento.getText().trim());
        usuario.setTelefone(txtTelefone.getText().trim());
        usuario.setEndereco(txtEndereco.getText().trim());
        usuario.setBairro(txtBairro.getText().trim());
        usuario.setCidade(txtCidade.getText().trim());
        usuario.setEstado(txtEstado.getText().trim());
        usuario.setCep(txtCep.getText().trim());
        usuario.setLimiteCredito(txtLimiteCredito.getText().trim());
        try {
            usuario.setPontosFidelidade(txtPontosFidelidade.getText().trim().isEmpty() ? 0 : Integer.parseInt(txtPontosFidelidade.getText()));
        } catch (NumberFormatException e) {
            usuario.setPontosFidelidade(0);
        }
        
        // Campos específicos de Fornecedor
        usuario.setCnpj(txtCnpj.getText().trim());
        usuario.setRazaoSocial(txtRazaoSocial.getText().trim());
        usuario.setNomeFantasia(txtNomeFantasia.getText().trim());
        usuario.setInscricaoEstadual(txtInscricaoEstadual.getText().trim());
        usuario.setTelefoneContato(txtTelefoneContato.getText().trim());
        usuario.setEmailContato(txtEmailContato.getText().trim());
        usuario.setEnderecoFornecedor(txtEnderecoFornecedor.getText().trim());
        usuario.setCondicoesPagamento(txtCondicoesPagamento.getText().trim());
        try {
            usuario.setPrazoEntrega(txtPrazoEntrega.getText().trim().isEmpty() ? 0 : Integer.parseInt(txtPrazoEntrega.getText()));
        } catch (NumberFormatException e) {
            usuario.setPrazoEntrega(0);
        }
        
        return usuario;
    }
    
    private void preencherFormulario(Usuario usuario) {
        // Campos básicos
        txtId.setText(usuario.getId() != null ? usuario.getId().toString() : "");
        txtNome.setText(usuario.getNome());
        txtLogin.setText(usuario.getLogin());
        txtSenha.setText(usuario.getSenha());
        txtEmail.setText(usuario.getEmail());
        txtCargo.setText(usuario.getCargo());
        comboNivelAcesso.setSelectedItem(usuario.getNivelAcesso());
        comboTipoUsuario.setSelectedItem(usuario.getTipoUsuario());
        chkAtivo.setSelected(usuario.getAtivo());
        
        // Campos específicos de Funcionário
        txtMatricula.setText(usuario.getMatricula());
        txtDepartamento.setText(usuario.getDepartamento());
        txtSalario.setText(usuario.getSalario() != null ? usuario.getSalario().toString() : "");
        txtDataAdmissao.setText(usuario.getDataAdmissao() != null ? usuario.getDataAdmissao().toLocalDate().toString() : "");
        txtDataDemissao.setText(usuario.getDataDemissao());
        
        // Campos específicos de Cliente
        txtCpf.setText(usuario.getCpf());
        txtDataNascimento.setText(usuario.getDataNascimento());
        txtTelefone.setText(usuario.getTelefone());
        txtEndereco.setText(usuario.getEndereco());
        txtBairro.setText(usuario.getBairro());
        txtCidade.setText(usuario.getCidade());
        txtEstado.setText(usuario.getEstado());
        txtCep.setText(usuario.getCep());
        txtLimiteCredito.setText(usuario.getLimiteCredito());
        txtPontosFidelidade.setText(usuario.getPontosFidelidade() != null ? usuario.getPontosFidelidade().toString() : "");
        
        // Campos específicos de Fornecedor
        txtCnpj.setText(usuario.getCnpj());
        txtRazaoSocial.setText(usuario.getRazaoSocial());
        txtNomeFantasia.setText(usuario.getNomeFantasia());
        txtInscricaoEstadual.setText(usuario.getInscricaoEstadual());
        txtTelefoneContato.setText(usuario.getTelefoneContato());
        txtEmailContato.setText(usuario.getEmailContato());
        txtEnderecoFornecedor.setText(usuario.getEnderecoFornecedor());
        txtCondicoesPagamento.setText(usuario.getCondicoesPagamento());
        txtPrazoEntrega.setText(usuario.getPrazoEntrega() != null ? usuario.getPrazoEntrega().toString() : "");
        
        atualizarAbasPorTipo();
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
