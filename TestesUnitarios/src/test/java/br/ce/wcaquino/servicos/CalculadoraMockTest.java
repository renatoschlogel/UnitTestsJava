package br.ce.wcaquino.servicos;

import java.math.BigDecimal;

import org.junit.Test;
import org.mockito.Mockito;

public class CalculadoraMockTest {

	@Test
	public void testName() throws Exception {
		
		
		Calculadora calculadora = Mockito.mock(Calculadora.class);
		
		Mockito.when(calculadora.somar(Mockito.eq(new BigDecimal(1)), Mockito.any(BigDecimal.class))).thenReturn(new BigDecimal(5));
		
		System.out.println(calculadora.somar(new BigDecimal(1), new BigDecimal(10)));
		
		
		
	}
}
