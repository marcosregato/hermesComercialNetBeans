package com.br.hermescomercialnetbeans.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import com.br.hermescomercialnetbeans.connectionDB.PostgreSQLConnection;
import com.br.hermescomercialnetbeans.model.Pagamento;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PagamentoDao {

    private PostgreSQLConnection con = new PostgreSQLConnection();
    private static final Logger logger = LogManager.getLogger(PagamentoDao.class);

    private Pagamento mapResultSetToPagamento(ResultSet rs) throws SQLException {
        Pagamento pagamento = new Pagamento();
        pagamento.setId(rs.getLong("id"));
        pagamento.setVendaId(rs.getLong("venda_id"));
        
        String formaPagamentoStr = rs.getString("forma_pagamento");
        if (formaPagamentoStr != null) {
            pagamento.setFormaPagamento(Pagamento.FormaPagamento.valueOf(formaPagamentoStr));
        }
        
        pagamento.setValor(rs.getDouble("valor"));
        pagamento.setValorRecebido(rs.getDouble("valor_recebido"));
        pagamento.setTroco(rs.getDouble("troco"));
        pagamento.setNumeroParcelas(rs.getString("numero_parcelas"));
        pagamento.setBandeiraCartao(rs.getString("bandeira_cartao"));
        pagamento.setAutorizacaoCartao(rs.getString("autorizacao_cartao"));
        pagamento.setChavePix(rs.getString("chave_pix"));
        pagamento.setNumeroCheque(rs.getString("numero_cheque"));
        pagamento.setBancoCheque(rs.getString("banco_cheque"));
        
        String dataPagamentoStr = rs.getString("data_pagamento");
        if (dataPagamentoStr != null && !dataPagamentoStr.isEmpty()) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            pagamento.setDataPagamento(LocalDateTime.parse(dataPagamentoStr, formatter));
        }
        
        String statusStr = rs.getString("status");
        if (statusStr != null) {
            pagamento.setStatus(Pagamento.StatusPagamento.valueOf(statusStr));
        }
        
        pagamento.setObservacoes(rs.getString("observacoes"));
        
        return pagamento;
    }

    public void salvar(Pagamento pagamento) {
        String query = "INSERT INTO pagamento (venda_id, forma_pagamento, valor, valor_recebido, troco, " +
                      "numero_parcelas, bandeira_cartao, autorizacao_cartao, chave_pix, numero_cheque, " +
                      "banco_cheque, data_pagamento, status, observacoes) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (PreparedStatement ps = PostgreSQLConnection.getConnection().prepareStatement(query)) {
            ps.setObject(1, pagamento.getVendaId());
            ps.setString(2, pagamento.getFormaPagamento() != null ? pagamento.getFormaPagamento().name() : null);
            ps.setDouble(3, pagamento.getValor());
            ps.setDouble(4, pagamento.getValorRecebido());
            ps.setDouble(5, pagamento.getTroco());
            ps.setString(6, pagamento.getNumeroParcelas());
            ps.setString(7, pagamento.getBandeiraCartao());
            ps.setString(8, pagamento.getAutorizacaoCartao());
            ps.setString(9, pagamento.getChavePix());
            ps.setString(10, pagamento.getNumeroCheque());
            ps.setString(11, pagamento.getBancoCheque());
            ps.setString(12, pagamento.getDataPagamento().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            ps.setString(13, pagamento.getStatus() != null ? pagamento.getStatus().name() : null);
            ps.setString(14, pagamento.getObservacoes());
            
            ps.executeUpdate();
            logger.info("Pagamento salvo com sucesso, venda_id: " + pagamento.getVendaId());
            
        } catch (SQLException e) {
            logger.error("Erro ao salvar pagamento: " + e.getMessage(), e);
            throw new RuntimeException("Erro ao salvar pagamento", e);
        }
    }

    public void remover(Long id) {
        String query = "DELETE FROM pagamento WHERE id = ?";
        
        try (PreparedStatement ps = PostgreSQLConnection.getConnection().prepareStatement(query)) {
            ps.setLong(1, id);
            ps.executeUpdate();
            logger.info("Pagamento removido com sucesso, ID: " + id);
            
        } catch (SQLException e) {
            logger.error("Erro ao remover pagamento: " + e.getMessage(), e);
            throw new RuntimeException("Erro ao remover pagamento", e);
        }
    }

    public void update(Pagamento pagamento) {
        String query = "UPDATE pagamento SET venda_id = ?, forma_pagamento = ?, valor = ?, valor_recebido = ?, " +
                      "troco = ?, numero_parcelas = ?, bandeira_cartao = ?, autorizacao_cartao = ?, chave_pix = ?, " +
                      "numero_cheque = ?, banco_cheque = ?, data_pagamento = ?, status = ?, observacoes = ? WHERE id = ?";
        
        try (PreparedStatement ps = PostgreSQLConnection.getConnection().prepareStatement(query)) {
            ps.setObject(1, pagamento.getVendaId());
            ps.setString(2, pagamento.getFormaPagamento() != null ? pagamento.getFormaPagamento().name() : null);
            ps.setDouble(3, pagamento.getValor());
            ps.setDouble(4, pagamento.getValorRecebido());
            ps.setDouble(5, pagamento.getTroco());
            ps.setString(6, pagamento.getNumeroParcelas());
            ps.setString(7, pagamento.getBandeiraCartao());
            ps.setString(8, pagamento.getAutorizacaoCartao());
            ps.setString(9, pagamento.getChavePix());
            ps.setString(10, pagamento.getNumeroCheque());
            ps.setString(11, pagamento.getBancoCheque());
            ps.setString(12, pagamento.getDataPagamento().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            ps.setString(13, pagamento.getStatus() != null ? pagamento.getStatus().name() : null);
            ps.setString(14, pagamento.getObservacoes());
            ps.setLong(15, pagamento.getId());
            
            ps.executeUpdate();
            logger.info("Pagamento atualizado com sucesso, ID: " + pagamento.getId());
            
        } catch (SQLException e) {
            logger.error("Erro ao atualizar pagamento: " + e.getMessage(), e);
            throw new RuntimeException("Erro ao atualizar pagamento", e);
        }
    }

    public List<Pagamento> listar() {
        String query = "SELECT * FROM pagamento ORDER BY data_pagamento DESC";
        List<Pagamento> pagamentos = new ArrayList<>();
        
        try (PreparedStatement ps = PostgreSQLConnection.getConnection().prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                pagamentos.add(mapResultSetToPagamento(rs));
            }
            
        } catch (SQLException e) {
            logger.error("Erro ao listar pagamentos: " + e.getMessage(), e);
            throw new RuntimeException("Erro ao listar pagamentos", e);
        }
        
        return pagamentos;
    }

    public Pagamento buscarPorId(Long id) {
        String query = "SELECT * FROM pagamento WHERE id = ?";
        
        try (PreparedStatement ps = PostgreSQLConnection.getConnection().prepareStatement(query)) {
            ps.setLong(1, id);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToPagamento(rs);
                }
            }
            
        } catch (SQLException e) {
            logger.error("Erro ao buscar pagamento por ID: " + e.getMessage(), e);
            throw new RuntimeException("Erro ao buscar pagamento por ID", e);
        }
        
        return null;
    }

    public List<Pagamento> buscarPorVendaId(Long vendaId) {
        String query = "SELECT * FROM pagamento WHERE venda_id = ? ORDER BY id";
        List<Pagamento> pagamentos = new ArrayList<>();
        
        try (PreparedStatement ps = PostgreSQLConnection.getConnection().prepareStatement(query)) {
            ps.setLong(1, vendaId);
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    pagamentos.add(mapResultSetToPagamento(rs));
                }
            }
            
        } catch (SQLException e) {
            logger.error("Erro ao buscar pagamentos por venda_id: " + e.getMessage(), e);
            throw new RuntimeException("Erro ao buscar pagamentos por venda_id", e);
        }
        
        return pagamentos;
    }

    public List<Pagamento> buscarPorFormaPagamento(String formaPagamento) {
        String query = "SELECT * FROM pagamento WHERE forma_pagamento = ? ORDER BY data_pagamento DESC";
        List<Pagamento> pagamentos = new ArrayList<>();
        
        try (PreparedStatement ps = PostgreSQLConnection.getConnection().prepareStatement(query)) {
            ps.setString(1, formaPagamento);
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    pagamentos.add(mapResultSetToPagamento(rs));
                }
            }
            
        } catch (SQLException e) {
            logger.error("Erro ao buscar pagamentos por forma_pagamento: " + e.getMessage(), e);
            throw new RuntimeException("Erro ao buscar pagamentos por forma_pagamento", e);
        }
        
        return pagamentos;
    }

    public List<Pagamento> buscarPorPeriodo(String dataInicio, String dataFim) {
        String query = "SELECT * FROM pagamento WHERE DATE(data_pagamento) BETWEEN ? AND ? ORDER BY data_pagamento DESC";
        List<Pagamento> pagamentos = new ArrayList<>();
        
        try (PreparedStatement ps = PostgreSQLConnection.getConnection().prepareStatement(query)) {
            ps.setString(1, dataInicio);
            ps.setString(2, dataFim);
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    pagamentos.add(mapResultSetToPagamento(rs));
                }
            }
            
        } catch (SQLException e) {
            logger.error("Erro ao buscar pagamentos por período: " + e.getMessage(), e);
            throw new RuntimeException("Erro ao buscar pagamentos por período", e);
        }
        
        return pagamentos;
    }
}
