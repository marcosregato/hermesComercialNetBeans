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
        logger.info("Iniciando salvamento do produto: " + (produto.getNome() != null ? produto.getNome() : "SEM NOME"));
        logger.debug("Código do produto: " + produto.getCodigo());
        logger.debug("Categoria: " + produto.getCategoria() + " | Subcategoria: " + produto.getSubCategoria());
        logger.debug("Estoque: " + produto.getEstoque() + " | Estoque Mínimo: " + produto.getEstoqueMinimo());
        logger.debug("Preço: R$ " + produto.getPreco() + " | Preço Compra: R$ " + produto.getPrecoCompra());
        
        String query ="INSERT INTO produto (nome, categoria, sub_categoria, codigo, marca, data_compra, preco, preco_compra, estoque, estoque_minimo, ativo) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (PreparedStatement ps = PostgreSQLConnection.getConnection().prepareStatement(query)) {
            logger.debug("Executando query INSERT para produto");

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

			int rowsAffected = ps.executeUpdate();
			logger.info("Produto salvo com sucesso! Linhas afetadas: " + rowsAffected);

		} catch (SQLException e) {
			logger.error("Erro SQL ao salvar produto: " + e.getMessage(), e);
			logger.error("SQL State: " + e.getSQLState());
			logger.error("Error Code: " + e.getErrorCode());
		} catch (Exception e) {
			logger.error("Erro inesperado ao salvar produto: " + e.getMessage(), e);
		}

	}

	public void remover(Integer id) throws SQLException {
		logger.info("Iniciando remoção do produto ID: " + id);
		
		// Verificar se produto está sendo usado em vendas
		String checkQuery = "SELECT COUNT(*) FROM item_venda WHERE produto_id = ?";
		logger.debug("Verificando se produto ID " + id + " está sendo usado em vendas");
		
		try (PreparedStatement ps = PostgreSQLConnection.getConnection().prepareStatement(checkQuery)) {
			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();
			if (rs.next() && rs.getInt(1) > 0) {
				int vendasCount = rs.getInt(1);
				logger.warn("Produto ID " + id + " não pode ser excluído! Está em " + vendasCount + " vendas.");
				throw new SQLException("Produto não pode ser excluído pois está registrado em " + vendasCount + " vendas!");
			}
			logger.debug("Produto ID " + id + " não está sendo usado em vendas. Pode ser excluído.");
		} catch (SQLException e) {
			logger.error("Erro ao verificar uso do produto ID " + id + " em vendas: " + e.getMessage(), e);
			throw e;
		}
		
		// Se não estiver sendo usado, pode excluir
		String query = "DELETE FROM produto WHERE id=?";
		logger.debug("Executando DELETE para produto ID: " + id);
		
		try (PreparedStatement ps = PostgreSQLConnection.getConnection().prepareStatement(query)) {
			ps.setInt(1, id);
			int rowsAffected = ps.executeUpdate();
			logger.info("Produto ID " + id + " excluído com sucesso! Linhas afetadas: " + rowsAffected);
			
			if (rowsAffected == 0) {
				logger.warn("Nenhuma linha afetada ao excluir produto ID " + id + ". Produto pode não existir.");
			}
			
		} catch (SQLException e) {
			logger.error("Erro SQL ao excluir produto ID " + id + ": " + e.getMessage(), e);
			logger.error("SQL State: " + e.getSQLState());
			logger.error("Error Code: " + e.getErrorCode());
			throw e;
		} catch (Exception e) {
			logger.error("Erro inesperado ao excluir produto ID " + id + ": " + e.getMessage(), e);
			throw new SQLException("Erro inesperado ao excluir produto", e);
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
