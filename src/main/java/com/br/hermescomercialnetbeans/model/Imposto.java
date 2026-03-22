
package com.br.hermescomercialnetbeans.model;

public class Imposto {

    private Long id;
    private int id_grupo_imposto;
    private int origem_mercadoria;
    private String cst_icms;
    private String csosn;
    private Float aliquota_icms;
    private Float aliquota_pis;
    private Float aliquota_cofins;
    private Float aliquota_ipi;
    private String cfop;
    private Float reducao_base;
    private Float mva_st;

    public Imposto() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getId_grupo_imposto() {
        return id_grupo_imposto;
    }

    public void setId_grupo_imposto(int id_grupo_imposto) {
        this.id_grupo_imposto = id_grupo_imposto;
    }

    public int getOrigem_mercadoria() {
        return origem_mercadoria;
    }

    public void setOrigem_mercadoria(int origem_mercadoria) {
        this.origem_mercadoria = origem_mercadoria;
    }

    public String getCst_icms() {
        return cst_icms;
    }

    public void setCst_icms(String cst_icms) {
        this.cst_icms = cst_icms;
    }

    public String getCsosn() {
        return csosn;
    }

    public void setCsosn(String csosn) {
        this.csosn = csosn;
    }

    public Float getAliquota_icms() {
        return aliquota_icms;
    }

    public void setAliquota_icms(Float aliquota_icms) {
        this.aliquota_icms = aliquota_icms;
    }

    public Float getAliquota_pis() {
        return aliquota_pis;
    }

    public void setAliquota_pis(Float aliquota_pis) {
        this.aliquota_pis = aliquota_pis;
    }

    public Float getAliquota_cofins() {
        return aliquota_cofins;
    }

    public void setAliquota_cofins(Float aliquota_cofins) {
        this.aliquota_cofins = aliquota_cofins;
    }

    public Float getAliquota_ipi() {
        return aliquota_ipi;
    }

    public void setAliquota_ipi(Float aliquota_ipi) {
        this.aliquota_ipi = aliquota_ipi;
    }

    public String getCfop() {
        return cfop;
    }

    public void setCfop(String cfop) {
        this.cfop = cfop;
    }

    public Float getReducao_base() {
        return reducao_base;
    }

    public void setReducao_base(Float reducao_base) {
        this.reducao_base = reducao_base;
    }

    public Float getMva_st() {
        return mva_st;
    }

    public void setMva_st(Float mva_st) {
        this.mva_st = mva_st;
    }
}
