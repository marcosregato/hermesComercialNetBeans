
package com.br.hermescomercialnetbeans.business.alerta;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

import com.br.hermescomercialnetbeans.dao.EstoqueDao;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MensagemAlertaBusiness {

	private EstoqueDao dao = new EstoqueDao();
    private static final Logger logger = LogManager.getLogger(MensagemAlertaBusiness.class);

	/**
	 * comparar a data da estoque com a data atual
	 * 
	 * @param data A data limite ou de comparação
	 * @return A data da compra se o produto for considerado encalhado
	 * */
	public String produtoEncalhado(String data) {

		try {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
			
			LocalDate dataAtual = LocalDate.now();
			String dataCompraStr = dao.getDataCompraEstoque();
			
			if (dataCompraStr == null) {
				return null;
			}

			LocalDate dataDoBanco = LocalDate.parse(dataCompraStr, formatter);
			LocalDate dataLimite = LocalDate.parse(data, formatter);

            if(dataDoBanco.isBefore(dataLimite)){
                return dataCompraStr;
            }

			long days = ChronoUnit.DAYS.between(dataDoBanco, dataAtual);
			
			if (days > 30) { // Exemplo de regra para considerar encalhado
				return dataCompraStr;
			}

		} catch (Exception e) {
            logger.error("Erro ao processar alerta de produto encalhado: " + e.getMessage(), e);
		}
        return null;
	}

}
