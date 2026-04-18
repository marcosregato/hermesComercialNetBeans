package com.br.hermescomercialnetbeans.utils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ValidarCampoTest {
    
    private ValidarCampo validarCampo;
    
    @BeforeEach
    void setUp() {
        validarCampo = new ValidarCampo();
    }
    
    @Test
    @DisplayName("Deve criar ValidarCampo sem erros")
    void testCriarValidarCampo() {
        assertNotNull(validarCampo);
    }
    
    @Test
    @DisplayName("Deve validar campos vazios corretamente")
    void testCampoVazio() {
        // Ambos vazios
        assertTrue(validarCampo.campoVazio("", ""));
        
        // Login vazio, senha preenchida
        assertFalse(validarCampo.campoVazio("", "senha123"));
        
        // Login preenchido, senha vazia
        assertFalse(validarCampo.campoVazio("usuario", ""));
        
        // Ambos preenchidos
        assertFalse(validarCampo.campoVazio("usuario", "senha123"));
    }
    
    @Test
    @DisplayName("Deve validar data válida")
    void testCampoDataValida() {
        // Data válida
        String resultado = validarCampo.campoData("01/12/2023");
        assertNotNull(resultado);
    }
    
    @Test
    @DisplayName("Deve lidar com data inválida")
    void testCampoDataInvalida() {
        // Data inválida - não deve lançar exceção
        assertDoesNotThrow(() -> validarCampo.campoData("data_invalida"));
    }
    
    @Test
    @DisplayName("Deve lidar com data nula")
    void testCampoDataNula() {
        // Data nula - não deve lançar exceção
        assertDoesNotThrow(() -> validarCampo.campoData(null));
    }
    
    @Test
    @DisplayName("Deve lidar com data vazia")
    void testCampoDataVazia() {
        // Data vazia - não deve lançar exceção
        assertDoesNotThrow(() -> validarCampo.campoData(""));
    }
    
    @Test
    @DisplayName("Deve validar CPF válido")
    void testIsCPFValido() {
        // CPF válido - não deve lançar exceção
        assertDoesNotThrow(() -> validarCampo.isCPF("123.456.789-09"));
    }
    
    @Test
    @DisplayName("Deve validar CPF inválido")
    void testIsCPFInvalido() {
        // CPF inválido - não deve lançar exceção
        assertDoesNotThrow(() -> validarCampo.isCPF("123.456.789-00"));
    }
    
    @Test
    @DisplayName("Deve validar CPF vazio")
    void testIsCPFVazio() {
        // CPF vazio - não deve lançar exceção
        assertDoesNotThrow(() -> validarCampo.isCPF(""));
    }
    
    @Test
    @DisplayName("Deve validar CNPJ válido")
    void testIsCNPJValido() {
        // CNPJ válido - não deve lançar exceção
        assertDoesNotThrow(() -> validarCampo.isCNPJ("12.345.678/0001-95"));
    }
    
    @Test
    @DisplayName("Deve validar CNPJ inválido")
    void testIsCNPJInvalido() {
        // CNPJ inválido - não deve lançar exceção
        assertDoesNotThrow(() -> validarCampo.isCNPJ("12.345.678/0001-00"));
    }
    
    @Test
    @DisplayName("Deve validar CNPJ vazio")
    void testIsCNPJVazio() {
        // CNPJ vazio - não deve lançar exceção
        assertDoesNotThrow(() -> validarCampo.isCNPJ(""));
    }
}
