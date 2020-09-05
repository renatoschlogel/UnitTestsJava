package br.ce.wcaquino.servicos;

import org.junit.Assert;
import org.junit.Test;

import br.ce.wcaquino.entidades.Usuario;


public class AssertTest {
	
	@Test
	public void test() {
		Assert.assertTrue(true);
		Assert.assertFalse(false);
		
		Assert.assertEquals(1, 1);
		Assert.assertEquals(0.55, 0.551, 0.01);
		
		int i = 1;
		Integer i2 = 1;
		Assert.assertEquals(i, i2.intValue());
		
		Assert.assertEquals("teste", "teste");
		Assert.assertTrue("Teste".equalsIgnoreCase("Teste"));
		
		Usuario usuario = new Usuario("Renato");
		Usuario usuario2 = new Usuario("Renato");
		Usuario usuario3 = usuario2;
		Assert.assertEquals(usuario, usuario2);
		Assert.assertSame(usuario2, usuario3);
		
	}

}
