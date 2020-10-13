package br.ce.wcaquino.servicos;

import java.math.BigDecimal;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class CalculadoraTest {

	private Calculadora calculadora;
	
	@Before
	public void setup() {
		calculadora = new Calculadora();
	}
	
	@Test
	public void deveSomarDoisValores() {
		BigDecimal a = new BigDecimal(6);
		BigDecimal b = new BigDecimal(5);
		BigDecimal resuldadoEsperado = new BigDecimal(11);
		
		BigDecimal resultado = calculadora.somar(a, b);
		
		Assert.assertTrue(resultado.compareTo(resuldadoEsperado) == 0);
	}
	
	@Test
	public void deveSubtrairDoisValores() {
		BigDecimal a = new BigDecimal(6);
		BigDecimal b = new BigDecimal(5);
		BigDecimal resuldadoEsperado = new BigDecimal(1);
		
		BigDecimal resultado = calculadora.subtrair(a, b);
		
		Assert.assertTrue(resultado.compareTo(resuldadoEsperado) == 0);
	}
	
	@Test
	public void deveDividirDoisValores() {
		BigDecimal a = new BigDecimal(5);
		BigDecimal b = new BigDecimal(2);
		BigDecimal resuldadoEsperado = new BigDecimal(2.5);
		
		BigDecimal resultado = calculadora.dividir(a, b);
		
		Assert.assertTrue(resultado.compareTo(resuldadoEsperado) == 0);
	}
	
	@Test(expected = NaoPodeDividirPorZeroException.class)
	public void deveLancarEceptionAoDividirPorZero() {
		BigDecimal a = new BigDecimal(5);
		BigDecimal b = new BigDecimal(0);
		
		calculadora.dividir(a, b);
	}
	
	
}
