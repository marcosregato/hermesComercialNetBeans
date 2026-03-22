/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.br.hermescomercialnetbeans.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.List;

import com.br.hermescomercialnetbeans.Repository.RepositoryProduto;
import com.br.hermescomercialnetbeans.connectionDB.ConnectionBD;
import com.br.hermescomercialnetbeans.model.Produto;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
 *
 * @author marcos
 */
public class ProdutoDao implements RepositoryProduto {

    private ConnectionBD con = new ConnectionBD();
    private static final Logger logger = LogManager.getLogger(ProdutoDao.class);

    private Produto mapResultSetToProduto(ResultSet rs) throws SQLException {
        Produto produto = new Produto();
        produto.setNome(rs.getString("nome"));
        produto.setCategoria(rs.getString("categoria"));
        produto.setCodigo(rs.getString("codigo"));
        produto.setMarca(rs.getString("marca"));
        produto.setSubCategoria(rs.getString("subCategoria"));
        produto.setDataCompra(rs.getString("dataCompra"));
        return produto;
    }


	@Override
	public void salvar(Produto produto) {
        String query ="INSERT INTO produto (nome, categoria, subCategoria, codigo, marca,dataCompra) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = con.getConnection("").prepareStatement(query)) {


			ps.setString(1, produto.getNome());
			ps.setString(2, produto.getCategoria());
			ps.setString(3, produto.getSubCategoria());
			ps.setString(4, produto.getCodigo());
			ps.setString(5, produto.getMarca());
			ps.setString(6, produto.getDataCompra());

			ps.executeUpdate();

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}

	}

	@Override
	public void remove(String nome) {
        String query = "DELETE FROM produto WHERE nome=?";
        try (PreparedStatement ps = con.getConnection("").prepareStatement(query)) {

			ps.setString(1, nome);
			ps.executeUpdate();

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}

	}

	@Override
	public void update(Produto produto) {
        String query = "UPDATE produto SET categoria = ?, subCategoria = ?, codigo = ?, marca = ?, dataCompra = ? WHERE nome = ?";
        try (PreparedStatement ps = con.getConnection("").prepareStatement(query)) {
			ps.setString(1, produto.getCategoria());
			ps.setString(2, produto.getSubCategoria());
			ps.setString(3, produto.getCodigo());
			ps.setString(4, produto.getMarca());
			ps.setString(5, produto.getDataCompra());
			ps.setString(6, produto.getNome());
			ps.executeUpdate();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

    @Override
    public List<Produto> buscar(String nome) {
        String sql = "SELECT * FROM produto WHERE nome LIKE ?";
        List<Produto> lista = new ArrayList<>();
        try (PreparedStatement ps = con.getConnection("").prepareStatement(sql)) {
            ps.setString(1, "%" + nome + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Produto produto = mapResultSetToProduto(rs);
                    lista.add(produto);
                }
            }
        } catch (Exception e) {
            logger.error("Erro ao buscar pessoa: " + e.getMessage());
        }
        return lista;
    }

    @Override
    public List<Produto> listar() {
        String sql = "SELECT * FROM produto";
        List<Produto> lista = new ArrayList<>();
        try (PreparedStatement ps = con.getConnection("").prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(mapResultSetToProduto(rs));
            }
        } catch (Exception e) {
            logger.error("Erro ao listar produtos: " + e.getMessage());
        }
        return lista;
    }

}
