
package com.br.hermescomercialnetbeans.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.br.hermescomercialnetbeans.Repository.RepositoryAlertaEstoque;
import com.br.hermescomercialnetbeans.connectionDB.ConnectionBD;
import com.br.hermescomercialnetbeans.model.AlertaEstoque;

public class AlertaEstoqueDao implements RepositoryAlertaEstoque{

    private ConnectionBD con = new ConnectionBD();
    private static final Logger logger = LogManager.getLogger(AlertaEstoqueDao.class);

    private AlertaEstoque mapResultSetToAlertaEstoque(ResultSet rs) throws SQLException {
        AlertaEstoque alertaEstoque = new AlertaEstoque();
        alertaEstoque.setTempoEstoque(rs.getString("tempo_estoque"));
        alertaEstoque.setValor(rs.getString("valor"));
        return alertaEstoque;
    }

    @Override
    public void salvar(AlertaEstoque alertaEstoque) {
        String query = "INSERT INTO alerta_estoque (tempo_estoque, valor) VALUES (?, ?)";
        try (PreparedStatement ps = con.getConnection("").prepareStatement(query)) {
            ps.setString(1, alertaEstoque.getTempoEstoque());
            ps.setString(2, alertaEstoque.getValor());
            ps.executeUpdate();
        } catch (Exception e) {
            logger.error("Erro ao salvar alerta de estoque: " + e.getMessage());
        }
    }

    @Override
    public void remove(String nome) {
        String query = "DELETE FROM alerta_estoque WHERE tempo_estoque=?";
        try (PreparedStatement ps = con.getConnection("").prepareStatement(query)) {
            ps.setString(1, nome);
            ps.executeUpdate();
        } catch (Exception e) {
            logger.error("Erro ao remover alerta de estoque: " + e.getMessage());
        }
    }

    @Override
    public void update(AlertaEstoque alertaEstoque) {
        String query = "UPDATE alerta_estoque SET tempo_estoque = ?, valor = ? WHERE id = ?";
        try (PreparedStatement ps = con.getConnection("").prepareStatement(query)) {
            ps.setString(1, alertaEstoque.getTempoEstoque());
            ps.setString(2, alertaEstoque.getValor());
            ps.executeUpdate();
        } catch (Exception e) {
            logger.error("Erro ao atualizar alerta de estoque: " + e.getMessage());
        }
    }

    @Override
    public List<AlertaEstoque> listar() {
        String query = "SELECT * FROM alerta_estoque";
        List<AlertaEstoque> lista = new ArrayList<>();
        try (PreparedStatement ps = con.getConnection("").prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(mapResultSetToAlertaEstoque(rs));
            }
            return lista;
        } catch (Exception e) {
            logger.error("Erro ao listar alertas de estoque: " + e.getMessage());
        }
        return Collections.emptyList();
    }

    @Override
    public List<AlertaEstoque> buscar(String nome) {
        String query = "SELECT * FROM alerta_estoque WHERE tempo_estoque = ?";
        List<AlertaEstoque> lista = new ArrayList<>();
        try (PreparedStatement ps = con.getConnection("").prepareStatement(query)) {
            ps.setString(1, nome);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapResultSetToAlertaEstoque(rs));
                }
            }
            return lista;
        } catch (Exception e) {
            logger.error("Erro ao buscar alerta de estoque: " + e.getMessage());
        }
        return Collections.emptyList();
    }
    
    public String getValidadeEstoque() {
        String dia = null;
        return dia;
    }
}
