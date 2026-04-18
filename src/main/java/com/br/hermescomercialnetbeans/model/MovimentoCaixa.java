package com.br.hermescomercialnetbeans.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class MovimentoCaixa {
    
    public enum TipoMovimento {
        ABERTURA("Abertura"),
        FECHAMENTO("Fechamento"),
        SANGRIA("Sangria"),
        SUPRIMENTO("Suprimento"),
        VENDA("Venda"),
        CANCELAMENTO("Cancelamento");
        
        private final String descricao;
        
        TipoMovimento(String descricao) {
            this.descricao = descricao;
        }
        
        public String getDescricao() {
            return descricao;
        }
        
        @Override
        public String toString() {
            return descricao;
        }
    }
    
    private Long id;
    private LocalDateTime dataHora;
    private Long usuarioId;
    private String usuarioNome;
    private TipoMovimento tipoMovimento;
    private double valor;
    private double saldoAnterior;
    private double saldoAtual;
    private String descricao;
    private String observacoes;
    
    public MovimentoCaixa() {
        this.dataHora = LocalDateTime.now();
    }
    
    public MovimentoCaixa(Long usuarioId, String usuarioNome, TipoMovimento tipoMovimento, 
                         double valor, double saldoAnterior, String descricao) {
        this();
        this.usuarioId = usuarioId;
        this.usuarioNome = usuarioNome;
        this.tipoMovimento = tipoMovimento;
        this.valor = valor;
        this.saldoAnterior = saldoAnterior;
        this.calcularSaldoAtual();
        this.descricao = descricao;
    }
    
    private void calcularSaldoAtual() {
        switch (tipoMovimento) {
            case ABERTURA:
            case SUPRIMENTO:
            case VENDA:
                this.saldoAtual = saldoAnterior + valor;
                break;
            case FECHAMENTO:
            case SANGRIA:
            case CANCELAMENTO:
                this.saldoAtual = saldoAnterior - valor;
                break;
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public void setDataHora(LocalDateTime dataHora) {
        this.dataHora = dataHora;
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }

    public String getUsuarioNome() {
        return usuarioNome;
    }

    public void setUsuarioNome(String usuarioNome) {
        this.usuarioNome = usuarioNome;
    }

    public TipoMovimento getTipoMovimento() {
        return tipoMovimento;
    }

    public void setTipoMovimento(TipoMovimento tipoMovimento) {
        this.tipoMovimento = tipoMovimento;
        calcularSaldoAtual();
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
        calcularSaldoAtual();
    }

    public double getSaldoAnterior() {
        return saldoAnterior;
    }

    public void setSaldoAnterior(double saldoAnterior) {
        this.saldoAnterior = saldoAnterior;
        calcularSaldoAtual();
    }

    public double getSaldoAtual() {
        return saldoAtual;
    }

    public void setSaldoAtual(double saldoAtual) {
        this.saldoAtual = saldoAtual;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }
    
    public void setObservacao(String observacao) {
        this.observacoes = observacao;
    }
    
    public String getObservacao() {
        return observacoes;
    }
    
    public String getDataHoraFormatada() {
        return dataHora.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
    }
    
    public boolean isEntrada() {
        return tipoMovimento == TipoMovimento.ABERTURA || 
               tipoMovimento == TipoMovimento.SUPRIMENTO || 
               tipoMovimento == TipoMovimento.VENDA;
    }
    
    public boolean isSaida() {
        return tipoMovimento == TipoMovimento.FECHAMENTO || 
               tipoMovimento == TipoMovimento.SANGRIA || 
               tipoMovimento == TipoMovimento.CANCELAMENTO;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        MovimentoCaixa movimentoCaixa = (MovimentoCaixa) obj;
        return id != null && id.equals(movimentoCaixa.id);
    }
    
    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
    
    @Override
    public String toString() {
        return "MovimentoCaixa{" +
                "id=" + id +
                ", tipoMovimento=" + tipoMovimento +
                ", valor=" + valor +
                ", descricao='" + descricao + '\'' +
                ", dataHora=" + dataHora +
                '}';
    }
}
