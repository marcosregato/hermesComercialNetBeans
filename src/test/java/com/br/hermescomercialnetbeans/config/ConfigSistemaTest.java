package com.br.hermescomercialnetbeans.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ConfigSistemaTest {
    
    private ConfigSistema configSistema;
    
    @BeforeEach
    void setUp() {
        configSistema = new ConfigSistema();
    }
    
    @Test
    @DisplayName("Deve criar ConfigSistema sem erros")
    void testCriarConfigSistema() {
        assertNotNull(configSistema);
    }
    
    @Test
    @DisplayName("Deve retornar nome do banco de dados correto")
    void testGetDataBase() {
        String databaseName = configSistema.getDataBase();
        assertNotNull(databaseName);
        assertEquals("hermescomercial", databaseName);
    }
    
    @Test
    @DisplayName("Deve retornar valor consistente do banco de dados")
    void testGetDataBaseConsistencia() {
        String database1 = configSistema.getDataBase();
        String database2 = configSistema.getDataBase();
        assertEquals(database1, database2);
        assertFalse(database1.isEmpty());
    }
}
