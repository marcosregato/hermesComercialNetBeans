
package com.br.hermescomercialnetbeans.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.br.hermescomercialnetbeans.Repository.RepositoryAtributo;
import com.br.hermescomercialnetbeans.connectionDB.ConnectionBD;
import com.br.hermescomercialnetbeans.model.Atributo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AtributoDao implements RepositoryAtributo {

    private ConnectionBD con = new ConnectionBD();
    private static final Logger logger = LogManager.getLogger(AtributoDao.class);

    private Atributo mapResultSetToAtributo(ResultSet rs) throws SQLException {
        Atributo atributo = new Atributo();
        atributo.setImpostoEstadual(rs.getFloat("imposto_estadual"));
        atributo.setImpostoMunicipal(rs.getFloat("imposto_municipal"));
        atributo.setImpostoFederal(rs.getFloat("imposto_federal"));
        return atributo;
    }

    @Override
    public void salvar(Atributo atributo) {
        String query = "INSERT INTO atributo (imposto_estadual, imposto_municipal, imposto_federal) VALUES (?, ?, ?)";
        try (PreparedStatement ps = con.getConnection("").prepareStatement(query)) {
            ps.setFloat(1, atributo.getImpostoEstadual());
            ps.setFloat(2, atributo.getImpostoMunicipal());
            ps.setFloat(3, atributo.getImpostoFederal());
            ps.executeUpdate();
        } catch (Exception e) {
            logger.error("Erro ao salvar pessoa: " + e.getMessage());
        }
    }

    @Override
    public void remove(String nome) {
        String query = "DELETE FROM atributo WHERE nome=?";
        try (PreparedStatement ps = con.getConnection("").prepareStatement(query)) {
            ps.setString(1, nome);
            ps.executeUpdate();
        } catch (Exception e) {
            logger.error("Erro ao remove pessoa: " + e.getMessage());
        }
    }

    @Override
    public void update(Atributo atributo) {
        String query = "UPDATE atributo SET imposto_estadual = ?, imposto_municipal = ?, imposto_federal = ?";
        try (PreparedStatement ps = con.getConnection("").prepareStatement(query)) {
            ps.setFloat(1, atributo.getImpostoEstadual());
            ps.setFloat(2, atributo.getImpostoMunicipal());
            ps.setFloat(3, atributo.getImpostoFederal());
            ps.executeUpdate();
        } catch (Exception e) {
            logger.error("Erro ao update pessoa: " + e.getMessage());
        }
    }

    @Override
    public List<Atributo> listar() {
        String query = "SELECT * FROM atributo";
        List<Atributo> lista = new ArrayList<>();
        try (PreparedStatement ps = con.getConnection("").prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(mapResultSetToAtributo(rs));
            }
            return lista;
        } catch (Exception e) {
            logger.error("Erro ao listar pessoa: " + e.getMessage());
        }
        return Collections.emptyList();
    }

    @Override
    public List<Atributo> buscar(String nome) {
        String query = "SELECT * FROM atributo WHERE nome LIKE ?";
        List<Atributo> lista = new ArrayList<>();
        try (PreparedStatement ps = con.getConnection("").prepareStatement(query)) {
            ps.setString(1, "%" + nome + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapResultSetToAtributo(rs));
                }
            }
            return lista;
        } catch (Exception e) {
            logger.error("Erro ao buscar pessoa: " + e.getMessage());
        }
        return Collections.emptyList();
    }
}
