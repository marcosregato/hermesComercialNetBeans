
package com.br.hermescomercialnetbeans.dao;

import com.br.hermescomercialnetbeans.connectionDB.ConnectionBD;
import com.br.hermescomercialnetbeans.model.Pessoa;
import com.br.hermescomercialnetbeans.model.Usuario;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDao {
    
    private static final org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(UsuarioDao.class);
    private ConnectionBD con = new ConnectionBD();


    private Pessoa mapResultSetToPessoa(ResultSet rs) throws SQLException {
        Pessoa pessoa = new Pessoa();
        pessoa.setNome(rs.getString("nome"));
        pessoa.setEndereco(rs.getString("endereco"));
        pessoa.setBairro(rs.getString("bairro"));
        pessoa.setCidade(rs.getString("cidade"));
        pessoa.setEstado(rs.getString("estado"));
        pessoa.setCep(rs.getString("cep"));
        pessoa.setCnpj(rs.getString("cnpj"));
        pessoa.setCpf(rs.getString("cpf"));
        pessoa.setEmail(rs.getString("email"));
        pessoa.setTipoPessoa(rs.getString("tipoPessoa"));
        return pessoa;
    }

    public void salvar(Usuario usuario) {
        String sql = "INSERT INTO pessoa (nome, endereco, bairro, city, estado, cep, cnpj, cpf, email, tipoPessoa) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = con.getConnection("Postgres").prepareStatement(sql)) {
            ps.setString(1, pessoa.getNome());
            ps.setString(2, pessoa.getEndereco());
            ps.setString(3, pessoa.getBairro());
            ps.setString(4, pessoa.getCidade());
            ps.setString(5, pessoa.getEstado());
            ps.setString(6, pessoa.getCep());
            ps.setString(7, pessoa.getCnpj());
            ps.setString(8, pessoa.getCpf());
            ps.setString(9, pessoa.getEmail());
            ps.setString(10, pessoa.getTipoPessoa());
            ps.executeUpdate();
        } catch (Exception e) {
            logger.error("Erro ao salvar pessoa: " + e.getMessage());
        }
    }

    public void update(Pessoa pessoa) {
        String sql = "UPDATE pessoa SET endereco=?, bairro=?, cidade=?, estado=?, cep=?, cnpj=?, cpf=?, email=?, tipoPessoa=? WHERE nome=?";
        try (PreparedStatement ps = con.getConnection("").prepareStatement(sql)) {
            ps.setString(1, pessoa.getEndereco());
            ps.setString(2, pessoa.getBairro());
            ps.setString(3, pessoa.getCidade());
            ps.setString(4, pessoa.getEstado());
            ps.setString(5, pessoa.getCep());
            ps.setString(6, pessoa.getCnpj());
            ps.setString(7, pessoa.getCpf());
            ps.setString(8, pessoa.getEmail());
            ps.setString(9, pessoa.getTipoPessoa());
            ps.setString(10, pessoa.getNome());
            ps.executeUpdate();
        } catch (Exception e) {
            logger.error("Erro ao atualizar pessoa: " + e.getMessage());
        }
    }

    public void remove(String nome) {
        String sql = "DELETE FROM pessoa WHERE nome=?";
        try (PreparedStatement ps = con.getConnection("").prepareStatement(sql)) {
            ps.setString(1, nome);
            ps.executeUpdate();
        } catch (Exception e) {
            logger.error("Erro ao remover pessoa: " + e.getMessage());
        }
    }

    public List<Pessoa> buscar(String nome) {
        String sql = "SELECT * FROM pessoa WHERE nome LIKE ?";
        List<Pessoa> lista = new ArrayList<>();
        try (PreparedStatement ps = con.getConnection("").prepareStatement(sql)) {
            ps.setString(1, "%" + nome + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Pessoa pessoa = mapResultSetToPessoa(rs);
                    lista.add(pessoa);
                }
            }
        } catch (Exception e) {
            logger.error("Erro ao buscar pessoa: " + e.getMessage());
        }
        return lista;
    }

    public List<Pessoa> lista() {
        String sql = "SELECT * FROM pessoa";
        List<Pessoa> lista = new ArrayList<>();
        try (PreparedStatement ps = con.getConnection("").prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(mapResultSetToPessoa(rs));
            }
        } catch (Exception e) {
            logger.error("Erro ao listar pessoas: " + e.getMessage());
        }
        return lista;
    }
}
