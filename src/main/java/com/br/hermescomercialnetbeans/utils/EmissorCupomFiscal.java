package com.br.hermescomercialnetbeans.utils;

import com.br.hermescomercialnetbeans.model.Venda;
import com.br.hermescomercialnetbeans.model.ItemVenda;
import com.br.hermescomercialnetbeans.model.Pagamento;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class EmissorCupomFiscal {
    
    public static String gerarCupomFiscal(Venda venda, List<ItemVenda> itens, List<Pagamento> pagamentos) {
        StringBuilder cupom = new StringBuilder();
        
        // Cabeçalho
        cupom.append("----------------------------------------\n");
        cupom.append("        CUPOM FISCAL ELETRÔNICO        \n");
        cupom.append("----------------------------------------\n");
        cupom.append("HERMES COMERCIAL LTDA\n");
        cupom.append("CNPJ: 12.345.678/0001-90\n");
        cupom.append("IE: 123.456.789.111\n");
        cupom.append("Endereço: Rua das Flores, 123 - Centro\n");
        cupom.append("Cidade: São Paulo - SP\n");
        cupom.append("CEP: 01234-567\n");
        cupom.append("Fone: (11) 3456-7890\n");
        cupom.append("----------------------------------------\n");
        
        // Dados da venda
        cupom.append("CUPOM FISCAL\n");
        cupom.append("CUPOM: ").append(venda.getCodigo()).append("\n");
        cupom.append("DATA: ").append(venda.getDataHora().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"))).append("\n");
        cupom.append("CCF: ").append(String.format("%06d", venda.getId())).append("\n");
        cupom.append("COO: ").append(String.format("%06d", System.currentTimeMillis() % 999999)).append("\n");
        cupom.append("----------------------------------------\n");
        
        // Itens
        cupom.append("ITEM           CODIGO     DESCRIÇÃO\n");
        cupom.append("QTD         UN.    VL.UNIT(R$)  VL.TOTAL(R$)\n");
        cupom.append("----------------------------------------\n");
        
        for (int i = 0; i < itens.size(); i++) {
            ItemVenda item = itens.get(i);
            cupom.append(String.format("%03d", i + 1)).append(" ");
            
            // Código do produto
            String codigo = item.getProdutoCodigo() != null ? item.getProdutoCodigo() : "000000";
            cupom.append(String.format("%-12s", codigo));
            
            // Descrição truncada
            String descricao = item.getProdutoDescricao() != null ? item.getProdutoDescricao() : "PRODUTO";
            if (descricao.length() > 20) {
                descricao = descricao.substring(0, 20);
            }
            cupom.append(String.format("%-20s", descricao));
            cupom.append("\n");
            
            // Quantidade, unidade, valor unitário e total
            cupom.append(String.format("%9d", item.getQuantidade()));
            cupom.append("   UN     ");
            cupom.append(String.format("%10.2f", item.getValorUnitario()));
            cupom.append("   ");
            cupom.append(String.format("%10.2f", item.getSubtotal()));
            cupom.append("\n");
        }
        
        cupom.append("----------------------------------------\n");
        
        // Totais
        double totalItens = itens.stream().mapToDouble(ItemVenda::getSubtotal).sum();
        double totalDesconto = venda.getValorDesconto();
        double totalAcrescimo = venda.getValorAcrescimo();
        double totalFinal = venda.getValorFinal();
        
        cupom.append(String.format("%-30s %10.2f", "SUBTOTAL", totalItens)).append("\n");
        if (totalDesconto > 0) {
            cupom.append(String.format("%-30s %10.2f", "DESCONTO", totalDesconto)).append("\n");
        }
        if (totalAcrescimo > 0) {
            cupom.append(String.format("%-30s %10.2f", "ACRÉSCIMO", totalAcrescimo)).append("\n");
        }
        cupom.append("----------------------------------------\n");
        cupom.append(String.format("%-30s %10.2f", "TOTAL", totalFinal)).append("\n");
        cupom.append("----------------------------------------\n");
        
        // Formas de pagamento
        cupom.append("FORMA DE PAGAMENTO\n");
        for (Pagamento pag : pagamentos) {
            String forma = pag.getFormaPagamento() != null ? pag.getFormaPagamento().getDescricao() : "NÃO INFORMADO";
            cupom.append(String.format("%-25s %10.2f", forma, pag.getValor())).append("\n");
        }
        cupom.append("----------------------------------------\n");
        
        // Informações de tributação
        cupom.append("TRIBUTOS FEDERAIS\n");
        cupom.append("ICMS: R$ ").append(String.format("%.2f", totalFinal * 0.18)).append("\n");
        cupom.append("IPI: R$ ").append(String.format("%.2f", totalFinal * 0.10)).append("\n");
        cupom.append("PIS: R$ ").append(String.format("%.2f", totalFinal * 0.0065)).append("\n");
        cupom.append("COFINS: R$ ").append(String.format("%.2f", totalFinal * 0.03)).append("\n");
        cupom.append("----------------------------------------\n");
        
        // Rodapé
        cupom.append("OBRIGADO PELA PREFERÊNCIA\n");
        cupom.append("VOLTE SEMPRE!\n");
        cupom.append("----------------------------------------\n");
        cupom.append("Consulta via leitor óptico\n");
        cupom.append("CHAVE DE AUTENTICIDADE: ");
        cupom.append(gerarChaveAutenticacao(venda)).append("\n");
        cupom.append("----------------------------------------\n");
        cupom.append("DATA/HORA IMPRESSÃO: ");
        cupom.append(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"))).append("\n");
        cupom.append("----------------------------------------\n");
        
        return cupom.toString();
    }
    
    public static String gerarNotaFiscal(Venda venda, List<ItemVenda> itens, List<Pagamento> pagamentos) {
        StringBuilder nf = new StringBuilder();
        
        // Cabeçalho da NF
        nf.append("=================================================\n");
        nf.append("           NOTA FISCAL ELETRÔNICA           \n");
        nf.append("=================================================\n");
        nf.append("MODELO: 55          SÉRIE: 001\n");
        nf.append("NÚMERO: ").append(String.format("%09d", venda.getId())).append("\n");
        nf.append("DATA EMISSÃO: ").append(venda.getDataHora().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"))).append("\n");
        nf.append("-------------------------------------------------\n");
        
        // Emitente
        nf.append("EMITENTE:\n");
        nf.append("RAZÃO SOCIAL: HERMES COMERCIAL LTDA\n");
        nf.append("CNPJ: 12.345.678/0001-90\n");
        nf.append("IE: 123.456.789.111\n");
        nf.append("ENDEREÇO: Rua das Flores, 123 - Centro\n");
        nf.append("MUNICÍPIO: São Paulo - SP\n");
        nf.append("CEP: 01234-567\n");
        nf.append("TELEFONE: (11) 3456-7890\n");
        nf.append("-------------------------------------------------\n");
        
        // Destinatário
        nf.append("DESTINATÁRIO:\n");
        if (venda.getClienteNome() != null && !venda.getClienteNome().isEmpty()) {
            nf.append("NOME: ").append(venda.getClienteNome()).append("\n");
        } else {
            nf.append("NOME: CONSUMIDOR FINAL\n");
            nf.append("CNPJ/CPF: 000.000.000-00\n");
        }
        nf.append("-------------------------------------------------\n");
        
        // Itens
        nf.append("DETALHAMENTO DOS PRODUTOS/SERVIÇOS:\n");
        nf.append("-------------------------------------------------\n");
        nf.append("ITEM | CÓDIGO | DESCRIÇÃO                    | QTD | UN | VL.UNIT | VL.TOTAL\n");
        nf.append("-------------------------------------------------\n");
        
        for (int i = 0; i < itens.size(); i++) {
            ItemVenda item = itens.get(i);
            nf.append(String.format("%04d", i + 1)).append(" | ");
            
            String codigo = item.getProdutoCodigo() != null ? item.getProdutoCodigo() : "000000";
            nf.append(String.format("%-6s", codigo)).append(" | ");
            
            String descricao = item.getProdutoDescricao() != null ? item.getProdutoDescricao() : "PRODUTO";
            if (descricao.length() > 28) {
                descricao = descricao.substring(0, 28);
            }
            nf.append(String.format("%-28s", descricao)).append(" | ");
            
            nf.append(String.format("%3d", item.getQuantidade())).append(" | ");
            nf.append("UN | ");
            nf.append(String.format("%7.2f", item.getValorUnitario())).append(" | ");
            nf.append(String.format("%8.2f", item.getSubtotal())).append("\n");
        }
        
        nf.append("-------------------------------------------------\n");
        
        // Cálculos
        double totalItens = itens.stream().mapToDouble(ItemVenda::getSubtotal).sum();
        double totalDesconto = venda.getValorDesconto();
        double totalAcrescimo = venda.getValorAcrescimo();
        double totalFinal = venda.getValorFinal();
        
        nf.append("CÁLCULO DO IMPOSTO:\n");
        nf.append("BASE DE CÁLCULO DO ICMS: R$ ").append(String.format("%.2f", totalFinal)).append("\n");
        nf.append("VALOR DO ICMS: R$ ").append(String.format("%.2f", totalFinal * 0.18)).append("\n");
        nf.append("BASE DE CÁLCULO DO IPI: R$ ").append(String.format("%.2f", totalFinal)).append("\n");
        nf.append("VALOR DO IPI: R$ ").append(String.format("%.2f", totalFinal * 0.10)).append("\n");
        nf.append("-------------------------------------------------\n");
        nf.append("VALOR TOTAL DOS PRODUTOS: R$ ").append(String.format("%.2f", totalItens)).append("\n");
        if (totalDesconto > 0) {
            nf.append("VALOR DO DESCONTO: R$ ").append(String.format("%.2f", totalDesconto)).append("\n");
        }
        if (totalAcrescimo > 0) {
            nf.append("VALOR DO ACRÉSCIMO: R$ ").append(String.format("%.2f", totalAcrescimo)).append("\n");
        }
        nf.append("VALOR TOTAL DA NOTA: R$ ").append(String.format("%.2f", totalFinal)).append("\n");
        nf.append("-------------------------------------------------\n");
        
        // Pagamento
        nf.append("FORMA DE PAGAMENTO:\n");
        for (Pagamento pag : pagamentos) {
            String forma = pag.getFormaPagamento() != null ? pag.getFormaPagamento().getDescricao() : "NÃO INFORMADO";
            nf.append(forma).append(": R$ ").append(String.format("%.2f", pag.getValor())).append("\n");
        }
        nf.append("-------------------------------------------------\n");
        
        // Chave de acesso
        nf.append("CHAVE DE ACESSO: ").append(gerarChaveNFe(venda)).append("\n");
        nf.append("-------------------------------------------------\n");
        nf.append("CONSULTE A AUTENTICIDADE DA NF-E EM:\n");
        nf.append("http://www.nfe.fazenda.gov.br\n");
        nf.append("-------------------------------------------------\n");
        
        return nf.toString();
    }
    
    private static String gerarChaveAutenticacao(Venda venda) {
        // Gera uma chave de autenticidade simulada
        String base = venda.getCodigo() + venda.getId() + System.currentTimeMillis();
        return base.substring(0, Math.min(base.length(), 20));
    }
    
    private static String gerarChaveNFe(Venda venda) {
        // Gera uma chave de NFe simulada (44 dígitos)
        StringBuilder chave = new StringBuilder();
        chave.append("35"); // UF (SP)
        chave.append(String.format("%02d", LocalDateTime.now().getYear() % 100)); // Ano
        chave.append(String.format("%02d", LocalDateTime.now().getMonthValue())); // Mês
        chave.append("12345678"); // CNPJ emitente
        chave.append("55"); // Modelo
        chave.append("001"); // Série
        chave.append(String.format("%09d", venda.getId())); // Número
        chave.append("1"); // Tipo de emissão
        chave.append("123456789"); // Código numérico
        
        // Preencher com zeros até 44 dígitos
        while (chave.length() < 44) {
            chave.append("0");
        }
        
        return chave.substring(0, 44);
    }
    
    public static void imprimirCupom(String cupom) {
        try {
            // Simulação de impressão - em um sistema real enviaria para impressora fiscal
            System.out.println("=== IMPRIMINDO CUPOM FISCAL ===");
            System.out.println(cupom);
            System.out.println("=== FIM DA IMPRESSÃO ===");
        } catch (Exception e) {
            System.err.println("Erro ao imprimir cupom: " + e.getMessage());
        }
    }
    
    public static void salvarCupomArquivo(String cupom, String nomeArquivo) {
        try {
            // Simulação de salvamento em arquivo
            System.out.println("Cupom salvo em arquivo: " + nomeArquivo);
            System.out.println(cupom);
        } catch (Exception e) {
            System.err.println("Erro ao salvar cupom: " + e.getMessage());
        }
    }
    
    public static void emitirCupom(Venda venda, List<ItemVenda> itens) {
        try {
            // Simulação de emissão de cupom fiscal
            String cupom = gerarCupomFiscal(venda, itens, null);
            System.out.println("=== CUPOM FISCAL EMITIDO ===");
            System.out.println(cupom);
            System.out.println("=========================");
        } catch (Exception e) {
            System.err.println("Erro ao emitir cupom: " + e.getMessage());
        }
    }
}
