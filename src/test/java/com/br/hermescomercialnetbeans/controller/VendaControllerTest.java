package com.br.hermescomercialnetbeans.controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class VendaControllerTest {
    
    private VendaController vendaController;
    private ByteArrayOutputStream outputStream;
    private PrintStream originalOut;
    
    @BeforeEach
    void setUp() {
        vendaController = new VendaController();
        
        // Configurar captura de saída do console
        outputStream = new ByteArrayOutputStream();
        originalOut = System.out;
        System.setOut(new PrintStream(outputStream));
    }
    
    @AfterEach
    void tearDown() {
        // Restaurar System.out original
        System.setOut(originalOut);
    }
    
    @Test
    @DisplayName("Deve criar VendaController sem erros")
    void testCriarVendaController() {
        assertNotNull(vendaController);
    }
    
    @Test
    @DisplayName("Deve finalizar compra com valores válidos")
    void testFinalizarCompraValoresValidos() {
        // Arrange
        double total = 100.50;
        
        // Act
        assertDoesNotThrow(() -> vendaController.finalizarCompra(null, total));
        
        // Assert
        String output = outputStream.toString();
        assertNotNull(output);
        assertTrue(output.contains("Finalizando compra"));
        assertTrue(output.contains("R$ 100.50"));
    }
    
    @Test
    @DisplayName("Deve finalizar compra com total zero")
    void testFinalizarCompraTotalZero() {
        // Arrange
        double total = 0.0;
        
        // Act
        assertDoesNotThrow(() -> vendaController.finalizarCompra(null, total));
        
        // Assert
        String output = outputStream.toString();
        assertTrue(output.contains("R$ 0.00"));
    }
    
    @Test
    @DisplayName("Deve finalizar compra com total negativo")
    void testFinalizarCompraTotalNegativo() {
        // Arrange
        double total = -50.0;
        
        // Act
        assertDoesNotThrow(() -> vendaController.finalizarCompra(null, total));
        
        // Assert
        String output = outputStream.toString();
        assertTrue(output.contains("R$ -50.00"));
    }
    
    @Test
    @DisplayName("Deve finalizar compra com table model nulo")
    void testFinalizarCompraTableModelNulo() {
        // Arrange
        double total = 100.0;
        
        // Act
        assertDoesNotThrow(() -> vendaController.finalizarCompra(null, total));
        
        // Assert
        String output = outputStream.toString();
        assertNotNull(output);
        assertTrue(output.contains("Finalizando compra"));
    }
    
    @Test
    @DisplayName("Deve lidar com valores grandes")
    void testFinalizarCompraValorGrande() {
        // Arrange
        double total = 999999.99;
        
        // Act
        assertDoesNotThrow(() -> vendaController.finalizarCompra(null, total));
        
        // Assert
        String output = outputStream.toString();
        assertTrue(output.contains("R$ 999999.99"));
    }
    
    @Test
    @DisplayName("Deve lidar com casas decimais")
    void testFinalizarCompraCasasDecimais() {
        // Arrange
        double total = 123.456789;
        
        // Act
        assertDoesNotThrow(() -> vendaController.finalizarCompra(null, total));
        
        // Assert
        String output = outputStream.toString();
        assertTrue(output.contains("R$ 123.456789"));
    }
    
    @Test
    @DisplayName("Deve processar múltiplas finalizações")
    void testMultiplasFinalizacoes() {
        double total = 100.0;
        // Act
        assertDoesNotThrow(() -> vendaController.finalizarCompra(null, total));
        assertDoesNotThrow(() -> vendaController.finalizarCompra(null, 200.0));
        assertDoesNotThrow(() -> vendaController.finalizarCompra(null, 300.0));
        
        // Assert
        String output = outputStream.toString();
        assertTrue(output.contains("R$ 100.00"));
        assertTrue(output.contains("R$ 200.00"));
        assertTrue(output.contains("R$ 300.00"));
    }
}
