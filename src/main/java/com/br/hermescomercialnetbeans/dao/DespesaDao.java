
package com.br.hermescomercialnetbeans.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.br.hermescomercialnetbeans.Repository.RepositoryDespesa;
import com.br.hermescomercialnetbeans.connectionDB.ConnectionBD;
import com.br.hermescomercialnetbeans.model.Despesa;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DespesaDao implements RepositoryDespesa {

    private ConnectionBD con = new ConnectionBD();
    private static final Logger logger = LogManager.getLogger(DespesaDao.class);

    private Despesa mapResultSetToDespesa(ResultSet rs) throws SQLException {
        Despesa despesa = new Despesa();
        despesa.setId(rs.getLong("id"));
        despesa.setTipo(rs.getString("tipo"));
        despesa.setNome(rs.getString("nome"));
        despesa.setValor(rs.getFloat("valor"));
        despesa.setDescricao(rs.getString("descricao"));
        return despesa;
    }

    @Override
    public void salvar(Despesa despesa) {
        String query = "INSERT INTO despesa (tipo, nome, valor, descricao) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = con.getConnection("").prepareStatement(query)) {
            ps.setString(1, despesa.getTipo());
            ps.setString(2, despesa.getNome());
            ps.setFloat(3, despesa.getValor());
            ps.setString(4, despesa.getDescricao());
            ps.executeUpdate();
        } catch (Exception e) {
            logger.error("Erro ao salvar despesa: " + e.getMessage(), e);
        }
    }

    @Override
    public void remove(String nome) {
        String query = "DELETE FROM despesa WHERE nome=?";
        try (PreparedStatement ps = con.getConnection("").prepareStatement(query)) {
            ps.setString(1, nome);
            ps.executeUpdate();
        } catch (Exception e) {
            logger.error("Erro ao remover despesa: " + e.getMessage(), e);
        }
    }

    @Override
    public void update(Despesa despesa) {
        String query = "UPDATE despesa SET tipo = ?, nome = ?, valor = ?, descricao = ? WHERE id = ?";
        try (PreparedStatement ps = con.getConnection("").prepareStatement(query)) {
            ps.setString(1, despesa.getTipo());
            ps.setString(2, despesa.getNome());
            ps.setFloat(3, despesa.getValor());
            ps.setString(4, despesa.getDescricao());
            ps.setLong(5, despesa.getId());
            ps.executeUpdate();
        } catch (Exception e) {
            logger.error("Erro ao atualizar despesa: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Despesa> listar() {
        String query = "SELECT * FROM despesa";
        List<Despesa> lista = new ArrayList<>();
        try (PreparedStatement ps = con.getConnection("").prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(mapResultSetToDespesa(rs));
            }
            return lista;
        } catch (Exception e) {
            logger.error("Erro ao listar despesas: " + e.getMessage(), e);
        }
        return Collections.emptyList();
    }

    @Override
    public List<Despesa> buscar(String nome) {
        String query = "SELECT * FROM despesa WHERE nome LIKE ?";
        List<Despesa> lista = new ArrayList<>();
        try (PreparedStatement ps = con.getConnection("").prepareStatement(query)) {
            ps.setString(1, "%" + nome + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapResultSetToDespesa(rs));
                }
            }
            return lista;
        } catch (Exception e) {
            logger.error("Erro ao buscar despesa: " + e.getMessage(), e);
        }
        return Collections.emptyList();
    }
}
