package br.ce.wcaquino.servicos;

import java.math.BigDecimal;

public class Calculadora {

	public BigDecimal somar(BigDecimal a, BigDecimal b) {
		return a.add(b);
	}

	public BigDecimal subtrair(BigDecimal a, BigDecimal b) {
		return a.subtract(b);
	}

	public BigDecimal dividir(BigDecimal a, BigDecimal b) {
		
		if (b.compareTo(BigDecimal.ZERO) == 0) {
			throw new NaoPodeDividirPorZeroException();
		}
		return a.divide(b);
	}

}
