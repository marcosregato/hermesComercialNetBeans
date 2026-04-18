/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.br.hermescomercialnetbeans.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;

import java.util.ArrayList;
import java.util.List;

import com.br.hermescomercialnetbeans.connectionDB.PostgreSQLConnection;
import com.br.hermescomercialnetbeans.model.Produto;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
 *
 * @author marcos
 */
public class ProdutoDao  {

    private PostgreSQLConnection con = new PostgreSQLConnection();
    private static final Logger logger = LogManager.getLogger(ProdutoDao.class);

        
    private Produto mapResultSetToProdutoCompleto(ResultSet rs) throws SQLException {
        Produto produto = new Produto();
        produto.setId(rs.getInt("id"));
        produto.setNome(rs.getString("nome"));
        produto.setCategoria(rs.getString("categoria"));
        produto.setCodigo(rs.getString("codigo"));
        produto.setMarca(rs.getString("marca"));
        
        // Tratar coluna subCategoria que pode não existir
        try {
            produto.setSubCategoria(rs.getString("subCategoria"));
        } catch (SQLException e) {
            produto.setSubCategoria("");
        }
        
        produto.setDataCompra(rs.getString("data_compra"));
        produto.setPreco(rs.getBigDecimal("preco"));
        produto.setPrecoCompra(rs.getBigDecimal("preco_compra"));
        produto.setEstoque(rs.getInt("estoque"));
        produto.setEstoqueMinimo(rs.getInt("estoque_minimo"));
        produto.setAtivo(rs.getBoolean("ativo"));
        return produto;
    }


	public void salvar(Produto produto) {
        String query ="INSERT INTO produto (nome, categoria, sub_categoria, codigo, marca, data_compra, preco, preco_compra, estoque, estoque_minimo, ativo) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = PostgreSQLConnection.getConnection().prepareStatement(query)) {

			ps.setString(1, produto.getNome());
			ps.setString(2, produto.getCategoria());
			ps.setString(3, produto.getSubCategoria());
			ps.setString(4, produto.getCodigo());
			ps.setString(5, produto.getMarca());
			ps.setDate(6, produto.getDataCompra() != null && !produto.getDataCompra().isEmpty() ? 
				Date.valueOf(produto.getDataCompra()) : null);
			ps.setBigDecimal(7, produto.getPreco());
			ps.setBigDecimal(8, produto.getPrecoCompra());
			ps.setInt(9, produto.getEstoque());
			ps.setInt(10, produto.getEstoqueMinimo());
			ps.setBoolean(11, produto.getAtivo());

			ps.executeUpdate();

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}

	}

	public void remover(Integer id) throws SQLException {
		// Verificar se produto está sendo usado em vendas
		String checkQuery = "SELECT COUNT(*) FROM item_venda WHERE produto_id = ?";
		try (PreparedStatement ps = PostgreSQLConnection.getConnection().prepareStatement(checkQuery)) {
			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();
			if (rs.next() && rs.getInt(1) > 0) {
				throw new SQLException("Produto não pode ser excluído pois está registrado em vendas!");
			}
		}
		
		// Se não estiver sendo usado, pode excluir
		String query = "DELETE FROM produto WHERE id=?";
		try (PreparedStatement ps = PostgreSQLConnection.getConnection().prepareStatement(query)) {
			ps.setInt(1, id);
			ps.executeUpdate();
			logger.info("Produto ID " + id + " excluído com sucesso");
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw e;
		}
	}

	public void atualizar(Produto produto) {
		String query = "UPDATE produto SET nome=?, categoria=?, sub_categoria=?, codigo=?, marca=?, data_compra=?, preco=?, preco_compra=?, estoque=?, estoque_minimo=?, ativo=? WHERE id=?";
		try (PreparedStatement ps = PostgreSQLConnection.getConnection().prepareStatement(query)) {

			ps.setString(1, produto.getNome());
			ps.setString(2, produto.getCategoria());
			ps.setString(3, produto.getSubCategoria());
			ps.setString(4, produto.getCodigo());
			ps.setString(5, produto.getMarca());
			ps.setDate(6, produto.getDataCompra() != null && !produto.getDataCompra().isEmpty() ? 
				Date.valueOf(produto.getDataCompra()) : null);
			ps.setBigDecimal(7, produto.getPreco());
			ps.setBigDecimal(8, produto.getPrecoCompra());
			ps.setInt(9, produto.getEstoque());
			ps.setInt(10, produto.getEstoqueMinimo());
			ps.setBoolean(11, produto.getAtivo());
			ps.setInt(12, produto.getId());

			ps.executeUpdate();

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

    public List<Produto> buscar(String nome) {
        String sql = "SELECT * FROM produto WHERE nome LIKE ?";
        List<Produto> lista = new ArrayList<>();
        try (PreparedStatement ps = PostgreSQLConnection.getConnection().prepareStatement(sql)) {
            ps.setString(1, "%" + nome + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Produto produto = mapResultSetToProdutoCompleto(rs);
                    lista.add(produto);
                }
            }
        } catch (Exception e) {
            logger.error("Erro ao buscar produto: " + e.getMessage());
        }
        return lista;
    }
    
    public List<Produto> listar() {
        String sql = "SELECT * FROM produto";
        List<Produto> lista = new ArrayList<>();
        try (PreparedStatement ps = PostgreSQLConnection.getConnection().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(mapResultSetToProdutoCompleto(rs));
            }
        } catch (Exception e) {
            logger.error("Erro ao listar produtos: " + e.getMessage());
        }
        return lista;
    }
    
    /**
     * Busca produto por código
     * @param codigo Código do produto
     * @return Produto encontrado ou null
     */
    public Produto buscarPorCodigo(String codigo) {
        String query = "SELECT * FROM produto WHERE codigo = ? AND ativo = true";
        
        try (PreparedStatement ps = PostgreSQLConnection.getConnection().prepareStatement(query)) {
            ps.setString(1, codigo);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToProdutoCompleto(rs);
                }
            }
            
        } catch (SQLException e) {
            logger.error("Erro ao buscar produto por código: " + e.getMessage(), e);
            throw new RuntimeException("Erro ao buscar produto por código", e);
        }
        
        return null;
    }
    
    /**
     * Busca produto por ID
     * @param id ID do produto
     * @return Produto encontrado ou null
     */
    public Produto buscarPorId(Integer id) {
        String query = "SELECT * FROM produto WHERE id = ?";
        
        try (PreparedStatement ps = PostgreSQLConnection.getConnection().prepareStatement(query)) {
            ps.setInt(1, id);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToProdutoCompleto(rs);
                }
            }
            
        } catch (SQLException e) {
            logger.error("Erro ao buscar produto por ID: " + e.getMessage(), e);
            throw new RuntimeException("Erro ao buscar produto por ID", e);
        }
        
        return null;
    }

}
