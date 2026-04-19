package com.br.hermescomercialnetbeans.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.br.hermescomercialnetbeans.connectionDB.PostgreSQLConnection;
import com.br.hermescomercialnetbeans.model.ItemVenda;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ItemVendaDao {

    private static final Logger logger = LogManager.getLogger(ItemVendaDao.class);

    private ItemVenda mapResultSetToItemVenda(ResultSet rs) throws SQLException {
        ItemVenda item = new ItemVenda();
        item.setId(rs.getLong("id"));
        item.setVendaId(rs.getLong("venda_id"));
        item.setProdutoId(rs.getLong("produto_id"));
        item.setProdutoCodigo(rs.getString("produto_codigo"));
        item.setProdutoDescricao(rs.getString("produto_descricao"));
        item.setQuantidade(rs.getInt("quantidade"));
        item.setValorUnitario(rs.getDouble("valor_unitario"));
        item.setSubtotal(rs.getDouble("subtotal"));
        item.setDesconto(rs.getDouble("desconto"));
        
        return item;
    }

   
    public void salvar(ItemVenda itemVenda) {
        String query = "INSERT INTO item_venda (venda_id, produto_id, produto_codigo, produto_descricao, " +
                      "quantidade, valor_unitario, subtotal, desconto) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (PreparedStatement ps = PostgreSQLConnection.getConnection().prepareStatement(query)) {
            ps.setObject(1, itemVenda.getVendaId());
            ps.setObject(2, itemVenda.getProdutoId());
            ps.setString(3, itemVenda.getProdutoCodigo());
            ps.setString(4, itemVenda.getProdutoDescricao());
            ps.setInt(5, itemVenda.getQuantidade());
            ps.setDouble(6, itemVenda.getValorUnitario());
            ps.setDouble(7, itemVenda.getSubtotal());
            ps.setDouble(8, itemVenda.getDesconto());
            
            ps.executeUpdate();
            logger.info("Item venda salvo com sucesso, venda_id: " + itemVenda.getVendaId());
            
        } catch (SQLException e) {
            logger.error("Erro ao salvar item venda: " + e.getMessage(), e);
            throw new RuntimeException("Erro ao salvar item venda", e);
        }
    }

   
    public void remover(Long id) {
        String query = "DELETE FROM item_venda WHERE id = ?";
        
        try (PreparedStatement ps = PostgreSQLConnection.getConnection().prepareStatement(query)) {
            ps.setLong(1, id);
            ps.executeUpdate();
            logger.info("Item venda removido com sucesso, ID: " + id);
            
        } catch (SQLException e) {
            logger.error("Erro ao remover item venda: " + e.getMessage(), e);
            throw new RuntimeException("Erro ao remover item venda", e);
        }
    }

   
    public void update(ItemVenda itemVenda) {
        String query = "UPDATE item_venda SET venda_id = ?, produto_id = ?, produto_codigo = ?, " +
                      "produto_descricao = ?, quantidade = ?, valor_unitario = ?, subtotal = ?, desconto = ? WHERE id = ?";
        
        try (PreparedStatement ps = PostgreSQLConnection.getConnection().prepareStatement(query)) {
            ps.setLong(1, itemVenda.getVendaId());
            ps.setLong(2, itemVenda.getProdutoId());
            ps.setString(3, itemVenda.getProdutoCodigo());
            ps.setString(4, itemVenda.getProdutoDescricao());
            ps.setInt(5, itemVenda.getQuantidade());
            ps.setDouble(6, itemVenda.getValorUnitario());
            ps.setDouble(7, itemVenda.getSubtotal());
            ps.setDouble(8, itemVenda.getDesconto());
            ps.setLong(9, itemVenda.getId());
            
            ps.executeUpdate();
            logger.info("Item venda atualizado com sucesso, ID: " + itemVenda.getId());
            
        } catch (SQLException e) {
            logger.error("Erro ao atualizar item venda: " + e.getMessage(), e);
            throw new RuntimeException("Erro ao atualizar item venda", e);
        }
    }

   
    public List<ItemVenda> listar() {
        String query = "SELECT * FROM item_venda ORDER BY id DESC";
        List<ItemVenda> itens = new ArrayList<>();
        
        try (PreparedStatement ps = PostgreSQLConnection.getConnection().prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                itens.add(mapResultSetToItemVenda(rs));
            }
            
        } catch (SQLException e) {
            logger.error("Erro ao listar itens venda: " + e.getMessage(), e);
            throw new RuntimeException("Erro ao listar itens venda", e);
        }
        
        return itens;
    }

   
    public ItemVenda buscarPorId(Long id) {
        String query = "SELECT * FROM item_venda WHERE id = ?";
        
        try (PreparedStatement ps = PostgreSQLConnection.getConnection().prepareStatement(query)) {
            ps.setLong(1, id);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToItemVenda(rs);
                }
            }
            
        } catch (SQLException e) {
            logger.error("Erro ao buscar item venda por ID: " + e.getMessage(), e);
            throw new RuntimeException("Erro ao buscar item venda por ID", e);
        }
        
        return null;
    }

   
    public List<ItemVenda> buscarPorVendaId(Long vendaId) {
        String query = "SELECT * FROM item_venda WHERE venda_id = ? ORDER BY id";
        List<ItemVenda> itens = new ArrayList<>();
        
        try (PreparedStatement ps = PostgreSQLConnection.getConnection().prepareStatement(query)) {
            ps.setLong(1, vendaId);
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    itens.add(mapResultSetToItemVenda(rs));
                }
            }
            
        } catch (SQLException e) {
            logger.error("Erro ao buscar itens venda por venda_id: " + e.getMessage(), e);
            throw new RuntimeException("Erro ao buscar itens venda por venda_id", e);
        }
        
        return itens;
    }

   
    public List<ItemVenda> buscarPorProdutoId(Long produtoId) {
        String query = "SELECT * FROM item_venda WHERE produto_id = ? ORDER BY id DESC";
        List<ItemVenda> itens = new ArrayList<>();
        
        try (PreparedStatement ps = PostgreSQLConnection.getConnection().prepareStatement(query)) {
            ps.setLong(1, produtoId);
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    itens.add(mapResultSetToItemVenda(rs));
                }
            }
            
        } catch (SQLException e) {
            logger.error("Erro ao buscar itens venda por produto_id: " + e.getMessage(), e);
            throw new RuntimeException("Erro ao buscar itens venda por produto_id", e);
        }
        
        return itens;
    }
}
