/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.br.hermescomercialnetbeans.model;

/**
 *
 * @author marcos
 */
public class Custo {
    
    private long id;
    private float custoUnitario;
    private float custoTotal;
    private Long fk_fornecedor;
    
    public Long getFk_fornecedor() {
        return fk_fornecedor;
    }

    public void setFk_fornecedor(Long fk_fornecedor) {
        this.fk_fornecedor = fk_fornecedor;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setCustoUnitario(float custounitario) {
        this.custoUnitario = custounitario;
    }

    public void setCustoTotal(float custototal) {
        this.custoTotal = custototal;
    }

    public float getCustoUnitario() {
        return custoUnitario;
    }

    public float getCustoTotal() {
        return custoTotal;
    }

    public long getId() {
        return id;
    }
}
