package com.br.hermescomercialnetbeans.model;

import java.time.LocalDateTime;

public class Pagamento {
    
    public enum FormaPagamento {
        DINHEIRO("Dinheiro"),
        CARTAO_CREDITO("Cartão de Crédito"),
        CARTAO_DEBITO("Cartão de Débito"),
        PIX("PIX"),
        CHEQUE("Cheque"),
        CREDIARIO("Crediário"),
        VALE_ALIMENTACAO("Vale Alimentação"),
        VALE_REFEICAO("Vale Refeição"),
        OUTROS("Outros");
        
        private final String descricao;
        
        FormaPagamento(String descricao) {
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
    
    public enum StatusPagamento {
        PENDENTE("Pendente"),
        PAGO("Pago"),
        CANCELADO("Cancelado"),
        ESTORNADO("Estornado");
        
        private final String descricao;
        
        StatusPagamento(String descricao) {
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
    private Long vendaId;
    private FormaPagamento formaPagamento;
    private double valor;
    private double valorRecebido;
    private double troco;
    private String numeroParcelas;
    private String bandeiraCartao;
    private String autorizacaoCartao;
    private String chavePix;
    private String numeroCheque;
    private String bancoCheque;
    private LocalDateTime dataPagamento;
    private StatusPagamento status;
    private String observacoes;
    
    public Pagamento() {
        this.dataPagamento = LocalDateTime.now();
        this.status = StatusPagamento.PENDENTE;
        this.valorRecebido = 0.0;
        this.troco = 0.0;
    }
    
    public Pagamento(Long vendaId, FormaPagamento formaPagamento, double valor) {
        this();
        this.vendaId = vendaId;
        this.formaPagamento = formaPagamento;
        this.valor = valor;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getVendaId() {
        return vendaId;
    }

    public void setVendaId(Long vendaId) {
        this.vendaId = vendaId;
    }

    public FormaPagamento getFormaPagamento() {
        return formaPagamento;
    }

    public void setFormaPagamento(FormaPagamento formaPagamento) {
        this.formaPagamento = formaPagamento;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
        calcularTroco();
    }

    public double getValorRecebido() {
        return valorRecebido;
    }

    public void setValorRecebido(double valorRecebido) {
        this.valorRecebido = valorRecebido;
        calcularTroco();
    }

    public double getTroco() {
        return troco;
    }

    public void setTroco(double troco) {
        this.troco = troco;
    }
    
    private void calcularTroco() {
        if (formaPagamento == FormaPagamento.DINHEIRO && valorRecebido > valor) {
            this.troco = valorRecebido - valor;
        } else {
            this.troco = 0.0;
        }
    }

    public String getNumeroParcelas() {
        return numeroParcelas;
    }

    public void setNumeroParcelas(String numeroParcelas) {
        this.numeroParcelas = numeroParcelas;
    }

    public String getBandeiraCartao() {
        return bandeiraCartao;
    }

    public void setBandeiraCartao(String bandeiraCartao) {
        this.bandeiraCartao = bandeiraCartao;
    }

    public String getAutorizacaoCartao() {
        return autorizacaoCartao;
    }

    public void setAutorizacaoCartao(String autorizacaoCartao) {
        this.autorizacaoCartao = autorizacaoCartao;
    }

    public String getChavePix() {
        return chavePix;
    }

    public void setChavePix(String chavePix) {
        this.chavePix = chavePix;
    }

    public String getNumeroCheque() {
        return numeroCheque;
    }

    public void setNumeroCheque(String numeroCheque) {
        this.numeroCheque = numeroCheque;
    }

    public String getBancoCheque() {
        return bancoCheque;
    }

    public void setBancoCheque(String bancoCheque) {
        this.bancoCheque = bancoCheque;
    }

    public LocalDateTime getDataPagamento() {
        return dataPagamento;
    }

    public void setDataPagamento(LocalDateTime dataPagamento) {
        this.dataPagamento = dataPagamento;
    }

    public StatusPagamento getStatus() {
        return status;
    }

    public void setStatus(StatusPagamento status) {
        this.status = status;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }
    
    public boolean isPago() {
        return status == StatusPagamento.PAGO;
    }
    
    public boolean precisaTroco() {
        return formaPagamento == FormaPagamento.DINHEIRO && valorRecebido > valor;
    }
    
    public void confirmarPagamento() {
        this.status = StatusPagamento.PAGO;
        this.dataPagamento = LocalDateTime.now();
    }
    
    public void cancelar() {
        this.status = StatusPagamento.CANCELADO;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Pagamento pagamento = (Pagamento) obj;
        return id != null && id.equals(pagamento.id);
    }
    
    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
    
    @Override
    public String toString() {
        return "Pagamento{" +
                "id=" + id +
                ", formaPagamento=" + formaPagamento +
                ", valor=" + valor +
                ", status=" + status +
                '}';
    }
}
