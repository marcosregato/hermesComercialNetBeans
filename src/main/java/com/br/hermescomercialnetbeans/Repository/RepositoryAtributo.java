package com.br.hermescomercialnetbeans.Repository;

import com.br.hermescomercialnetbeans.model.AlertaEstoque;
import com.br.hermescomercialnetbeans.model.Atributo;

import java.util.List;

public interface RepositoryAtributo {

    void salvar(Atributo atributo);

    void remove(String nome);

    void update(Atributo atributo);

    List<Atributo> listar();

    List<Atributo> buscar(String nome);
}
