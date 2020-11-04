package br.ce.wcaquino.servicos;

import static org.junit.Assert.*;

import java.math.BigDecimal;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;


public class CalculadoraMockTest {

	@Mock
	private Calculadora calcMock;

	@Spy
	private Calculadora calcSpy;
	
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		
	}
	
	@Test
	public void devoMostrarADifrerencaEntreMockESpy() throws Exception {
		
		Mockito.when(calcMock.somar(new BigDecimal(1), new BigDecimal(2))).thenReturn(new BigDecimal(3));
		Mockito.when(calcSpy.somar(new BigDecimal(1), new BigDecimal(2))).thenReturn(new BigDecimal(3));
		
		Mockito.doReturn(new BigDecimal(5)).when(calcSpy).somar(new BigDecimal(2), new BigDecimal(3));
		
		
		System.out.println("Mock: " +  calcMock.somar(new BigDecimal(1), new BigDecimal(2)));
		System.out.println("Spy: " +  calcSpy.somar(new BigDecimal(1), new BigDecimal(2)));
	}
	
	@Test
	public void testName() throws Exception {
		
		Calculadora calculadora = Mockito.mock(Calculadora.class);
		
		ArgumentCaptor<BigDecimal> argumentCaptor = ArgumentCaptor.forClass(BigDecimal.class);
		
		Mockito.when(calculadora.somar(argumentCaptor.capture(), argumentCaptor.capture())).thenReturn(new BigDecimal(5));
		
		Assert.assertEquals(new BigDecimal(5), calculadora.somar(new BigDecimal(1), new BigDecimal(10)));
		
		System.out.println(argumentCaptor.getAllValues());
		
	}
}
