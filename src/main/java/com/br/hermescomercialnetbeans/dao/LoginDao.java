
package com.br.hermescomercialnetbeans.dao;

import com.br.hermescomercialnetbeans.connectionDB.ConnectionBD;
import com.br.hermescomercialnetbeans.model.Pessoa;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class LoginDao {

    private ConnectionBD con = new ConnectionBD();
    private static final Logger logger = LogManager.getLogger(LoginDao.class);

    public Pessoa acessarPessoa(String login, String senha) {
       
        Pessoa pessoa = null;

        String query = "SELECT p.nome, p.endereco, p.bairro, p.cidade, p.estado, p.cep, p.cnpj, p.cpf, p.email, p.tipoUsuario " +
                       "FROM login l " +
                       "INNER JOIN pessoa p ON l.fk_pessoa = p.id " +
                       "WHERE l.login = ? AND l.senha = ?";

        try (PreparedStatement ps = con.getConnection("Postgres").prepareStatement(query)) {
            ps.setString(1, login);
            ps.setString(2, senha);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    pessoa = new Pessoa();
                    pessoa.setId(rs.getLong("id"));
                    pessoa.setNome(rs.getString("nome"));
                    pessoa.setEndereco(rs.getString("endereco"));
                    pessoa.setBairro(rs.getString("bairro"));
                    pessoa.setCidade(rs.getString("cidade"));
                    pessoa.setEstado(rs.getString("estado"));
                    pessoa.setCep(rs.getString("cep"));
                    pessoa.setCnpj(rs.getString("cnpj"));
                    pessoa.setCpf(rs.getString("cpf"));
                    pessoa.setEmail(rs.getString("email"));
                    pessoa.setTipoUsuario(rs.getString("tipoUsuario"));
                }
            }
        } catch (Exception e) {
            logger.error("Erro ao acessar usuário", e);
        }
        return pessoa;
    }
    
    public void salvar(Pessoa pessoa) {
        String query = "INSERT INTO pessoa (nome, endereco, bairro, cidade, estado, cep, cnpj, cpf, email, tipousuario) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = con.getConnection("Postgres").prepareStatement(query)) {
            ps.setString(1, pessoa.getNome());
            ps.setString(2, pessoa.getEndereco());
            ps.setString(3, pessoa.getBairro());
            ps.setString(4, pessoa.getCidade());
            ps.setString(5,pessoa.getEstado());
            ps.setString(6,pessoa.getCep());
            ps.setString(7,pessoa.getCnpj());
            ps.setString(8,pessoa.getCpf());
            ps.setString(9,pessoa.getEmail());
            ps.setString(10,pessoa.getTipoPessoa());
            ps.executeUpdate();
        } catch (Exception e) { 
            logger.error("Erro ao salvar pessoa: " + e.getMessage());

        }
    }
}
