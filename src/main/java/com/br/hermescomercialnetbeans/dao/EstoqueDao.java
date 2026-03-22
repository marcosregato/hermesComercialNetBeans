
package com.br.hermescomercialnetbeans.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.br.hermescomercialnetbeans.Repository.RepositoryEstoque;
import com.br.hermescomercialnetbeans.connectionDB.ConnectionBD;
import com.br.hermescomercialnetbeans.model.Estoque;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class EstoqueDao implements RepositoryEstoque{

	private ConnectionBD con = new ConnectionBD();
    private static final Logger logger = LogManager.getLogger(EstoqueDao.class);

	@Override
	public void salvar(Estoque estoque) {
        String query ="INSERT INTO estoque (quantidade, maximo, minimo) VALUES (?, ?, ?)";
        try (PreparedStatement ps = con.getConnection("").prepareStatement(query)) {
			ps.setString(1, estoque.getQuantidade());
			ps.setInt(2, estoque.getMaximo());
			ps.setInt(3, estoque.getMinimo());
			ps.executeUpdate();
		} catch (Exception e) {
            logger.error("Erro ao salvar pessoa: " + e.getMessage());
		}
	}

	@Override
	public void remove(String nome) {
        String query = "DELETE e FROM estoque e INNER JOIN produto p ON p.id = e.fk_produto WHERE p.nome = ?";
        try (PreparedStatement ps = con.getConnection("").prepareStatement(query)) {
            ps.setString(1, nome);
			ps.executeUpdate();
		} catch (Exception e) {
            logger.error("Erro ao salvar pessoa: " + e.getMessage());
		}
	}

	@Override
	public void update(Estoque estoque) {
        String query = "UPDATE estoque SET quantidade = ?, maximo = ?, minimo = ? WHERE id = ?";
        try (PreparedStatement ps = con.getConnection("").prepareStatement(query)) {
			ps.setString(1, estoque.getQuantidade());
			ps.setInt(2, estoque.getMaximo());
			ps.setInt(3, estoque.getMinimo());
			ps.setLong(4, estoque.getId());
			ps.executeUpdate();
		} catch (Exception e) {
            logger.error("Erro ao salvar pessoa: " + e.getMessage());
		}
	}

	@Override
	public List<Estoque> listar() {
        String query ="SELECT * FROM estoque";
        List<Estoque> lista = new ArrayList<>();
        try (PreparedStatement ps = con.getConnection("").prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
			while (rs.next()) {
				Estoque item = new Estoque();
				item.setQuantidade(rs.getString("quantidade"));
				item.setMaximo(rs.getInt("maximo"));
				item.setMinimo(rs.getInt("minimo"));
				lista.add(item);
			}
			return lista;
		} catch (Exception e) {
            logger.error("Erro ao salvar pessoa: " + e.getMessage());
		}
		return Collections.emptyList();
	}

	@Override
	public List<Estoque> buscar(String nome) {
		List<Estoque> lista = new ArrayList<>();
        String query = "SELECT e.* FROM produto p "
                + "INNER JOIN estoque e ON p.id = e.fk_produto WHERE p.codigo = ?";
        try (PreparedStatement ps = con.getConnection("").prepareStatement(query)) {
            ps.setString(1, nome);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Estoque estoque = new Estoque();
                    estoque.setQuantidade(rs.getString("quantidade"));
                    estoque.setMaximo(rs.getInt("maximo"));
                    estoque.setMinimo(rs.getInt("minimo"));
                    lista.add(estoque);
                }
            }
		} catch (Exception e) {
            logger.error("Erro ao salvar pessoa: " + e.getMessage());
		}
		return lista;
	}

    public String getDataCompraEstoque() {
        String query = "SELECT data_compra FROM estoque ORDER BY data_compra DESC LIMIT 1";
        try (PreparedStatement ps = con.getConnection("").prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getString("data_compra");
            }
        } catch (Exception e) {
            logger.error("Erro ao salvar pessoa: " + e.getMessage());
        }
        return null;
    }
}
