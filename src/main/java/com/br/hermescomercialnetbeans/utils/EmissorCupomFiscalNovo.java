package com.br.hermescomercialnetbeans.utils;

import com.br.hermescomercialnetbeans.model.Venda;
import com.br.hermescomercialnetbeans.model.ItemVenda;
import org.apache.logging.log4j.LogManager;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Utilitário para emissão de cupom fiscal
 * @author marcos
 */
public class EmissorCupomFiscalNovo {
    
    private static final org.apache.logging.log4j.Logger logger = LogManager.getLogger(EmissorCupomFiscalNovo.class);
    
    private static final String SEPARADOR = "--------------------------------------------------";
    private static final DateTimeFormatter DATA_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
    
    /**
     * Emite um cupom fiscal para a venda informada
     */
    public void emitirCupom(Venda venda, List<ItemVenda> itens) throws Exception {
        if (venda == null || itens == null || itens.isEmpty()) {
            throw new IllegalArgumentException("Dados da venda inválidos!");
        }
        
        StringBuilder cupom = new StringBuilder();
        
        // Cabeçalho
        cupom.append(SEPARADOR).append("\n");
        cupom.append("           HERMES COMERCIAL PDV\n");
        cupom.append("       CNPJ: 00.000.000/0001-00\n");
        cupom.append("    Rua das Compras, 123 - Centro\n");
        cupom.append("      São Paulo - SP - CEP: 01234-567\n");
        cupom.append("          Tel: (11) 3333-3333\n");
        cupom.append(SEPARADOR).append("\n");
        
        // Informações da venda
        cupom.append("CUPOM FISCAL ELETRÔNICO\n");
        cupom.append("Nº: ").append(venda.getCodigo() != null ? venda.getCodigo() : "0001").append("\n");
        cupom.append("DATA: ").append(venda.getDataHora().format(DATA_FORMAT)).append("\n");
        cupom.append("OPERADOR: ").append(venda.getUsuarioNome() != null ? venda.getUsuarioNome() : "Sistema").append("\n");
        cupom.append("CLIENTE: ").append(venda.getClienteNome() != null ? venda.getClienteNome() : "CONSUMIDOR FINAL").append("\n");
        cupom.append(SEPARADOR).append("\n");
        
        // Itens da venda
        cupom.append(String.format("%-3s %-25s %5s %10s %10s\n", "QTD", "DESCRIÇÃO", "UNIT", "TOTAL", "DESC"));
        cupom.append(SEPARADOR).append("\n");
        
        for (ItemVenda item : itens) {
            String descricao = truncarTexto(item.getProdutoDescricao(), 25);
            String unitario = formatarValor(item.getValorUnitario());
            String total = formatarValor(item.getSubtotal());
            String desconto = item.getDesconto() > 0 ? formatarValor(item.getDesconto()) : "-";
            
            cupom.append(String.format("%3d %-25s %5s %10s %10s\n", 
                item.getQuantidade(), descricao, unitario, total, desconto));
        }
        
        cupom.append(SEPARADOR).append("\n");
        
        // Totais
        cupom.append(String.format("%-35s %10s\n", "SUBTOTAL:", formatarValor(venda.getValorTotal())));
        
        if (venda.getValorDesconto() > 0) {
            cupom.append(String.format("%-35s %10s\n", "DESCONTO:", formatarValor(venda.getValorDesconto())));
        }
        
        if (venda.getValorAcrescimo() > 0) {
            cupom.append(String.format("%-35s %10s\n", "ACRÉSCIMO:", formatarValor(venda.getValorAcrescimo())));
        }
        
        cupom.append(SEPARADOR).append("\n");
        cupom.append(String.format("%-35s %10s\n", "TOTAL A PAGAR:", formatarValor(venda.getValorFinal())));
        cupom.append(SEPARADOR).append("\n");
        
        // Forma de pagamento
        cupom.append("FORMA PAGAMENTO: ").append(venda.getTipoPagamento() != null ? venda.getTipoPagamento() : "DINHEIRO").append("\n");
        cupom.append(SEPARADOR).append("\n");
        
        // Mensagens fiscais
        cupom.append("          VENDA NÃO SUJEITA A ICMS\n");
        cupom.append("          CPF/CNPJ DO CONSUMIDOR:\n");
        cupom.append("                   NÃO INFORMADO\n");
        cupom.append(SEPARADOR).append("\n");
        
        // Rodapé
        cupom.append("          OBRIGADO PELA PREFERÊNCIA\n");
        cupom.append("          VOLTE SEMPRE!\n");
        cupom.append(SEPARADOR).append("\n");
        
        // Informações legais
        cupom.append("          SAT Nº: 123456\n");
        cupom.append("          VERSÃO: 1.0.0\n");
        cupom.append("          DATA/HORA EMISSÃO: ");
        cupom.append(LocalDateTime.now().format(DATA_FORMAT)).append("\n");
        cupom.append(SEPARADOR).append("\n");
        
        // Salvar cupom em arquivo
        salvarCupomEmArquivo(cupom.toString(), venda.getCodigo());
        
        // Imprimir cupom (simulação)
        imprimirCupom(cupom.toString());
        
        logger.info("Cupom fiscal emitido com sucesso para venda: " + venda.getCodigo());
    }
    
    /**
     * Emite cupom de cancelamento
     */
    public void emitirCupomCancelamento(Venda vendaOriginal) throws Exception {
        StringBuilder cupom = new StringBuilder();
        
        cupom.append(SEPARADOR).append("\n");
        cupom.append("           CUPOM DE CANCELAMENTO\n");
        cupom.append("           HERMES COMERCIAL PDV\n");
        cupom.append(SEPARADOR).append("\n");
        
        cupom.append("CUPOM ORIGINAL CANCELADO:\n");
        cupom.append("Nº: ").append(vendaOriginal.getCodigo()).append("\n");
        cupom.append("DATA: ").append(vendaOriginal.getDataHora().format(DATA_FORMAT)).append("\n");
        cupom.append("VALOR: ").append(formatarValor(vendaOriginal.getValorFinal())).append("\n");
        cupom.append(SEPARADOR).append("\n");
        
        cupom.append("MOTIVO: CANCELAMENTO PELO OPERADOR\n");
        cupom.append("DATA CANCELAMENTO: ");
        cupom.append(LocalDateTime.now().format(DATA_FORMAT)).append("\n");
        cupom.append("OPERADOR: ").append(vendaOriginal.getUsuarioNome()).append("\n");
        cupom.append(SEPARADOR).append("\n");
        
        cupom.append("          CANCELAMENTO CONFIRMADO\n");
        cupom.append(SEPARADOR).append("\n");
        
        salvarCupomEmArquivo(cupom.toString(), "CANCEL_" + vendaOriginal.getCodigo());
        imprimirCupom(cupom.toString());
        
        logger.info("Cupom de cancelamento emitido para venda: " + vendaOriginal.getCodigo());
    }
    
    /**
     * Emite relatório de fechamento de caixa
     */
    public void emitirRelatorioCaixa(BigDecimal totalVendas, BigDecimal totalSangrias, 
                                    BigDecimal totalSuprimentos, BigDecimal saldoFinal) throws Exception {
        StringBuilder relatorio = new StringBuilder();
        
        relatorio.append(SEPARADOR).append("\n");
        relatorio.append("        RELATÓRIO DE FECHAMENTO DE CAIXA\n");
        relatorio.append("           HERMES COMERCIAL PDV\n");
        relatorio.append(SEPARADOR).append("\n");
        
        relatorio.append("DATA: ").append(LocalDateTime.now().format(DATA_FORMAT)).append("\n");
        relatorio.append("OPERADOR: SISTEMA\n");
        relatorio.append(SEPARADOR).append("\n");
        
        relatorio.append("RESUMO DE MOVIMENTAÇÕES:\n");
        relatorio.append(String.format("%-25s %15s\n", "TOTAL VENDAS:", formatarValor(totalVendas)));
        relatorio.append(String.format("%-25s %15s\n", "TOTAL SANGRIAS:", formatarValor(totalSangrias)));
        relatorio.append(String.format("%-25s %15s\n", "TOTAL SUPRIMENTOS:", formatarValor(totalSuprimentos)));
        relatorio.append(SEPARADOR).append("\n");
        
        relatorio.append(String.format("%-25s %15s\n", "SALDO FINAL DO CAIXA:", formatarValor(saldoFinal)));
        relatorio.append(SEPARADOR).append("\n");
        
        relatorio.append("          CAIXA FECHADO COM SUCESSO\n");
        relatorio.append(SEPARADOR).append("\n");
        
        salvarCupomEmArquivo(relatorio.toString(), "CAIXA_" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")));
        imprimirCupom(relatorio.toString());
        
        logger.info("Relatório de fechamento de caixa emitido");
    }
    
    /**
     * Salva o cupom em um arquivo de texto
     */
    private void salvarCupomEmArquivo(String conteudo, String nomeArquivo) throws IOException {
        String nomeCompleto = "cupom_" + nomeArquivo + "_" + 
            LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + ".txt";
        
        try (FileWriter writer = new FileWriter("cupons/" + nomeCompleto);
             PrintWriter printer = new PrintWriter(writer)) {
            
            printer.print(conteudo);
            printer.flush();
            
            logger.info("Cupom salvo em arquivo: " + nomeCompleto);
        }
    }
    
    /**
     * Simula a impressão do cupom
     */
    private void imprimirCupom(String conteudo) {
        // Em um sistema real, aqui seria a integração com impressora térmica
        logger.info("=== INÍCIO DA IMPRESSÃO DO CUPOM ===");
        logger.info(conteudo);
        logger.info("=== FIM DA IMPRESSÃO DO CUPOM ===");
        
        // Simulação de impressão para console
        System.out.println("\n=== CUPOM FISCAL ===");
        System.out.println(conteudo);
        System.out.println("=== FIM DO CUPOM ===\n");
    }
    
    /**
     * Formata valor monetário
     */
    private String formatarValor(double valor) {
        return String.format("R$ %,.2f", valor);
    }
    
    /**
     * Formata valor monetário (BigDecimal)
     */
    private String formatarValor(BigDecimal valor) {
        return String.format("R$ %,.2f", valor);
    }
    
    /**
     * Trunca texto para caber no espaço especificado
     */
    private String truncarTexto(String texto, int tamanhoMaximo) {
        if (texto == null) return "";
        if (texto.length() <= tamanhoMaximo) return texto;
        return texto.substring(0, tamanhoMaximo - 3) + "...";
    }
    
    /**
     * Verifica se impressora está disponível
     */
    public boolean verificarImpressora() {
        // Simulação - em um sistema real verificaria hardware
        return true;
    }
    
    /**
     * Obtém status da impressora
     */
    public String getStatusImpressora() {
        // Simulação - em um sistema real verificaria status real
        return "PRONTA";
    }
    
    /**
     * Emite cupom de teste
     */
    public void emitirCupomTeste() throws Exception {
        Venda vendaTeste = new Venda();
        vendaTeste.setCodigo("TESTE001");
        vendaTeste.setDataHora(LocalDateTime.now());
        vendaTeste.setUsuarioNome("OPERADOR TESTE");
        vendaTeste.setClienteNome("CLIENTE TESTE");
        vendaTeste.setValorTotal(100.0);
        vendaTeste.setValorDesconto(10.0);
        vendaTeste.setValorFinal(90.0);
        vendaTeste.setTipoPagamento("DINHEIRO");
        
        ItemVenda itemTeste = new ItemVenda();
        itemTeste.setQuantidade(2);
        itemTeste.setProdutoDescricao("PRODUTO DE TESTE UNITÁRIO");
        itemTeste.setValorUnitario(50.0);
        itemTeste.setSubtotal(100.0);
        itemTeste.setDesconto(10.0);
        
        emitirCupom(vendaTeste, List.of(itemTeste));
        
        logger.info("Cupom de teste emitido com sucesso");
    }
}
