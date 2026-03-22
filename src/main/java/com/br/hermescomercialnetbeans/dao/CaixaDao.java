
package com.br.hermescomercialnetbeans.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.br.hermescomercialnetbeans.connectionDB.ConnectionBD;
import com.br.hermescomercialnetbeans.model.Caixa;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class CaixaDao {

    private ConnectionBD con = new ConnectionBD();
    private static final Logger logger = LogManager.getLogger(CaixaDao.class);

    private Caixa mapResultSetToCaixa(ResultSet rs) throws SQLException {
        Caixa caixa = new Caixa();
        caixa.setTipo(rs.getString("tipo"));
        caixa.setValor(rs.getFloat("valor"));
        caixa.setValorCaixa(rs.getFloat("valor_caixa"));
        caixa.setImpostoEstadual(rs.getFloat("imposto_estadual"));
        caixa.setImpostoMunicipal(rs.getFloat("imposto_municipal"));
        caixa.setImpostoFederal(rs.getFloat("imposto_federal"));
       
        return caixa;
    }


    public void salvar(Caixa caixa) {
        String query = "INSERT INTO caixa (tipo, valor, valor_caixa, imposto_estadual, imposto_municipal, imposto_federal) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = con.getConnection("").prepareStatement(query)) {
            ps.setString(1, caixa.getTipo());
            ps.setFloat(2, caixa.getValor());
            ps.setFloat(3, caixa.getValorCaixa());
            ps.setFloat(4, caixa.getImpostoEstadual());
            ps.setFloat(5, caixa.getImpostoMunicipal());
            ps.setFloat(6, caixa.getImpostoFederal());
            ps.executeUpdate();
        } catch (Exception e) {
            logger.error("Erro ao salvar caixa: " + e.getMessage());
        }
    }
    
    public void delete(String tipo) {
        String query = "DELETE FROM caixa WHERE tipo=?";
        try (PreparedStatement ps = con.getConnection("").prepareStatement(query)) {
            ps.setString(1, tipo);
            ps.executeUpdate();
        } catch (Exception e) {
            logger.error("Erro ao deletar caixa: " + e.getMessage());
        }
    }
    
    public void update(Caixa caixa) {
        String query = "UPDATE caixa SET valor = ?, valor_caixa = ?, imposto_estadual = ?, imposto_municipal = ?, imposto_federal = ? WHERE tipo = ?";
        try (PreparedStatement ps = con.getConnection("").prepareStatement(query)) {
            ps.setFloat(1, caixa.getValor());
            ps.setFloat(2, caixa.getValorCaixa());
            ps.setFloat(3, caixa.getImpostoEstadual());
            ps.setFloat(4, caixa.getImpostoMunicipal());
            ps.setFloat(5, caixa.getImpostoFederal());
            ps.setString(6, caixa.getTipo());
            ps.executeUpdate();
        } catch (Exception e) {
            logger.error("Erro ao atualizar caixa: " + e.getMessage());
        }
    }
    
    public List<Caixa> listar() {
        String query = "SELECT * FROM caixa";
        List<Caixa> lista = new ArrayList<>();
        try (PreparedStatement ps = con.getConnection("").prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(mapResultSetToCaixa(rs));
            }
            return lista;
        } catch (Exception e) {
            logger.error("Erro ao listar caixa: " + e.getMessage());
        }
        return Collections.emptyList();
    }
    
    public List<Caixa> buscar(String tipo) {
        String query = "SELECT * FROM caixa WHERE tipo LIKE ?";
        List<Caixa> lista = new ArrayList<>();
        try (PreparedStatement ps = con.getConnection("").prepareStatement(query)) {
            ps.setString(1, "%" + tipo + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapResultSetToCaixa(rs));
                }
            }
            return lista;
        } catch (Exception e) {
            logger.error("Erro ao buscar caixa: " + e.getMessage());
        }
        return Collections.emptyList();
    }
}
