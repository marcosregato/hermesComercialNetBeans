package com.br.hermescomercialnetbeans.business.permissao;

import com.br.hermescomercialnetbeans.model.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class PermissaoAcessoTest {
    
    private PermissaoAcesso permissaoAcesso;
    private Usuario usuario;
    
    @BeforeEach
    void setUp() {
        permissaoAcesso = new PermissaoAcesso();
        usuario = new Usuario();
        usuario.setId(1);
        usuario.setNome("testuser");
        usuario.setSenha("password123");
    }
    
    @Test
    @DisplayName("Deve criar PermissaoAcesso sem erros")
    void testCriarPermissaoAcesso() {
        assertNotNull(permissaoAcesso);
    }
    
    @Test
    @DisplayName("Deve processar acesso com usuário válido")
    void testAcessoAcaoUsuarioValido() {
        // Act & Assert
        assertDoesNotThrow(() -> permissaoAcesso.acessoAcao(usuario));
    }
    
    @Test
    @DisplayName("Deve lidar com usuário nulo sem lançar exceção")
    void testAcessoAcaoUsuarioNulo() {
        // Act & Assert
        assertDoesNotThrow(() -> permissaoAcesso.acessoAcao(null));
    }
    
    @Test
    @DisplayName("Deve lidar com usuário sem nome")
    void testAcessoAcaoUsuarioSemNome() {
        // Arrange
        usuario.setNome(null);
        
        // Act & Assert
        assertDoesNotThrow(() -> permissaoAcesso.acessoAcao(usuario));
    }
    
    @Test
    @DisplayName("Deve lidar com usuário com nome vazio")
    void testAcessoAcaoUsuarioNomeVazio() {
        // Arrange
        usuario.setNome("");
        
        // Act & Assert
        assertDoesNotThrow(() -> permissaoAcesso.acessoAcao(usuario));
    }
    
    @Test
    @DisplayName("Deve processar múltiplos acessos")
    void testMultiplosAcessos() {
        // Act & Assert
        assertDoesNotThrow(() -> {
            permissaoAcesso.acessoAcao(usuario);
            permissaoAcesso.acessoAcao(usuario);
            permissaoAcesso.acessoAcao(usuario);
        });
    }
}
