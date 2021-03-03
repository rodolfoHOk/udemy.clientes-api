package br.com.hioktec.clientes.util;

import java.math.BigDecimal;

import org.springframework.stereotype.Component;

@Component // para spring fazer a injeção do componente
public class BigDecimalConverter {
	
	public BigDecimal converter(String valor) {
		if(valor == null) {
			return null;
		}
		valor = valor.replace(".","").replace(",",".");
		return new BigDecimal(valor);
	}
}
