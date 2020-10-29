package br.ce.wcaquino.servicos;

import static br.ce.wcaquino.builders.FilmeBuilder.umFilme;
import static br.ce.wcaquino.builders.FilmeBuilder.umFilmeSemEstoque;
import static br.ce.wcaquino.builders.UsuarioBuilder.umUsuario;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import org.junit.rules.ExpectedException;

import br.ce.wcaquino.builders.FilmeBuilder;
import br.ce.wcaquino.builders.UsuarioBuilder;
import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.exceptions.FilmeSemEstoqueException;
import br.ce.wcaquino.exceptions.LocadoraException;
import br.ce.wcaquino.matchers.DiaSemanaMatcher;
import br.ce.wcaquino.matchers.MatchersProprios;
import br.ce.wcaquino.utils.DataUtils;
import buildermaster.BuilderMaster;

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
		
		Assume.assumeFalse(DataUtils.verificarDiaSemana(new Date(), Calendar.SATURDAY));
		
		//cenario
		Usuario usuario = umUsuario().agora();
		List<Filme> filmes = new ArrayList();	
		Filme filme = umFilme().comValor(5.0).agora();
		filmes.add(filme);
		//acao
		Locacao locacao = service.alugarFilme(usuario, filmes);

		error.checkThat(locacao.getValor(), is(CoreMatchers.equalTo(5.0)));
		error.checkThat(locacao.getDataLocacao(), MatchersProprios.ehHoje());
		
		error.checkThat(locacao.getDataRetorno(), MatchersProprios.ehHojeComDiferencaDias(1));
		error.checkThat(locacao.getDataRetorno(), MatchersProprios.ehHojeComDiferencaDias(1));
	}
	
	@Test(expected = FilmeSemEstoqueException.class)
	public void deveLancarExecaoAoTentarAlugarUmFilmeSemEstoque() throws Exception {
		Usuario usuario = UsuarioBuilder.umUsuario().agora();
		List<Filme> filmes = new ArrayList();
		Filme filme = umFilmeSemEstoque().agora();
		filmes.add(filme);
		service.alugarFilme(usuario, filmes);
	}
	
	@Test
	public void naoDeveAlugarFilmeSemEstoque() throws FilmeSemEstoqueException {
		List<Filme> filmes = new ArrayList();
		Filme filme = umFilme().agora();;
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
		Filme filme = umFilme().agora();;
		filmes.add(filme);
		try {
			service.alugarFilme(null, filmes);
			Assert.fail();
		} catch (LocadoraException e) {
			assertThat(e.getMessage(), is("Usuário não informado!"));
			
		}
	}

	@Test
	public void naoDeveDevolverFilmeNoDomingo() throws Exception {

		Assume.assumeTrue(DataUtils.verificarDiaSemana(new Date(), Calendar.SATURDAY));

		Usuario usuario = UsuarioBuilder.umUsuario().agora();

		List<Filme> filmes = new ArrayList();
		filmes.add(new Filme("Filme 1", 2, 4.0));

		Locacao locacao = service.alugarFilme(usuario, filmes);

		assertThat(locacao.getDataRetorno(), new DiaSemanaMatcher(Calendar.MONDAY));
		assertThat(locacao.getDataRetorno(), MatchersProprios.caiEmUmaSegundaFeira());
		

	}
	
}
