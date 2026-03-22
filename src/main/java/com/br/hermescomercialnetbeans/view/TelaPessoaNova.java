package com.br.hermescomercialnetbeans.view;

import com.br.hermescomercialnetbeans.dao.UsuarioDao;
import com.br.hermescomercialnetbeans.model.Pessoa;
import com.br.hermescomercialnetbeans.util.ValidarCampo;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Tela para gerenciamento de Pessoas
 * Funcionalidades: CRUD (Create, Read, Update, Delete) e Busca
 *
 * @author Sistema
 */
public class TelaPessoaNova extends javax.swing.JFrame {

    private static final Logger logger = LogManager.getLogger(TelaPessoaNova.class);
    
     
     
    @SuppressWarnings("unchecked")

    // Componentes de entrada
    private JTextField txtNome;
    private JTextField txtEndereco;
    private JTextField txtBairro;
    private JTextField txtCidade;
    private JComboBox<String> cbEstado;
    private JTextField txtCep;
    private JTextField txtCnpj;
    private JTextField txtCpf;
    private JTextField txtTelefone;
    private JTextField txtEmail;
    private JComboBox<String> cbTipoPessoa;

    // Componentes da tabela
    private JTable tabelaPessoas;
    private DefaultTableModel modeloTabela;

    // Componentes de busca
    private JTextField txtBusca;
    private JButton btnBuscar;

    // Botões de ação
    private JButton btnSalvar;
    private JButton btnEditar;
    private JButton btnExcluir;
    private JButton btnLimpar;
    private JButton btnNovo;

    // Dados
    private UsuarioDao pessoaDao;
    private ValidarCampo validador;
    private Pessoa pessoaAtual;
    private boolean eModoEdicao = false;

    public TelaPessoaNova() {
        inicializarComponentes();
        pessoaDao = new UsuarioDao();
        validador = new ValidarCampo();
        pessoaAtual = new Pessoa();
        carregarPessoas();
    }

    private void inicializarComponentes() {
        setTitle("Gerenciamento de Pessoas");
        setDefaultCloseOperation(JInternalFrame.DISPOSE_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null);

        JPanel painelPrincipal = new JPanel(new BorderLayout(10, 10));
        painelPrincipal.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Painel de entrada
        painelPrincipal.add(criarPainelFormulario(), BorderLayout.NORTH);

        // Painel de busca e tabela
        painelPrincipal.add(criarPainelTabela(), BorderLayout.CENTER);

        // Painel de botões
        painelPrincipal.add(criarPainelBotoes(), BorderLayout.SOUTH);

        setContentPane(painelPrincipal);
    }

    private JPanel criarPainelFormulario() {
        JPanel painel = new JPanel();
        painel.setBorder(new TitledBorder("Dados da Pessoa"));
        painel.setLayout(new GridLayout(4, 4, 10, 10));

        // Linha 1
        painel.add(new JLabel("Nome:"));
        txtNome = new JTextField();
        painel.add(txtNome);

        painel.add(new JLabel("Tipo:"));
        cbTipoPessoa = new JComboBox<>(new String[]{"Selecione...", "Usuário", "Fornecedor", "Cliente", "Funcionário"});
        painel.add(cbTipoPessoa);

        painel.add(new JLabel("Telefone:"));
        txtTelefone = new JTextField();
        painel.add(txtTelefone);

        // Linha 2
        painel.add(new JLabel("Endereço:"));
        txtEndereco = new JTextField();
        painel.add(txtEndereco);

        painel.add(new JLabel("CPF:"));
        txtCpf = new JTextField();
        painel.add(txtCpf);

        painel.add(new JLabel("Email:"));
        txtEmail = new JTextField();
        painel.add(txtEmail);

        // Linha 3
        painel.add(new JLabel("Bairro:"));
        txtBairro = new JTextField();
        painel.add(txtBairro);

        painel.add(new JLabel("CNPJ:"));
        txtCnpj = new JTextField();
        painel.add(txtCnpj);

        painel.add(new JLabel("CEP:"));
        txtCep = new JTextField();
        painel.add(txtCep);

        // Linha 4
        painel.add(new JLabel("Cidade:"));
        txtCidade = new JTextField();
        painel.add(txtCidade);

        painel.add(new JLabel("Estado:"));
        cbEstado = new JComboBox<>(getEstados());
        painel.add(cbEstado);

        painel.add(new JLabel(""));
        painel.add(new JLabel(""));

        return painel;
    }

    private JPanel criarPainelTabela() {
        JPanel painel = new JPanel(new BorderLayout(10, 10));
        painel.setBorder(new TitledBorder("Lista de Pessoas"));

        // Painel de busca
        JPanel painelBusca = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        painelBusca.add(new JLabel("Buscar por nome:"));
        txtBusca = new JTextField(20);
        painelBusca.add(txtBusca);
        btnBuscar = new JButton("Buscar");
        btnBuscar.addActionListener(e -> buscarPessoas());
        painelBusca.add(btnBuscar);

        painel.add(painelBusca, BorderLayout.NORTH);

        // Tabela
        String[] colunas = {"ID", "Nome", "Tipo", "Telefone", "Email", "CPF", "CNPJ"};
        modeloTabela = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tabelaPessoas = new JTable(modeloTabela);
        tabelaPessoas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabelaPessoas.getSelectionModel().addListSelectionListener(e -> carregarDadosSelecionado());

        JScrollPane scrollPane = new JScrollPane(tabelaPessoas);
        painel.add(scrollPane, BorderLayout.CENTER);

        return painel;
    }

    private JPanel criarPainelBotoes() {
        JPanel painel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));

        btnNovo = new JButton("Novo");
        btnNovo.addActionListener(e -> novoRegistro());
        painel.add(btnNovo);

        btnSalvar = new JButton("Salvar");
        btnSalvar.addActionListener(e -> salvarPessoa());
        painel.add(btnSalvar);

        btnEditar = new JButton("Editar");
        btnEditar.addActionListener(e -> editarPessoa());
        painel.add(btnEditar);

        btnExcluir = new JButton("Excluir");
        btnExcluir.addActionListener(e -> excluirPessoa());
        painel.add(btnExcluir);

        btnLimpar = new JButton("Limpar");
        btnLimpar.addActionListener(e -> limparFormulario());
        painel.add(btnLimpar);

        return painel;
    }

    private void carregarPessoas() {
        try {
            modeloTabela.setRowCount(0);
            List<Pessoa> pessoas = pessoaDao.lista();

            for (Pessoa p : pessoas) {
                Object[] linha = {
                    p.getId(),
                    p.getNome(),
                    p.getTipoPessoa(),
                    p.getTelefone(),
                    p.getEmail(),
                    p.getCpf(),
                    p.getCnpj()
                };
                modeloTabela.addRow(linha);
            }
        } catch (Exception e) {
            logger.error("Erro ao carregar pessoas: " + e.getMessage(), e);
            JOptionPane.showMessageDialog(this, "Erro ao carregar dados!", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buscarPessoas() {
        String busca = txtBusca.getText().trim();
        if (busca.isEmpty()) {
            carregarPessoas();
            return;
        }

        try {
            modeloTabela.setRowCount(0);
            List<Pessoa> pessoas = pessoaDao.buscar(busca);

            for (Pessoa p : pessoas) {
                Object[] linha = {
                    p.getId(),
                    p.getNome(),
                    p.getTipoPessoa(),
                    p.getTelefone(),
                    p.getEmail(),
                    p.getCpf(),
                    p.getCnpj()
                };
                modeloTabela.addRow(linha);
            }
        } catch (Exception e) {
            logger.error("Erro ao buscar pessoas: " + e.getMessage(), e);
            JOptionPane.showMessageDialog(this, "Erro ao buscar dados!", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void carregarDadosSelecionado() {
        int linha = tabelaPessoas.getSelectedRow();
        if (linha == -1) {
            return;
        }

        try {
            String nome = (String) modeloTabela.getValueAt(linha, 1);
            List<Pessoa> resultados = pessoaDao.buscar(nome);

            if (!resultados.isEmpty()) {
                Pessoa p = resultados.get(0);
                preencherFormulario(p);
                pessoaAtual = p;
                eModoEdicao = true;
            }
        } catch (Exception e) {
            logger.error("Erro ao carregar dados selecionado: " + e.getMessage(), e);
        }
    }

    private void preencherFormulario(Pessoa p) {
        txtNome.setText(p.getNome() != null ? p.getNome() : "");
        txtEndereco.setText(p.getEndereco() != null ? p.getEndereco() : "");
        txtBairro.setText(p.getBairro() != null ? p.getBairro() : "");
        txtCidade.setText(p.getCidade() != null ? p.getCidade() : "");
        cbEstado.setSelectedItem(p.getEstado() != null ? p.getEstado() : "");
        txtCep.setText(p.getCep() != null ? p.getCep() : "");
        txtCnpj.setText(p.getCnpj() != null ? p.getCnpj() : "");
        txtCpf.setText(p.getCpf() != null ? p.getCpf() : "");
        txtTelefone.setText(p.getTelefone() != null ? p.getTelefone() : "");
        txtEmail.setText(p.getEmail() != null ? p.getEmail() : "");
        cbTipoPessoa.setSelectedItem(p.getTipoPessoa() != null ? p.getTipoPessoa() : "Selecione...");
    }

    private void salvarPessoa() {
        if (!validarFormulario()) {
            return;
        }

        try {
            preencherObjeto();

            if (eModoEdicao) {
                pessoaDao.update(pessoaAtual);
                JOptionPane.showMessageDialog(this, "Pessoa atualizada com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            } else {
                pessoaDao.salvar(pessoaAtual);
                JOptionPane.showMessageDialog(this, "Pessoa salva com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            }

            carregarPessoas();
            limparFormulario();
            eModoEdicao = false;
        } catch (Exception e) {
            logger.error("Erro ao salvar pessoa: " + e.getMessage(), e);
            JOptionPane.showMessageDialog(this, "Erro ao salvar categoria: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void editarPessoa() {
        if (tabelaPessoas.getSelectedRow() == -1) {
            JOptionPane.showMessageDialog(this, "Selecione uma pessoa para editar!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        eModoEdicao = true;
        btnSalvar.setText("Atualizar");
    }

    private void excluirPessoa() {
        if (tabelaPessoas.getSelectedRow() == -1) {
            JOptionPane.showMessageDialog(this, "Selecione uma pessoa para excluir!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirmacao = JOptionPane.showConfirmDialog(this, "Deseja realmente excluir este registro?", "Confirmação", JOptionPane.YES_NO_OPTION);
        if (confirmacao != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            String nome = (String) modeloTabela.getValueAt(tabelaPessoas.getSelectedRow(), 1);
            pessoaDao.remove(nome);
            JOptionPane.showMessageDialog(this, "Pessoa excluída com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            carregarPessoas();
            limparFormulario();
        } catch (Exception e) {
            logger.error("Erro ao excluir pessoa: " + e.getMessage(), e);
            JOptionPane.showMessageDialog(this, "Erro ao excluir pessoa: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void novoRegistro() {
        limparFormulario();
        eModoEdicao = false;
        btnSalvar.setText("Salvar");
        pessoaAtual = new Pessoa();
    }

    private void limparFormulario() {
        txtNome.setText("");
        txtEndereco.setText("");
        txtBairro.setText("");
        txtCidade.setText("");
        cbEstado.setSelectedIndex(0);
        txtCep.setText("");
        txtCnpj.setText("");
        txtCpf.setText("");
        txtTelefone.setText("");
        txtEmail.setText("");
        cbTipoPessoa.setSelectedIndex(0);
        tabelaPessoas.clearSelection();
        eModoEdicao = false;
        btnSalvar.setText("Salvar");
    }

    private void preencherObjeto() {
        pessoaAtual.setNome(txtNome.getText().trim());
        pessoaAtual.setEndereco(txtEndereco.getText().trim());
        pessoaAtual.setBairro(txtBairro.getText().trim());
        pessoaAtual.setCidade(txtCidade.getText().trim());
        pessoaAtual.setEstado((String) cbEstado.getSelectedItem());
        pessoaAtual.setCep(txtCep.getText().trim());
        pessoaAtual.setCnpj(txtCnpj.getText().trim());
        pessoaAtual.setCpf(txtCpf.getText().trim());
        pessoaAtual.setTelefone(txtTelefone.getText().trim());
        pessoaAtual.setEmail(txtEmail.getText().trim());
        pessoaAtual.setTipoPessoa((String) cbTipoPessoa.getSelectedItem());
    }

    private boolean validarFormulario() {
        if (txtNome.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nome é obrigatório!", "Validação", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        if (cbTipoPessoa.getSelectedIndex() == 0) {
            JOptionPane.showMessageDialog(this, "Tipo de pessoa é obrigatório!", "Validação", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        String tipo = (String) cbTipoPessoa.getSelectedItem();
        String cpf = txtCpf.getText().trim();
        String cnpj = txtCnpj.getText().trim();

        if ("Usuário".equals(tipo) && !cpf.isEmpty() && !validador.isCPF(cpf)) {
            JOptionPane.showMessageDialog(this, "CPF inválido!", "Validação", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        if ("Fornecedor".equals(tipo) && !cnpj.isEmpty() && !validador.isCNPJ(cnpj)) {
            JOptionPane.showMessageDialog(this, "CNPJ inválido!", "Validação", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        return true;
    }

    private String[] getEstados() {
        return new String[]{
            "Selecione...", "AC", "AL", "AP", "AM", "BA", "CE", "DF", "ES", "GO",
            "MA", "MT", "MS", "MG", "PA", "PB", "PR", "PE", "PI",
            "RJ", "RN", "RS", "RO", "RR", "SC", "SP", "SE", "TO"
        };
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Sistema Hermes Comercial");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1000, 700);
            frame.setLocationRelativeTo(null);

            JDesktopPane desktop = new JDesktopPane();
            TelaPessoaNova tela = new TelaPessoaNova();
            desktop.add(tela);
            tela.setVisible(true);

            frame.setContentPane(desktop);
            frame.setVisible(true);
        });
    }
}
