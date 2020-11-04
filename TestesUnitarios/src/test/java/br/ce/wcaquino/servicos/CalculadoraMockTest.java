package br.ce.wcaquino.servicos;

import java.math.BigDecimal;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import br.ce.wcaquino.entidades.Locacao;


public class CalculadoraMockTest {

	@Test
	public void testName() throws Exception {
		
		Calculadora calculadora = Mockito.mock(Calculadora.class);
		
		ArgumentCaptor<BigDecimal> argumentCaptor = ArgumentCaptor.forClass(BigDecimal.class);
		
		Mockito.when(calculadora.somar(argumentCaptor.capture(), argumentCaptor.capture())).thenReturn(new BigDecimal(5));
		
		Assert.assertEquals(new BigDecimal(5), calculadora.somar(new BigDecimal(1), new BigDecimal(10)));
		
		System.out.println(argumentCaptor.getAllValues());
		
	}
}
