package com.br.hermescomercialnetbeans.dao;

import com.br.hermescomercialnetbeans.model.MovimentoCaixa;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import com.br.hermescomercialnetbeans.connectionDB.PostgreSQLConnection;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.PreparedStatement;

public class MovimentoCaixaDao {

    private static final Logger logger = LogManager.getLogger(MovimentoCaixaDao.class);

    private MovimentoCaixa mapResultSetToMovimentoCaixa(ResultSet rs) throws SQLException {
        MovimentoCaixa movimento = new MovimentoCaixa();
        movimento.setId(rs.getLong("id"));
        
        String dataHoraStr = rs.getString("data_hora");
        if (dataHoraStr != null && !dataHoraStr.isEmpty()) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            movimento.setDataHora(LocalDateTime.parse(dataHoraStr, formatter));
        }
        
        movimento.setUsuarioId(rs.getLong("usuario_id"));
        movimento.setUsuarioNome(rs.getString("usuario_nome"));
        
        String tipoMovimentoStr = rs.getString("tipo_movimento");
        if (tipoMovimentoStr != null) {
            movimento.setTipoMovimento(MovimentoCaixa.TipoMovimento.valueOf(tipoMovimentoStr));
        }
        
        movimento.setValor(rs.getDouble("valor"));
        movimento.setSaldoAnterior(rs.getDouble("saldo_anterior"));
        movimento.setSaldoAtual(rs.getDouble("saldo_atual"));
        movimento.setDescricao(rs.getString("descricao"));
        movimento.setObservacoes(rs.getString("observacoes"));
        
        return movimento;
    }

    public void salvar(MovimentoCaixa movimento) {
        String query = "INSERT INTO movimento_caixa (data_hora, usuario_id, usuario_nome, tipo_movimento, " +
                      "valor, saldo_anterior, saldo_atual, descricao, observacoes) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (PreparedStatement ps = PostgreSQLConnection.getConnection().prepareStatement(query)) {
            ps.setString(1, movimento.getDataHora().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            ps.setObject(2, movimento.getUsuarioId());
            ps.setString(3, movimento.getUsuarioNome());
            ps.setString(4, movimento.getTipoMovimento() != null ? movimento.getTipoMovimento().name() : null);
            ps.setDouble(5, movimento.getValor());
            ps.setDouble(6, movimento.getSaldoAnterior());
            ps.setDouble(7, movimento.getSaldoAtual());
            ps.setString(8, movimento.getDescricao());
            ps.setString(9, movimento.getObservacoes());
            
            ps.executeUpdate();
            logger.info("Movimento caixa salvo com sucesso: " + movimento.getTipoMovimento());
            
        } catch (SQLException e) {
            logger.error("Erro ao salvar movimento caixa: " + e.getMessage(), e);
            throw new RuntimeException("Erro ao salvar movimento caixa", e);
        }
    }

    public void remover(Long id) {
        String query = "DELETE FROM movimento_caixa WHERE id = ?";
        
        try (PreparedStatement ps = PostgreSQLConnection.getConnection().prepareStatement(query)) {
            ps.setLong(1, id);
            ps.executeUpdate();
            logger.info("Movimento caixa removido com sucesso, ID: " + id);
            
        } catch (SQLException e) {
            logger.error("Erro ao remover movimento caixa: " + e.getMessage(), e);
            throw new RuntimeException("Erro ao remover movimento caixa", e);
        }
    }

    public void update(MovimentoCaixa movimento) {
        String query = "UPDATE movimento_caixa SET data_hora = ?, usuario_id = ?, usuario_nome = ?, " +
                      "tipo_movimento = ?, valor = ?, saldo_anterior = ?, saldo_atual = ?, " +
                      "descricao = ?, observacoes = ? WHERE id = ?";
        
        try (PreparedStatement ps = PostgreSQLConnection.getConnection().prepareStatement(query)) {
            ps.setString(1, movimento.getDataHora().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            ps.setObject(2, movimento.getUsuarioId());
            ps.setString(3, movimento.getUsuarioNome());
            ps.setString(4, movimento.getTipoMovimento() != null ? movimento.getTipoMovimento().name() : null);
            ps.setDouble(5, movimento.getValor());
            ps.setDouble(6, movimento.getSaldoAnterior());
            ps.setDouble(7, movimento.getSaldoAtual());
            ps.setString(8, movimento.getDescricao());
            ps.setString(9, movimento.getObservacoes());
            ps.setLong(10, movimento.getId());
            
            ps.executeUpdate();
            logger.info("Movimento caixa atualizado com sucesso, ID: " + movimento.getId());
            
        } catch (SQLException e) {
            logger.error("Erro ao atualizar movimento caixa: " + e.getMessage(), e);
            throw new RuntimeException("Erro ao atualizar movimento caixa", e);
        }
    }

    public List<MovimentoCaixa> listar() {
        String query = "SELECT * FROM movimento_caixa ORDER BY data_hora DESC";
        List<MovimentoCaixa> movimentos = new ArrayList<>();
        
        try (PreparedStatement ps = PostgreSQLConnection.getConnection().prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                movimentos.add(mapResultSetToMovimentoCaixa(rs));
            }
            
        } catch (SQLException e) {
            logger.error("Erro ao listar movimentos caixa: " + e.getMessage(), e);
            throw new RuntimeException("Erro ao listar movimentos caixa", e);
        }
        
        return movimentos;
    }

    public MovimentoCaixa buscarPorId(Long id) {
        String query = "SELECT * FROM movimento_caixa WHERE id = ?";
        
        try (PreparedStatement ps = PostgreSQLConnection.getConnection().prepareStatement(query)) {
            ps.setLong(1, id);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToMovimentoCaixa(rs);
                }
            }
            
        } catch (SQLException e) {
            logger.error("Erro ao buscar movimento caixa por ID: " + e.getMessage(), e);
            throw new RuntimeException("Erro ao buscar movimento caixa por ID", e);
        }
        
        return null;
    }

    public List<MovimentoCaixa> buscarPorPeriodo(String dataInicio, String dataFim) {
        String query = "SELECT * FROM movimento_caixa WHERE DATE(data_hora) BETWEEN ? AND ? ORDER BY data_hora DESC";
        List<MovimentoCaixa> movimentos = new ArrayList<>();
        
        try (PreparedStatement ps = PostgreSQLConnection.getConnection().prepareStatement(query)) {
            ps.setString(1, dataInicio);
            ps.setString(2, dataFim);
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    movimentos.add(mapResultSetToMovimentoCaixa(rs));
                }
            }
            
        } catch (SQLException e) {
            logger.error("Erro ao buscar movimentos caixa por período: " + e.getMessage(), e);
            throw new RuntimeException("Erro ao buscar movimentos caixa por período", e);
        }
        
        return movimentos;
    }

    public List<MovimentoCaixa> buscarPorUsuario(Long usuarioId) {
        String query = "SELECT * FROM movimento_caixa WHERE usuario_id = ? ORDER BY data_hora DESC";
        List<MovimentoCaixa> movimentos = new ArrayList<>();
        
        try (PreparedStatement ps = PostgreSQLConnection.getConnection().prepareStatement(query)) {
            ps.setLong(1, usuarioId);
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    movimentos.add(mapResultSetToMovimentoCaixa(rs));
                }
            }
            
        } catch (SQLException e) {
            logger.error("Erro ao buscar movimentos caixa por usuário: " + e.getMessage(), e);
            throw new RuntimeException("Erro ao buscar movimentos caixa por usuário", e);
        }
        
        return movimentos;
    }

    public MovimentoCaixa buscarUltimoMovimento() {
        String query = "SELECT * FROM movimento_caixa ORDER BY data_hora DESC LIMIT 1";
        
        try (PreparedStatement ps = PostgreSQLConnection.getConnection().prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            
            if (rs.next()) {
                return mapResultSetToMovimentoCaixa(rs);
            }
            
        } catch (SQLException e) {
            logger.error("Erro ao buscar último movimento caixa: " + e.getMessage(), e);
            throw new RuntimeException("Erro ao buscar último movimento caixa", e);
        }
        
        return null;
    }

    public List<MovimentoCaixa> buscarMovimentosAbertos() {
        String query = "SELECT * FROM movimento_caixa WHERE tipo_movimento = 'ABERTURA' " +
                      "AND NOT EXISTS (SELECT 1 FROM movimento_caixa mc2 WHERE mc2.tipo_movimento = 'FECHAMENTO' " +
                      "AND mc2.data_hora > movimento_caixa.data_hora) ORDER BY data_hora DESC";
        List<MovimentoCaixa> movimentos = new ArrayList<>();
        
        try (PreparedStatement ps = PostgreSQLConnection.getConnection().prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                movimentos.add(mapResultSetToMovimentoCaixa(rs));
            }
            
        } catch (SQLException e) {
            logger.error("Erro ao buscar movimentos caixa abertos: " + e.getMessage(), e);
            throw new RuntimeException("Erro ao buscar movimentos caixa abertos", e);
        }
        
        return movimentos;
    }

    public double calcularSaldoAtual() {
        String query = "SELECT saldo_atual FROM movimento_caixa ORDER BY data_hora DESC LIMIT 1";
        
        try (PreparedStatement ps = PostgreSQLConnection.getConnection().prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            
            if (rs.next()) {
                return rs.getDouble("saldo_atual");
            }
            
        } catch (SQLException e) {
            logger.error("Erro ao calcular saldo atual: " + e.getMessage(), e);
            throw new RuntimeException("Erro ao calcular saldo atual", e);
        }
        
        return 0.0;
    }

    public double calcularSaldoPorPeriodo(String dataInicio, String dataFim) {
        String query = "SELECT saldo_atual FROM movimento_caixa WHERE DATE(data_hora) BETWEEN ? AND ? " +
                      "ORDER BY data_hora DESC LIMIT 1";
        
        try (PreparedStatement ps = PostgreSQLConnection.getConnection().prepareStatement(query)) {
            ps.setString(1, dataInicio);
            ps.setString(2, dataFim);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble("saldo_atual");
                }
            }
            
        } catch (SQLException e) {
            logger.error("Erro ao calcular saldo por período: " + e.getMessage(), e);
            throw new RuntimeException("Erro ao calcular saldo por período", e);
        }
        
        return 0.0;
    }
    
    /**
     * Busca um movimento de caixa aberto (sem fechamento)
     * @return MovimentoCaixa aberto ou null se não encontrar
     */
    public MovimentoCaixa buscarCaixaAberto() {
        String query = "SELECT * FROM movimento_caixa WHERE tipo_movimento = 'ABERTURA' " +
                     "AND id NOT IN (SELECT id_abertura FROM movimento_caixa WHERE tipo_movimento = 'FECHAMENTO' AND id_abertura IS NOT NULL) " +
                     "ORDER BY data_hora DESC LIMIT 1";
        
        try (PreparedStatement ps = PostgreSQLConnection.getConnection().prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            
            if (rs.next()) {
                return mapResultSetToMovimentoCaixa(rs);
            }
            
        } catch (SQLException e) {
            logger.error("Erro ao buscar caixa aberto: " + e.getMessage(), e);
            throw new RuntimeException("Erro ao buscar caixa aberto", e);
        }
        
        return null;
    }
    
    /**
     * Lista movimentos do dia atual
     * @return Lista de movimentos do dia
     */
    public List<MovimentoCaixa> listarHoje() {
        String query = "SELECT * FROM movimento_caixa WHERE DATE(data_hora) = CURRENT_DATE ORDER BY data_hora DESC";
        List<MovimentoCaixa> movimentos = new ArrayList<>();
        
        try (PreparedStatement ps = PostgreSQLConnection.getConnection().prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                movimentos.add(mapResultSetToMovimentoCaixa(rs));
            }
            
        } catch (SQLException e) {
            logger.error("Erro ao listar movimentos de hoje: " + e.getMessage(), e);
            throw new RuntimeException("Erro ao listar movimentos de hoje", e);
        }
        
        return movimentos;
    }
}
