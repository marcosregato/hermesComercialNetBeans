package com.br.hermescomercialnetbeans.model;

public class ItemVenda {
    
    private Long id;
    private Long vendaId;
    private Long produtoId;
    private String produtoCodigo;
    private String produtoDescricao;
    private int quantidade;
    private double valorUnitario;
    private double subtotal;
    private double desconto;
    
    public ItemVenda() {
    }
    
    public ItemVenda(Long vendaId, Long produtoId, String produtoCodigo, 
                     String produtoDescricao, int quantidade, double valorUnitario) {
        this.vendaId = vendaId;
        this.produtoId = produtoId;
        this.produtoCodigo = produtoCodigo;
        this.produtoDescricao = produtoDescricao;
        this.quantidade = quantidade;
        this.valorUnitario = valorUnitario;
        this.subtotal = quantidade * valorUnitario;
        this.desconto = 0.0;
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

    public Long getProdutoId() {
        return produtoId;
    }

    public void setProdutoId(Long produtoId) {
        this.produtoId = produtoId;
    }

    public String getProdutoCodigo() {
        return produtoCodigo;
    }

    public void setProdutoCodigo(String produtoCodigo) {
        this.produtoCodigo = produtoCodigo;
    }

    public String getProdutoDescricao() {
        return produtoDescricao;
    }

    public void setProdutoDescricao(String produtoDescricao) {
        this.produtoDescricao = produtoDescricao;
    }
    
    public String getCodigo() {
        return produtoCodigo;
    }
    
    public void setCodigo(String codigo) {
        this.produtoCodigo = codigo;
    }
    
    public String getDescricao() {
        return produtoDescricao;
    }
    
    public void setDescricao(String descricao) {
        this.produtoDescricao = descricao;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
        atualizarSubtotal();
    }

    public double getValorUnitario() {
        return valorUnitario;
    }

    public void setValorUnitario(double valorUnitario) {
        this.valorUnitario = valorUnitario;
        atualizarSubtotal();
    }

    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }

    public double getDesconto() {
        return desconto;
    }

    public void setDesconto(double desconto) {
        this.desconto = desconto;
        atualizarSubtotal();
    }
    
    public double getValorTotal() {
        return subtotal - desconto;
    }
    
    public void setValorTotal(double valorTotal) {
        this.subtotal = valorTotal + this.desconto;
    }
    
    private void atualizarSubtotal() {
        this.subtotal = (quantidade * valorUnitario) - desconto;
    }
    
    public double getSubtotalComDesconto() {
        return subtotal - desconto;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        ItemVenda itemVenda = (ItemVenda) obj;
        return id != null && id.equals(itemVenda.id);
    }
    
    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
    
    @Override
    public String toString() {
        return "ItemVenda{" +
                "id=" + id +
                ", produtoCodigo='" + produtoCodigo + '\'' +
                ", produtoDescricao='" + produtoDescricao + '\'' +
                ", quantidade=" + quantidade +
                ", valorUnitario=" + valorUnitario +
                ", subtotal=" + subtotal +
                '}';
    }
}
