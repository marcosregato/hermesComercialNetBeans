
package com.br.hermescomercialnetbeans.dao;

import com.br.hermescomercialnetbeans.connectionDB.ConnectionBD;
import com.br.hermescomercialnetbeans.model.GrupoImposto;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GrupoImpostoDao {

    private ConnectionBD con = new ConnectionBD();
    private static final Logger logger = LogManager.getLogger(GrupoImpostoDao.class);

    private GrupoImposto mapResultSetToGrupoImposto(ResultSet rs) throws SQLException {
        GrupoImposto grupoImposto = new GrupoImposto();
        grupoImposto.setId(rs.getLong("id"));
        grupoImposto.setNome_grupo(rs.getString("nome_grupo"));
        grupoImposto.setDescricao(rs.getString("descricao"));
        grupoImposto.setAtivo(rs.getBoolean("ativo"));
        grupoImposto.setData_criacao(rs.getString("data_criacao"));
        return grupoImposto;
    }
    
    public void salvar(GrupoImposto grupoImposto){
        String query = "INSERT INTO grupo_imposto(nome_grupo,descricao,ativo,data_criacao) VALUES(?,?,?,?)";
        try (PreparedStatement ps = con.getConnection("").prepareStatement(query)) {
            ps.setString(1, grupoImposto.getNome_grupo());
            ps.setString(2, grupoImposto.getDescricao());
            ps.setBoolean(3, grupoImposto.getAtivo());
            ps.setString(4, grupoImposto.getData_criacao());
            ps.executeUpdate();
        } catch (Exception e) {
            logger.error("Erro ao salvar grupo de imposto: " + e.getMessage());
        }
    }
    
    public void update(GrupoImposto grupoImposto){
        String query = "UPDATE grupo_imposto SET nome_grupo=?, descricao=?, ativo=?, data_criacao=? WHERE id=?";
        try (PreparedStatement ps = con.getConnection("").prepareStatement(query)){
            ps.setString(1, grupoImposto.getNome_grupo());
            ps.setString(2, grupoImposto.getDescricao());
            ps.setBoolean(3, grupoImposto.getAtivo());
            ps.setString(4, grupoImposto.getData_criacao());
            ps.setLong(5, grupoImposto.getId());
            ps.executeUpdate();
        } catch (Exception e) {
            logger.error("Erro ao atualizar grupo de imposto: " + e.getMessage());
        }
    }

    public void remove(Long id) {
        String query = "DELETE FROM grupo_imposto WHERE id=?";
        try (PreparedStatement ps = con.getConnection("").prepareStatement(query)) {
            ps.setLong(1, id);
            ps.executeUpdate();
        } catch (Exception e) {
            logger.error("Erro ao remover grupo de imposto: " + e.getMessage());
        }
    }

    public List<GrupoImposto> listar() {
        String query = "SELECT * FROM grupo_imposto";
        List<GrupoImposto> lista = new ArrayList<>();
        try (PreparedStatement ps = con.getConnection("").prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(mapResultSetToGrupoImposto(rs));
            }
        } catch (Exception e) {
            logger.error("Erro ao listar grupos de imposto: " + e.getMessage());
        }
        return lista;
    }

    public List<GrupoImposto> buscar(Long id) {
        String query = "SELECT * FROM grupo_imposto WHERE id = ?";
        List<GrupoImposto> lista = new ArrayList<>();
        try (PreparedStatement ps = con.getConnection("").prepareStatement(query)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapResultSetToGrupoImposto(rs));
                }
            }
        } catch (Exception e) {
            logger.error("Erro ao buscar grupo de imposto: " + e.getMessage());
        }
        return lista;
    }
}
