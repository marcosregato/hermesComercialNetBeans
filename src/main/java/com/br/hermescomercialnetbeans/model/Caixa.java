
package com.br.hermescomercialnetbeans.model;

public class Caixa {

    private float valorCaixa;
	private float valor;
	private String tipo;
    private Float impostoEstadual;
    private Float impostoMunicipal;
    private Float impostoFederal;


	public float getValorCaixa() {
		return valorCaixa;
	}

	public void setValorCaixa(float valorCaixa) {
		this.valorCaixa = valorCaixa;
	}

	public float getValor() {
		return valor;
	}

	public void setValor(float valor) {
		this.valor = valor;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

    public Float getImpostoEstadual() {
        return impostoEstadual;
    }

    public void setImpostoEstadual(Float impostoEstadual) {
        this.impostoEstadual = impostoEstadual;
    }

    public Float getImpostoMunicipal() {
        return impostoMunicipal;
    }

    public void setImpostoMunicipal(Float impostoMunicipal) {
        this.impostoMunicipal = impostoMunicipal;
    }

    public Float getImpostoFederal() {
        return impostoFederal;
    }

    public void setImpostoFederal(Float impostoFederal) {
        this.impostoFederal = impostoFederal;
    }
}
