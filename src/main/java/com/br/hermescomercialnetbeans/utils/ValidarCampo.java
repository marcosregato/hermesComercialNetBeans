/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.br.hermescomercialnetbeans.utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.InputMismatchException;



/**
 *
 * @author marcos
 */
public class ValidarCampo {


	// https://lincolnminto.wordpress.com/2015/03/09/validacao-de-campos-javafx-validation-fields-javafx/


	public Boolean campoVazio(String login, String senha){
		if((login.isEmpty()) && (senha.isEmpty())){
			//toolTip.setStyle("-fx-background-color: linear-gradient(#FF6B6B , #FFA6A6 );"
			//		+ " -fx-font-weight: bold;");

			return true;
		}
		return false;

	}

	public String campoData(String valor){
		if (valor == null || valor.isEmpty()) {
			return null;
		}
		try {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
			LocalDate data = LocalDate.parse(valor, formatter);
			return String.valueOf(data);
		} catch (DateTimeParseException e) {
			return null;
		}
	}

	// https://www.devmedia.com.br/validando-o-cnpj-em-uma-aplicacao-java/22374
	public boolean isCNPJ(String CNPJ) {
		CNPJ = removeCaracteresEspeciais(CNPJ);
		if (CNPJ.equals("00000000000000") || CNPJ.equals("11111111111111") ||
				CNPJ.equals("22222222222222") || CNPJ.equals("33333333333333") ||
				CNPJ.equals("44444444444444") || CNPJ.equals("55555555555555") ||
				CNPJ.equals("66666666666666") || CNPJ.equals("77777777777777") ||
				CNPJ.equals("88888888888888") || CNPJ.equals("99999999999999") ||
				(CNPJ.length() != 14))
			return(false);

		char dig13, dig14;
		int sm, i, r, num, peso;

		try {
			sm = 0;
			peso = 2;
			for (i=11; i>=0; i--) {
				num = CNPJ.charAt(i) - 48;
				sm = sm + (num * peso);
				peso = peso + 1;
				if (peso == 10)
					peso = 2;
			}

			r = sm % 11;
			if ((r == 0) || (r == 1))
				dig13 = '0';
			else dig13 = (char)((11-r) + 48);

			sm = 0;
			peso = 2;
			for (i=12; i>=0; i--) {
				num = CNPJ.charAt(i)- 48;
				sm = sm + (num * peso);
				peso = peso + 1;
				if (peso == 10)
					peso = 2;
			}

			r = sm % 11;
			if ((r == 0) || (r == 1))
				dig14 = '0';
			else dig14 = (char)((11-r) + 48);

			return (dig13 == CNPJ.charAt(12)) && (dig14 == CNPJ.charAt(13));
		} catch (InputMismatchException erro) {
			return(false);
		}
	}

	private String removeCaracteresEspeciais(String doc) {
		if (doc.contains(".")) {
			doc = doc.replace(".", "");
		}
		if (doc.contains("-")) {
			doc = doc.replace("-", "");
		}
		if (doc.contains("/")) {
			doc = doc.replace("/", "");
		}
		return doc;
	}

	public boolean isCPF(String CPF) {

		CPF = removeCaracteresEspeciais(CPF);

		// considera-se erro CPF's formados por uma sequencia de numeros iguais
		if (CPF.equals("00000000000") || CPF.equals("11111111111") || CPF.equals("22222222222") || CPF.equals("33333333333") || CPF.equals("44444444444") || CPF.equals("55555555555") || CPF.equals("66666666666") || CPF.equals("77777777777") || CPF.equals("88888888888") || CPF.equals("99999999999") || (CPF.length() != 11))
			return (false);

		char dig10, dig11;
		int sm, i, r, num, peso;

		// "try" - protege o codigo para eventuais erros de conversao de tipo (int)
		try {
			// Calculo do 1o. Digito Verificador
			sm = 0;
			peso = 10;
			for (i = 0; i < 9; i++) {
				// converte o i-esimo caractere do CPF em um numero:
				// por exemplo, transforma o caractere '0' no inteiro 0
				// (48 eh a posicao de '0' na tabela ASCII)
				num = CPF.charAt(i) - 48;
				sm = sm + (num * peso);
				peso = peso - 1;
			}

			r = 11 - (sm % 11);
			if ((r == 10) || (r == 11))
				dig10 = '0';
			else
				dig10 = (char) (r + 48); // converte no respectivo caractere numerico

			// Calculo do 2o. Digito Verificador
			sm = 0;
			peso = 11;
			for (i = 0; i < 10; i++) {
				num = CPF.charAt(i) - 48;
				sm = sm + (num * peso);
				peso = peso - 1;
			}

			r = 11 - (sm % 11);
			if ((r == 10) || (r == 11))
				dig11 = '0';
			else
				dig11 = (char) (r + 48);

			// Verifica se os digitos calculados conferem com os digitos informados.
			return (dig10 == CPF.charAt(9)) && (dig11 == CPF.charAt(10));
		} catch (InputMismatchException erro) {
			return (false);
		}
	}






}
