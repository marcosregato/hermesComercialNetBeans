package com.br.hermescomercialnetbeans.utils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ConvertDadoTest {
    
    private ConvertDado convertDado;
    
    @BeforeEach
    void setUp() {
        convertDado = new ConvertDado();
    }
    
    @Test
    @DisplayName("Deve criar ConvertDado sem erros")
    void testCriarConvertDado() {
        assertNotNull(convertDado);
    }
    
    @Test
    @DisplayName("Deve converter data válida")
    void testConvertDataValida() {
        // Data válida
        Date data = convertDado.convertData("01/12/2023");
        assertNotNull(data);
    }
    
    @Test
    @DisplayName("Deve lidar com data inválida")
    void testConvertDataInvalida() {
        // Data inválida - não deve lançar exceção
        assertDoesNotThrow(() -> convertDado.convertData("data_invalida"));
    }
    
    @Test
    @DisplayName("Deve lidar com data nula")
    void testConvertDataNula() {
        // Data nula - não deve lançar exceção
        assertDoesNotThrow(() -> convertDado.convertData(null));
    }
    
    @Test
    @DisplayName("Deve lidar com data vazia")
    void testConvertDataVazia() {
        // Data vazia - não deve lançar exceção
        assertDoesNotThrow(() -> convertDado.convertData(""));
    }
}
