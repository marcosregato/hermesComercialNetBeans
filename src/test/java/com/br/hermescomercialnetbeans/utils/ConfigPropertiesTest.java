package com.br.hermescomercialnetbeans.utils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ConfigPropertiesTest {
    
    private ConfigProperties configProperties;
    
    @BeforeEach
    void setUp() {
        configProperties = ConfigProperties.getInstance();
    }
    
    @Test
    @DisplayName("Deve criar ConfigProperties sem erros")
    void testCriarConfigProperties() {
        assertNotNull(configProperties);
    }
    
    @Test
    @DisplayName("Deve lidar com propriedades vazias")
    void testEmptyProperties() {
        // Não deve lançar exceção com propriedade vazia
        assertDoesNotThrow(() -> configProperties.getProperty(""));
    }
    
    @Test
    @DisplayName("Deve lidar com propriedades inexistentes")
    void testInexistentProperties() {
        // Não deve lançar exceção com propriedade inexistente
        assertDoesNotThrow(() -> configProperties.getProperty("propriedade.inexistente.12345"));
    }
}
