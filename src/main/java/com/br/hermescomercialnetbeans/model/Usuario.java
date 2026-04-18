package com.br.hermescomercialnetbeans.model;

import java.time.LocalDateTime;

/**
 * Modelo de Usuário para o sistema PDV
 * Representa os usuários que operam o sistema de ponto de venda
 * 
 * @author marcos
 */
public class Usuario {
    
    private Integer id;
    private String nome;
    private String login;
    private String senha;
    private String email;
    private String cargo;
    private String nivelAcesso;
    private LocalDateTime dataCadastro;
    private LocalDateTime ultimoAcesso;
    private Boolean ativo;
    private String permissaoVenda;
    private String permissaoCaixa;
    private String permissaoRelatorio;
    
    // Tipo de usuário
    private String tipoUsuario;
    
    // Campos específicos para Funcionário
    private String matricula;
    private String departamento;
    private Double salario;
    private LocalDateTime dataAdmissao;
    private String dataDemissao;
    
    // Campos específicos para Cliente
    private String cpf;
    private String dataNascimento;
    private String telefone;
    private String endereco;
    private String bairro;
    private String cidade;
    private String estado;
    private String cep;
    private String limiteCredito;
    private Integer pontosFidelidade;
    
    // Campos específicos para Fornecedor
    private String cnpj;
    private String razaoSocial;
    private String nomeFantasia;
    private String inscricaoEstadual;
    private String telefoneContato;
    private String emailContato;
    private String enderecoFornecedor;
    private String condicoesPagamento;
    private Integer prazoEntrega;
    
    // Níveis de acesso predefinidos
    public static final String NIVEL_ADMIN = "ADMIN";
    public static final String NIVEL_GERENTE = "GERENTE";
    public static final String NIVEL_OPERADOR = "OPERADOR";
    public static final String NIVEL_VISUALIZADOR = "VISUALIZADOR";
    
    // Tipos de usuário
    public static final String TIPO_FUNCIONARIO = "FUNCIONARIO";
    public static final String TIPO_CLIENTE = "CLIENTE";
    public static final String TIPO_FORNECEDOR = "FORNECEDOR";
    
    // Permissões
    public static final String PERMISSAO_SIM = "SIM";
    public static final String PERMISSAO_NAO = "NAO";
    
    public Usuario() {
        this.ativo = true;
        // nivelAcesso, tipoUsuario, dataCadastro e permissões são null por padrão
        this.pontosFidelidade = 0;
        this.prazoEntrega = 0;
    }
    
    public Usuario(String nome, String login, String senha, String cargo, String tipoUsuario) {
        this();
        this.nome = nome;
        this.login = login;
        this.senha = senha;
        this.cargo = cargo;
        this.tipoUsuario = tipoUsuario;
        
        // Ajustar permissões baseadas no tipo de usuário
        if (TIPO_CLIENTE.equals(tipoUsuario) || TIPO_FORNECEDOR.equals(tipoUsuario)) {
            this.nivelAcesso = NIVEL_VISUALIZADOR;
            this.permissaoVenda = PERMISSAO_NAO;
            this.permissaoCaixa = PERMISSAO_NAO;
            this.permissaoRelatorio = PERMISSAO_NAO;
        }
    }
    
    // Getters e Setters
    public Integer getId() {
        return id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    
    public String getNome() {
        return nome;
    }
    
    public void setNome(String nome) {
        this.nome = nome;
    }
    
    public String getLogin() {
        return login;
    }
    
    public void setLogin(String login) {
        this.login = login;
    }
    
    public String getSenha() {
        return senha;
    }
    
    public void setSenha(String senha) {
        this.senha = senha;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getCargo() {
        return cargo;
    }
    
    public void setCargo(String cargo) {
        this.cargo = cargo;
    }
    
    public String getNivelAcesso() {
        return nivelAcesso;
    }
    
    public void setNivelAcesso(String nivelAcesso) {
        this.nivelAcesso = nivelAcesso;
        // Atualiza permissões baseadas no nível
        atualizarPermissoesPorNivel();
    }
    
    public LocalDateTime getDataCadastro() {
        return dataCadastro;
    }
    
    public void setDataCadastro(LocalDateTime dataCadastro) {
        this.dataCadastro = dataCadastro;
    }
    
    public LocalDateTime getUltimoAcesso() {
        return ultimoAcesso;
    }
    
    public void setUltimoAcesso(LocalDateTime ultimoAcesso) {
        this.ultimoAcesso = ultimoAcesso;
    }
    
    public Boolean getAtivo() {
        return ativo;
    }
    
    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }
    
    public String getPermissaoVenda() {
        return permissaoVenda;
    }
    
    public void setPermissaoVenda(String permissaoVenda) {
        this.permissaoVenda = permissaoVenda;
    }
    
    public String getPermissaoCaixa() {
        return permissaoCaixa;
    }
    
    public void setPermissaoCaixa(String permissaoCaixa) {
        this.permissaoCaixa = permissaoCaixa;
    }
    
    public String getPermissaoRelatorio() {
        return permissaoRelatorio;
    }
    
    public void setPermissaoRelatorio(String permissaoRelatorio) {
        this.permissaoRelatorio = permissaoRelatorio;
    }
    
    // Getters e Setters para Tipo de Usuário
    public String getTipoUsuario() {
        return tipoUsuario;
    }
    
    public void setTipoUsuario(String tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
    }
    
    // Getters e Setters para Funcionário
    public String getMatricula() {
        return matricula;
    }
    
    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }
    
    public String getDepartamento() {
        return departamento;
    }
    
    public void setDepartamento(String departamento) {
        this.departamento = departamento;
    }
    
    public Double getSalario() {
        return salario;
    }
    
    public void setSalario(Double salario) {
        this.salario = salario;
    }
    
    public LocalDateTime getDataAdmissao() {
        return dataAdmissao;
    }
    
    public void setDataAdmissao(LocalDateTime dataAdmissao) {
        this.dataAdmissao = dataAdmissao;
    }
    
    public String getDataDemissao() {
        return dataDemissao;
    }
    
    public void setDataDemissao(String dataDemissao) {
        this.dataDemissao = dataDemissao;
    }
    
    // Getters e Setters para Cliente
    public String getCpf() {
        return cpf;
    }
    
    public void setCpf(String cpf) {
        this.cpf = cpf;
    }
    
    public String getDataNascimento() {
        return dataNascimento;
    }
    
    public void setDataNascimento(String dataNascimento) {
        this.dataNascimento = dataNascimento;
    }
    
    public String getTelefone() {
        return telefone;
    }
    
    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }
    
    public String getEndereco() {
        return endereco;
    }
    
    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }
    
    public String getBairro() {
        return bairro;
    }
    
    public void setBairro(String bairro) {
        this.bairro = bairro;
    }
    
    public String getCidade() {
        return cidade;
    }
    
    public void setCidade(String cidade) {
        this.cidade = cidade;
    }
    
    public String getEstado() {
        return estado;
    }
    
    public void setEstado(String estado) {
        this.estado = estado;
    }
    
    public String getCep() {
        return cep;
    }
    
    public void setCep(String cep) {
        this.cep = cep;
    }
    
    public String getLimiteCredito() {
        return limiteCredito;
    }
    
    public void setLimiteCredito(String limiteCredito) {
        this.limiteCredito = limiteCredito;
    }
    
    public Integer getPontosFidelidade() {
        return pontosFidelidade;
    }
    
    public void setPontosFidelidade(Integer pontosFidelidade) {
        this.pontosFidelidade = pontosFidelidade;
    }
    
    // Getters e Setters para Fornecedor
    public String getCnpj() {
        return cnpj;
    }
    
    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }
    
    public String getRazaoSocial() {
        return razaoSocial;
    }
    
    public void setRazaoSocial(String razaoSocial) {
        this.razaoSocial = razaoSocial;
    }
    
    public String getNomeFantasia() {
        return nomeFantasia;
    }
    
    public void setNomeFantasia(String nomeFantasia) {
        this.nomeFantasia = nomeFantasia;
    }
    
    public String getInscricaoEstadual() {
        return inscricaoEstadual;
    }
    
    public void setInscricaoEstadual(String inscricaoEstadual) {
        this.inscricaoEstadual = inscricaoEstadual;
    }
    
    public String getTelefoneContato() {
        return telefoneContato;
    }
    
    public void setTelefoneContato(String telefoneContato) {
        this.telefoneContato = telefoneContato;
    }
    
    public String getEmailContato() {
        return emailContato;
    }
    
    public void setEmailContato(String emailContato) {
        this.emailContato = emailContato;
    }
    
    public String getEnderecoFornecedor() {
        return enderecoFornecedor;
    }
    
    public void setEnderecoFornecedor(String enderecoFornecedor) {
        this.enderecoFornecedor = enderecoFornecedor;
    }
    
    public String getCondicoesPagamento() {
        return condicoesPagamento;
    }
    
    public void setCondicoesPagamento(String condicoesPagamento) {
        this.condicoesPagamento = condicoesPagamento;
    }
    
    public Integer getPrazoEntrega() {
        return prazoEntrega;
    }
    
    public void setPrazoEntrega(Integer prazoEntrega) {
        this.prazoEntrega = prazoEntrega;
    }
    
    // Métodos de verificação de permissão
    public boolean temPermissaoVenda() {
        return PERMISSAO_SIM.equals(permissaoVenda) && ativo;
    }
    
    public boolean temPermissaoCaixa() {
        return PERMISSAO_SIM.equals(permissaoCaixa) && ativo;
    }
    
    public boolean temPermissaoRelatorio() {
        return PERMISSAO_SIM.equals(permissaoRelatorio) && ativo;
    }
    
    public boolean temPermissaoAdmin() {
        return NIVEL_ADMIN.equals(nivelAcesso) && ativo;
    }
    
    public boolean temPermissaoGerente() {
        return (NIVEL_ADMIN.equals(nivelAcesso) || NIVEL_GERENTE.equals(nivelAcesso)) && ativo;
    }
    
    // Método para atualizar permissões baseadas no nível de acesso
    private void atualizarPermissoesPorNivel() {
        switch (nivelAcesso) {
            case NIVEL_ADMIN:
                this.permissaoVenda = PERMISSAO_SIM;
                this.permissaoCaixa = PERMISSAO_SIM;
                this.permissaoRelatorio = PERMISSAO_SIM;
                break;
            case NIVEL_GERENTE:
                this.permissaoVenda = PERMISSAO_SIM;
                this.permissaoCaixa = PERMISSAO_SIM;
                this.permissaoRelatorio = PERMISSAO_SIM;
                break;
            case NIVEL_OPERADOR:
                this.permissaoVenda = PERMISSAO_SIM;
                this.permissaoCaixa = PERMISSAO_NAO;
                this.permissaoRelatorio = PERMISSAO_NAO;
                break;
            case NIVEL_VISUALIZADOR:
                this.permissaoVenda = PERMISSAO_NAO;
                this.permissaoCaixa = PERMISSAO_NAO;
                this.permissaoRelatorio = PERMISSAO_SIM;
                break;
            default:
                this.permissaoVenda = PERMISSAO_NAO;
                this.permissaoCaixa = PERMISSAO_NAO;
                this.permissaoRelatorio = PERMISSAO_NAO;
        }
    }
    
    // Método para registrar último acesso
    public void registrarAcesso() {
        this.ultimoAcesso = LocalDateTime.now();
    }
    
    // Método para desativar usuário
    public void desativar() {
        this.ativo = false;
    }
    
    // Método para ativar usuário
    public void ativar() {
        this.ativo = true;
    }
    
    // Método para verificar se o usuário está ativo
    public boolean isAtivo() {
        return Boolean.TRUE.equals(ativo);
    }
    
    // Método para obter descrição do nível de acesso
    public String getDescricaoNivelAcesso() {
        switch (nivelAcesso) {
            case NIVEL_ADMIN:
                return "Administrador";
            case NIVEL_GERENTE:
                return "Gerente";
            case NIVEL_OPERADOR:
                return "Operador de Caixa";
            case NIVEL_VISUALIZADOR:
                return "Visualizador";
            default:
                return "Desconhecido";
        }
    }
    
    // Método para obter descrição do tipo de usuário
    public String getDescricaoTipoUsuario() {
        switch (tipoUsuario) {
            case TIPO_FUNCIONARIO:
                return "Funcionário";
            case TIPO_CLIENTE:
                return "Cliente";
            case TIPO_FORNECEDOR:
                return "Fornecedor";
            default:
                return "Desconhecido";
        }
    }
    
    // Métodos específicos para cada tipo de usuário
    public boolean isFuncionario() {
        return TIPO_FUNCIONARIO.equals(tipoUsuario);
    }
    
    public boolean isCliente() {
        return TIPO_CLIENTE.equals(tipoUsuario);
    }
    
    public boolean isFornecedor() {
        return TIPO_FORNECEDOR.equals(tipoUsuario);
    }
    
    // Métodos específicos para Funcionário
    public boolean isAtivoComoFuncionario() {
        return isFuncionario() && isAtivo() && dataDemissao == null;
    }
    
    public void demitir(String dataDemissao) {
        if (isFuncionario()) {
            this.dataDemissao = dataDemissao;
            this.ativo = false;
        }
    }
    
    // Métodos específicos para Cliente
    public void adicionarPontosFidelidade(Integer pontos) {
        if (isCliente() && pontos != null && pontos > 0) {
            this.pontosFidelidade = (this.pontosFidelidade != null ? this.pontosFidelidade : 0) + pontos;
        }
    }
    
    public void removerPontosFidelidade(Integer pontos) {
        if (isCliente() && pontos != null && pontos > 0) {
            this.pontosFidelidade = (this.pontosFidelidade != null ? this.pontosFidelidade : 0) - pontos;
            if (this.pontosFidelidade < 0) {
                this.pontosFidelidade = 0;
            }
        }
    }
    
    // Métodos específicos para Fornecedor
    public boolean temPrazoEntregaDefinido() {
        return isFornecedor() && prazoEntrega != null && prazoEntrega > 0;
    }
    
    // Método toString para exibição
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Usuario{");
        sb.append("id=").append(id);
        sb.append(", nome='").append(nome).append('\'');
        sb.append(", login='").append(login).append('\'');
        
        // Adicionar tipoUsuario se não for null
        if (tipoUsuario != null) {
            sb.append(", tipoUsuario='").append(tipoUsuario).append('\'');
        }
        
        // Adicionar nivelAcesso se não for null
        if (nivelAcesso != null) {
            sb.append(", nivelAcesso='").append(nivelAcesso).append('\'');
        }
        
        sb.append(", ativo=").append(ativo);
        sb.append('}');
        return sb.toString();
    }
    
    // Método equals para comparação
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        
        Usuario usuario = (Usuario) obj;
        
        return id != null ? id.equals(usuario.id) : usuario.id == null;
    }
    
    // Método hashCode
    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
    
    // Método clone (shallow copy)
    public Usuario clone() {
        Usuario clone = new Usuario();
        clone.id = this.id;
        clone.nome = this.nome;
        clone.login = this.login;
        clone.senha = this.senha;
        clone.email = this.email;
        clone.cargo = this.cargo;
        clone.nivelAcesso = this.nivelAcesso;
        clone.dataCadastro = this.dataCadastro;
        clone.ultimoAcesso = this.ultimoAcesso;
        clone.ativo = this.ativo;
        clone.permissaoVenda = this.permissaoVenda;
        clone.permissaoCaixa = this.permissaoCaixa;
        clone.permissaoRelatorio = this.permissaoRelatorio;
        return clone;
    }
}
