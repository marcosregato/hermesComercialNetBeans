/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.br.hermescomercialnetbeans.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.br.hermescomercialnetbeans.Repository.RepositoryCusto;
import com.br.hermescomercialnetbeans.connectionDB.ConnectionBD;
import com.br.hermescomercialnetbeans.model.Custo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author marcos
 */
public class CustoDao implements RepositoryCusto {

    private ConnectionBD con = new ConnectionBD();
    private static final Logger logger = LogManager.getLogger(CustoDao.class);

    private Custo mapResultSetToCusto(ResultSet rs) throws SQLException {
        Custo custo = new Custo();
        custo.setId(rs.getLong("id"));
        custo.setCustoUnitario(rs.getFloat("custounitario"));
        custo.setCustoTotal(rs.getFloat("custototal"));
        return custo;
    }

    @Override
    public void salvar(Custo custo) {
        String query = "INSERT INTO custo (custounitario, custototal) VALUES (?, ?)";
        try (PreparedStatement ps = con.getConnection("").prepareStatement(query)) {
            ps.setFloat(1, custo.getCustoUnitario());
            ps.setFloat(2, custo.getCustoTotal());
            ps.executeUpdate();
        } catch (Exception e) {
            logger.error("Erro ao salvar custo: " + e.getMessage(), e);
        }
    }

    @Override
    public void remove(String nome) {
        String query = "DELETE FROM custo WHERE id=?";
        try (PreparedStatement ps = con.getConnection("").prepareStatement(query)) {
            ps.setString(1, nome);
            ps.executeUpdate();
        } catch (Exception e) {
            logger.error("Erro ao remover custo: " + e.getMessage(), e);
        }
    }

    @Override
    public void update(Custo custo) {
        String query = "UPDATE custo SET custounitario = ?, custototal = ? WHERE id = ?";
        try (PreparedStatement ps = con.getConnection("").prepareStatement(query)) {
            ps.setFloat(1, custo.getCustoUnitario());
            ps.setFloat(2, custo.getCustoTotal());
            ps.setLong(3, custo.getId());
            ps.executeUpdate();
        } catch (Exception e) {
            logger.error("Erro ao atualizar custo: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Custo> listar() {
        String query = "SELECT * FROM custo";
        List<Custo> lista = new ArrayList<>();
        try (PreparedStatement ps = con.getConnection("").prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(mapResultSetToCusto(rs));
            }
            return lista;
        } catch (Exception e) {
            logger.error("Erro ao listar custos: " + e.getMessage(), e);
        }
        return Collections.emptyList();
    }

    @Override
    public List<Custo> buscar(String nome) {
        String query = "SELECT * FROM custo WHERE id = ?";
        List<Custo> lista = new ArrayList<>();
        try (PreparedStatement ps = con.getConnection("").prepareStatement(query)) {
            ps.setString(1, nome);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapResultSetToCusto(rs));
                }
            }
            return lista;
        } catch (Exception e) {
            logger.error("Erro ao buscar custo: " + e.getMessage(), e);
        }
        return Collections.emptyList();
    }
}
