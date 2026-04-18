package com.br.hermescomercialnetbeans.model;

import java.time.LocalDateTime;
import java.util.List;

public class Venda {
    
    private Long id;
    private String codigo;
    private LocalDateTime dataHora;
    private Long clienteId;
    private String clienteNome;
    private Long usuarioId;
    private String usuarioNome;
    private double valorTotal;
    private double valorDesconto;
    private double valorAcrescimo;
    private double valorFinal;
    private String status;
    private String tipoPagamento;
    private String observacoes;
    private boolean cancelada;
    private List<ItemVenda> itens;
    
    public Venda() {
        this.dataHora = LocalDateTime.now();
        this.status = "ABERTA";
        this.valorTotal = 0.0;
        this.valorDesconto = 0.0;
        this.valorAcrescimo = 0.0;
        this.valorFinal = 0.0;
        this.cancelada = false;
    }
    
    public Venda(Long usuarioId, String usuarioNome, Long clienteId, String clienteNome) {
        this();
        this.usuarioId = usuarioId;
        this.usuarioNome = usuarioNome;
        this.clienteId = clienteId;
        this.clienteNome = clienteNome;
        this.codigo = gerarCodigoVenda();
    }
    
    private String gerarCodigoVenda() {
        return "VND-" + System.currentTimeMillis();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public void setDataHora(LocalDateTime dataHora) {
        this.dataHora = dataHora;
    }

    public Long getClienteId() {
        return clienteId;
    }

    public void setClienteId(Long clienteId) {
        this.clienteId = clienteId;
    }

    public String getClienteNome() {
        return clienteNome;
    }

    public void setClienteNome(String clienteNome) {
        this.clienteNome = clienteNome;
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

    public double getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(double valorTotal) {
        this.valorTotal = valorTotal;
        atualizarValorFinal();
    }

    public double getValorDesconto() {
        return valorDesconto;
    }

    public void setValorDesconto(double valorDesconto) {
        this.valorDesconto = valorDesconto;
        atualizarValorFinal();
    }

    public double getValorAcrescimo() {
        return valorAcrescimo;
    }

    public void setValorAcrescimo(double valorAcrescimo) {
        this.valorAcrescimo = valorAcrescimo;
        atualizarValorFinal();
    }

    public double getValorFinal() {
        return valorFinal;
    }

    public void setValorFinal(double valorFinal) {
        this.valorFinal = valorFinal;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTipoPagamento() {
        return tipoPagamento;
    }

    public void setTipoPagamento(String tipoPagamento) {
        this.tipoPagamento = tipoPagamento;
    }
    
    public String getFormaPagamento() {
        return tipoPagamento;
    }
    
    public void setFormaPagamento(String formaPagamento) {
        this.tipoPagamento = formaPagamento;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }

    public boolean isCancelada() {
        return cancelada;
    }

    public void setCancelada(boolean cancelada) {
        this.cancelada = cancelada;
        if (cancelada) {
            this.status = "CANCELADA";
        }
    }

    public List<ItemVenda> getItens() {
        return itens;
    }

    public void setItens(List<ItemVenda> itens) {
        this.itens = itens;
        calcularTotal();
    }
    
    private void atualizarValorFinal() {
        this.valorFinal = valorTotal - valorDesconto + valorAcrescimo;
    }
    
    private void calcularTotal() {
        if (itens != null) {
            this.valorTotal = itens.stream()
                .mapToDouble(ItemVenda::getSubtotalComDesconto)
                .sum();
            atualizarValorFinal();
        }
    }
    
    public void adicionarItem(ItemVenda item) {
        if (itens != null) {
            itens.add(item);
            calcularTotal();
        }
    }
    
    public void removerItem(int index) {
        if (itens != null && index >= 0 && index < itens.size()) {
            itens.remove(index);
            calcularTotal();
        }
    }
    
    public int getTotalItens() {
        return itens != null ? itens.size() : 0;
    }
    
    public int getTotalQuantidade() {
        return itens != null ? itens.stream()
            .mapToInt(ItemVenda::getQuantidade)
            .sum() : 0;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Venda venda = (Venda) obj;
        return id != null && id.equals(venda.id);
    }
    
    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
    
    @Override
    public String toString() {
        return "Venda{" +
                "id=" + id +
                ", codigo='" + codigo + '\'' +
                ", tipoPagamento='" + tipoPagamento + '\'' +
                ", valorFinal=" + valorFinal +
                ", status='" + status + '\'' +
                '}';
    }
}
