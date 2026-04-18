package com.br.hermescomercialnetbeans.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import com.br.hermescomercialnetbeans.connectionDB.PostgreSQLConnection;
import com.br.hermescomercialnetbeans.model.Venda;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class VendaDao {

    private PostgreSQLConnection con = new PostgreSQLConnection();
    private static final Logger logger = LogManager.getLogger(VendaDao.class);

    private Venda mapResultSetToVenda(ResultSet rs) throws SQLException {
        Venda venda = new Venda();
        venda.setId(rs.getLong("id"));
        venda.setCodigo(rs.getString("codigo"));
        
        String dataHoraStr = rs.getString("data_hora");
        if (dataHoraStr != null && !dataHoraStr.isEmpty()) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            venda.setDataHora(LocalDateTime.parse(dataHoraStr, formatter));
        }
        
        venda.setClienteId(rs.getLong("cliente_id"));
        venda.setClienteNome(rs.getString("cliente_nome"));
        venda.setUsuarioId(rs.getLong("usuario_id"));
        venda.setUsuarioNome(rs.getString("usuario_nome"));
        venda.setValorTotal(rs.getDouble("valor_total"));
        venda.setValorDesconto(rs.getDouble("valor_desconto"));
        venda.setValorAcrescimo(rs.getDouble("valor_acrescimo"));
        venda.setValorFinal(rs.getDouble("valor_final"));
        venda.setStatus(rs.getString("status"));
        venda.setTipoPagamento(rs.getString("tipo_pagamento"));
        venda.setObservacoes(rs.getString("observacoes"));
        venda.setCancelada(rs.getBoolean("cancelada"));
        
        return venda;
    }

   
    public void salvar(Venda venda) {
        String query = "INSERT INTO venda (codigo, data_hora, cliente_id, cliente_nome, usuario_id, usuario_nome, " +
                      "valor_total, valor_desconto, valor_acrescimo, valor_final, status, tipo_pagamento, observacoes, cancelada) " +
                      "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (PreparedStatement ps = PostgreSQLConnection.getConnection().prepareStatement(query)) {
            ps.setString(1, venda.getCodigo());
            ps.setString(2, venda.getDataHora().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            ps.setObject(3, venda.getClienteId());
            ps.setString(4, venda.getClienteNome());
            ps.setObject(5, venda.getUsuarioId());
            ps.setString(6, venda.getUsuarioNome());
            ps.setDouble(7, venda.getValorTotal());
            ps.setDouble(8, venda.getValorDesconto());
            ps.setDouble(9, venda.getValorAcrescimo());
            ps.setDouble(10, venda.getValorFinal());
            ps.setString(11, venda.getStatus());
            ps.setString(12, venda.getTipoPagamento());
            ps.setString(13, venda.getObservacoes());
            ps.setBoolean(14, venda.isCancelada());
            
            ps.executeUpdate();
            logger.info("Venda salva com sucesso: " + venda.getCodigo());
            
        } catch (SQLException e) {
            logger.error("Erro ao salvar venda: " + e.getMessage(), e);
            throw new RuntimeException("Erro ao salvar venda", e);
        }
    }

   
    public void remover(Long id) {
        String query = "DELETE FROM venda WHERE id = ?";
        
        try (PreparedStatement ps = PostgreSQLConnection.getConnection().prepareStatement(query)) {
            ps.setLong(1, id);
            ps.executeUpdate();
            logger.info("Venda removida com sucesso, ID: " + id);
            
        } catch (SQLException e) {
            logger.error("Erro ao remover venda: " + e.getMessage(), e);
            throw new RuntimeException("Erro ao remover venda", e);
        }
    }

   
    public void update(Venda venda) {
        String query = "UPDATE venda SET codigo = ?, data_hora = ?, cliente_id = ?, cliente_nome = ?, " +
                      "usuario_id = ?, usuario_nome = ?, valor_total = ?, valor_desconto = ?, valor_acrescimo = ?, " +
                      "valor_final = ?, status = ?, tipo_pagamento = ?, observacoes = ?, cancelada = ? WHERE id = ?";
        
        try (PreparedStatement ps = PostgreSQLConnection.getConnection().prepareStatement(query)) {
            ps.setString(1, venda.getCodigo());
            ps.setString(2, venda.getDataHora().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            ps.setObject(3, venda.getClienteId());
            ps.setString(4, venda.getClienteNome());
            ps.setObject(5, venda.getUsuarioId());
            ps.setString(6, venda.getUsuarioNome());
            ps.setDouble(7, venda.getValorTotal());
            ps.setDouble(8, venda.getValorDesconto());
            ps.setDouble(9, venda.getValorAcrescimo());
            ps.setDouble(10, venda.getValorFinal());
            ps.setString(11, venda.getStatus());
            ps.setString(12, venda.getTipoPagamento());
            ps.setString(13, venda.getObservacoes());
            ps.setBoolean(14, venda.isCancelada());
            ps.setLong(15, venda.getId());
            
            ps.executeUpdate();
            logger.info("Venda atualizada com sucesso: " + venda.getCodigo());
            
        } catch (SQLException e) {
            logger.error("Erro ao atualizar venda: " + e.getMessage(), e);
            throw new RuntimeException("Erro ao atualizar venda", e);
        }
    }

   
    public List<Venda> listar() {
        String query = "SELECT * FROM venda ORDER BY data_hora DESC";
        List<Venda> vendas = new ArrayList<>();
        
        try (PreparedStatement ps = PostgreSQLConnection.getConnection().prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                vendas.add(mapResultSetToVenda(rs));
            }
            
        } catch (SQLException e) {
            logger.error("Erro ao listar vendas: " + e.getMessage(), e);
            throw new RuntimeException("Erro ao listar vendas", e);
        }
        
        return vendas;
    }

   
    public Venda buscarPorId(Long id) {
        String query = "SELECT * FROM venda WHERE id = ?";
        
        try (PreparedStatement ps = PostgreSQLConnection.getConnection().prepareStatement(query)) {
            ps.setLong(1, id);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToVenda(rs);
                }
            }
            
        } catch (SQLException e) {
            logger.error("Erro ao buscar venda por ID: " + e.getMessage(), e);
            throw new RuntimeException("Erro ao buscar venda por ID", e);
        }
        
        return null;
    }

   
    public Venda buscarPorCodigo(String codigo) {
        String query = "SELECT * FROM venda WHERE codigo = ?";
        
        try (PreparedStatement ps = PostgreSQLConnection.getConnection().prepareStatement(query)) {
            ps.setString(1, codigo);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToVenda(rs);
                }
            }
            
        } catch (SQLException e) {
            logger.error("Erro ao buscar venda por código: " + e.getMessage(), e);
            throw new RuntimeException("Erro ao buscar venda por código", e);
        }
        
        return null;
    }

   
    public List<Venda> buscarPorPeriodo(String dataInicio, String dataFim) {
        String query = "SELECT * FROM venda WHERE DATE(data_hora) BETWEEN ? AND ? ORDER BY data_hora DESC";
        List<Venda> vendas = new ArrayList<>();
        
        try (PreparedStatement ps = PostgreSQLConnection.getConnection().prepareStatement(query)) {
            ps.setString(1, dataInicio);
            ps.setString(2, dataFim);
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    vendas.add(mapResultSetToVenda(rs));
                }
            }
            
        } catch (SQLException e) {
            logger.error("Erro ao buscar vendas por período: " + e.getMessage(), e);
            throw new RuntimeException("Erro ao buscar vendas por período", e);
        }
        
        return vendas;
    }

   
    public List<Venda> buscarPorCliente(Long clienteId) {
        String query = "SELECT * FROM venda WHERE cliente_id = ? ORDER BY data_hora DESC";
        List<Venda> vendas = new ArrayList<>();
        
        try (PreparedStatement ps = PostgreSQLConnection.getConnection().prepareStatement(query)) {
            ps.setLong(1, clienteId);
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    vendas.add(mapResultSetToVenda(rs));
                }
            }
            
        } catch (SQLException e) {
            logger.error("Erro ao buscar vendas por cliente: " + e.getMessage(), e);
            throw new RuntimeException("Erro ao buscar vendas por cliente", e);
        }
        
        return vendas;
    }

   
    public List<Venda> buscarPorUsuario(Long usuarioId) {
        String query = "SELECT * FROM venda WHERE usuario_id = ? ORDER BY data_hora DESC";
        List<Venda> vendas = new ArrayList<>();
        
        try (PreparedStatement ps = PostgreSQLConnection.getConnection().prepareStatement(query)) {
            ps.setLong(1, usuarioId);
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    vendas.add(mapResultSetToVenda(rs));
                }
            }
            
        } catch (SQLException e) {
            logger.error("Erro ao buscar vendas por usuário: " + e.getMessage(), e);
            throw new RuntimeException("Erro ao buscar vendas por usuário", e);
        }
        
        return vendas;
    }
}
