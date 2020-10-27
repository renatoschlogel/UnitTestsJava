package br.ce.wcaquino.servicos;

import static br.ce.wcaquino.utils.DataUtils.isMesmaData;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

import java.beans.Transient;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import org.junit.rules.ExpectedException;

import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.exceptions.FilmeSemEstoqueException;
import br.ce.wcaquino.exceptions.LocadoraException;
import br.ce.wcaquino.utils.DataUtils;

public class LocacaoServiceTest {
	
	private LocacaoService service;
	
	@Rule
	public ErrorCollector error = new ErrorCollector();
	
	@Rule
	public ExpectedException exception = ExpectedException.none();
	
	@Before
	public void setUp() {
		service = new LocacaoService();
	}
	
	@Test
	public void deveAlugarFilme() throws Exception {
		//cenario
		Usuario usuario = new Usuario("Usuario 1");
		List<Filme> filmes = new ArrayList();	
		Filme filme = new Filme("Filme 1", 1, 5.0);
		filmes.add(filme);
		//acao
		Locacao locacao = service.alugarFilme(usuario, filmes);

		error.checkThat(locacao.getValor(), is(CoreMatchers.equalTo(5.0)));
		error.checkThat(isMesmaData(locacao.getDataLocacao(), new Date()), is(true));
		error.checkThat(isMesmaData(locacao.getDataRetorno(), DataUtils.obterDataComDiferencaDias(1)), is(true));
	}
	
	@Test(expected = FilmeSemEstoqueException.class)
	public void deveLancarExecaoAoTentarAlugarUmFilmeSemEstoque() throws Exception {
		Usuario usuario = new Usuario("Usuario 1");
		List<Filme> filmes = new ArrayList();
		Filme filme = new Filme("Filme 1", 0, 5.0);
		filmes.add(filme);
		service.alugarFilme(usuario, filmes);
	}
	
	@Test
	public void naoDeveAlugarFilmeSemEstoque() throws FilmeSemEstoqueException {
		List<Filme> filmes = new ArrayList();
		Filme filme = new Filme("Filme 1", 0, 5.0);
		filmes.add(filme);
		try {
			service.alugarFilme(null, filmes);
			Assert.fail();
		} catch (LocadoraException e) {
			assertThat(e.getMessage(), is("Usuário não informado!"));
			
		}
	}
	
	@Test
	public void naoDeveAlugarFilmeSemFilme() throws LocadoraException, FilmeSemEstoqueException {
		List<Filme> filmes = new ArrayList();
		Filme filme = new Filme("Filme 1", 0, 5.0);
		filmes.add(filme);
		try {
			service.alugarFilme(null, filmes);
			Assert.fail();
		} catch (LocadoraException e) {
			assertThat(e.getMessage(), is("Usuário não informado!"));
			
		}
	}
	
//	@Test
//	public void naoDeveDarDesconto() throws LocadoraException, FilmeSemEstoqueException {
//		List<Filme> filmes = new ArrayList();
//		Usuario usuario = new Usuario("Usuario 1");
//		Filme filme = new Filme("Filme 1", 0, 5.0);
//		filmes.add(filme);
//		Locacao alugarFilme = service.alugarFilme(usuario, filmes);
//				
//		
//	}
	
	@Test
	public void devePagar75PercetNoFilme3() throws FilmeSemEstoqueException, LocadoraException {
		Usuario usuario = new Usuario("Renato");
		
		List<Filme> filmes = new ArrayList();
		filmes.add(new Filme("Filme 1", 2, 4.0));
		filmes.add(new Filme("Filme 2", 2, 4.0));
		filmes.add(new Filme("Filme 3", 2, 4.0));

		Locacao locacao = service.alugarFilme(usuario, filmes);
		
		
		assertThat(locacao.getValor(), is(11.0));
	}
	
	@Test
	public void devePagar50PercetNoFilme4() throws FilmeSemEstoqueException, LocadoraException {
		Usuario usuario = new Usuario("Renato");
		
		List<Filme> filmes = new ArrayList();
		filmes.add(new Filme("Filme 1", 2, 4.0));
		filmes.add(new Filme("Filme 2", 2, 4.0));
		filmes.add(new Filme("Filme 3", 2, 4.0));
		filmes.add(new Filme("Filme 4", 2, 4.0));

		Locacao locacao = service.alugarFilme(usuario, filmes);
		
		
		assertThat(locacao.getValor(), is(13.0));
	}
	
	@Test
	public void devePagar25PercetNoFilme5() throws FilmeSemEstoqueException, LocadoraException {
		Usuario usuario = new Usuario("Renato");
		
		List<Filme> filmes = new ArrayList();
		filmes.add(new Filme("Filme 1", 2, 4.0));
		filmes.add(new Filme("Filme 2", 2, 4.0));
		filmes.add(new Filme("Filme 3", 2, 4.0));
		filmes.add(new Filme("Filme 4", 2, 4.0));
		filmes.add(new Filme("Filme 5", 2, 4.0));

		Locacao locacao = service.alugarFilme(usuario, filmes);
		
		
		assertThat(locacao.getValor(), is(14.0));
	}
	
	@Test
	public void deveSerDeGracaSextoFilme() throws FilmeSemEstoqueException, LocadoraException {
		Usuario usuario = new Usuario("Renato");
		
		List<Filme> filmes = new ArrayList();
		filmes.add(new Filme("Filme 1", 2, 4.0));
		filmes.add(new Filme("Filme 2", 2, 4.0));
		filmes.add(new Filme("Filme 3", 2, 4.0));
		filmes.add(new Filme("Filme 4", 2, 4.0));
		filmes.add(new Filme("Filme 5", 2, 4.0));
		filmes.add(new Filme("Filme 6", 2, 4.0));

		Locacao locacao = service.alugarFilme(usuario, filmes);
		
		
		assertThat(locacao.getValor(), is(14.0));
	}
	
	@Test
	public void naoDeveDevolverFilmeNoDomingo() throws Exception {
		Usuario usuario = new Usuario("Renato");
		
		List<Filme> filmes = new ArrayList();
		filmes.add(new Filme("Filme 1", 2, 4.0));
		
		Locacao locacao = service.alugarFilme(usuario, filmes);
		
	   boolean isSegundaFeira = DataUtils.verificarDiaSemana(locacao.getDataRetorno(), Calendar.MONDAY);
		
	   assertTrue(isSegundaFeira);
	}
	
	
}
