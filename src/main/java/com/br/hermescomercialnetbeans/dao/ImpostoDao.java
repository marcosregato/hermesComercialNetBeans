
package com.br.hermescomercialnetbeans.dao;

import com.br.hermescomercialnetbeans.connectionDB.ConnectionBD;
import com.br.hermescomercialnetbeans.model.Imposto;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ImpostoDao {

    private ConnectionBD con = new ConnectionBD();
    private static final Logger logger = LogManager.getLogger(ImpostoDao.class);
    
    private Imposto mapResultSetToImposto(ResultSet rs) throws SQLException {
        Imposto imposto = new Imposto();
        imposto.setId(rs.getLong("id"));
        imposto.setOrigem_mercadoria(rs.getInt("origem_mercadoria"));
        imposto.setCst_icms(rs.getString("cst_icms"));
        imposto.setCsosn(rs.getString("csosn"));
        imposto.setAliquota_icms(rs.getFloat("aliquota_icms"));
        imposto.setAliquota_pis(rs.getFloat("aliquota_pis"));
        imposto.setAliquota_cofins(rs.getFloat("aliquota_cofins"));
        imposto.setAliquota_ipi(rs.getFloat("aliquota_ipi"));
        return imposto;
    }

    public void salvar(Imposto imposto){
        String query = "INSERT INTO imposto (origem_mercadoria, cst_icms, csosn, aliquota_icms, aliquota_pis, aliquota_cofins, aliquota_ipi) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = con.getConnection("").prepareStatement(query)) {
            ps.setInt(1, imposto.getOrigem_mercadoria());
            ps.setString(2, imposto.getCst_icms());
            ps.setString(3, imposto.getCsosn());
            ps.setFloat(4, imposto.getAliquota_icms());
            ps.setFloat(5, imposto.getAliquota_pis());
            ps.setFloat(6, imposto.getAliquota_cofins());
            ps.setFloat(7, imposto.getAliquota_ipi());
            ps.executeUpdate();
        } catch (Exception e) {
            logger.error("Erro ao salvar imposto: " + e.getMessage());
        }
    }

    public void update(Imposto imposto) {
        String query = "UPDATE imposto SET origem_mercadoria=?, cst_icms=?, csosn=?, aliquota_icms=?, aliquota_pis=?, aliquota_cofins=?, aliquota_ipi=? WHERE id=?";
        try (PreparedStatement ps = con.getConnection("").prepareStatement(query)) {
            ps.setInt(1, imposto.getOrigem_mercadoria());
            ps.setString(2, imposto.getCst_icms());
            ps.setString(3, imposto.getCsosn());
            ps.setFloat(4, imposto.getAliquota_icms());
            ps.setFloat(5, imposto.getAliquota_pis());
            ps.setFloat(6, imposto.getAliquota_cofins());
            ps.setFloat(7, imposto.getAliquota_ipi());
            ps.setLong(8, imposto.getId());
            ps.executeUpdate();
        } catch (Exception e) {
            logger.error("Erro ao atualizar imposto: " + e.getMessage());
        }
    }

    public void remove(Long id) {
        String query = "DELETE FROM imposto WHERE id=?";
        try (PreparedStatement ps = con.getConnection("").prepareStatement(query)) {
            ps.setLong(1, id);
            ps.executeUpdate();
        } catch (Exception e) {
            logger.error("Erro ao remover imposto: " + e.getMessage());
        }
    }

    public List<Imposto> listar() {
        String query = "SELECT * FROM imposto";
        List<Imposto> lista = new ArrayList<>();
        try (PreparedStatement ps = con.getConnection("").prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(mapResultSetToImposto(rs));
            }
        } catch (Exception e) {
            logger.error("Erro ao listar impostos: " + e.getMessage());
        }
        return lista;
    }

    public List<Imposto> buscar(Long id) {
        String query = "SELECT * FROM imposto WHERE id = ?";
        List<Imposto> lista = new ArrayList<>();
        try (PreparedStatement ps = con.getConnection("").prepareStatement(query)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapResultSetToImposto(rs));
                }
            }
        } catch (Exception e) {
            logger.error("Erro ao buscar imposto: " + e.getMessage());
        }
        return lista;
    }
}
