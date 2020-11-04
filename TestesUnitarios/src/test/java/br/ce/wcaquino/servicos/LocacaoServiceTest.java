package br.ce.wcaquino.servicos;

import static br.ce.wcaquino.builders.FilmeBuilder.umFilme;
import static br.ce.wcaquino.builders.FilmeBuilder.umFilmeSemEstoque;
import static br.ce.wcaquino.builders.LocacaoBuilder.umaLocacao;
import static br.ce.wcaquino.builders.UsuarioBuilder.umUsuario;
import static br.ce.wcaquino.matchers.MatchersProprios.ehHoje;
import static br.ce.wcaquino.matchers.MatchersProprios.ehHojeComDiferencaDias;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
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
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import br.ce.wcaquino.builders.UsuarioBuilder;
import br.ce.wcaquino.daos.LocacaoDAO;
import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.exceptions.FilmeSemEstoqueException;
import br.ce.wcaquino.exceptions.LocadoraException;
import br.ce.wcaquino.matchers.DiaSemanaMatcher;
import br.ce.wcaquino.matchers.MatchersProprios;
import br.ce.wcaquino.utils.DataUtils;

@RunWith(PowerMockRunner.class)
@PrepareForTest({LocacaoService.class})
public class LocacaoServiceTest {
	
	@InjectMocks
	private LocacaoService service;
	
	@Mock
	private LocacaoDAO locacaoDAO;
	
	@Mock
	private SPCService spcService;
	
	@Mock
	private EmailService emailService;
	
	@Rule
	public ErrorCollector error = new ErrorCollector();
	
	@Rule
	public ExpectedException exception = ExpectedException.none();
	
	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		service = PowerMockito.spy(service);
	}
	
	@Test
	public void deveAlugarFilme() throws Exception {
		
		//cenario
		Usuario usuario = umUsuario().agora();
		List<Filme> filmes = new ArrayList();	
		Filme filme = umFilme().comValor(5.0).agora();
		filmes.add(filme);
		
//		PowerMockito.whenNew(Date.class).withNoArguments().thenReturn(DataUtils.obterData(06, 11, 2020));
		
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DAY_OF_MONTH, 06);
		calendar.set(Calendar.MONTH, Calendar.NOVEMBER);
		calendar.set(Calendar.YEAR, 2020);
		PowerMockito.mockStatic(Calendar.class);
		PowerMockito.when(Calendar.getInstance()).thenReturn(calendar);
		
		//acao
		
		Locacao locacao = service.alugarFilme(usuario, filmes);

		error.checkThat(locacao.getValor(), is(CoreMatchers.equalTo(5.0)));
//		error.checkThat(locacao.getDataLocacao(), MatchersProprios.ehHoje());
//		error.checkThat(locacao.getDataRetorno(), MatchersProprios.ehHojeComDiferencaDias(1));
		error.checkThat(DataUtils.isMesmaData(locacao.getDataLocacao(), DataUtils.obterData(06, 11, 2020)), is(true));
		error.checkThat(DataUtils.isMesmaData(locacao.getDataRetorno(), DataUtils.obterData(07, 11, 2020)), is(true));
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

		Usuario usuario = UsuarioBuilder.umUsuario().agora();

		List<Filme> filmes = new ArrayList();
		filmes.add(new Filme("Filme 1", 2, 4.0));
		
//		PowerMockito.whenNew(Date.class).withNoArguments().thenReturn(DataUtils.obterData(07, 11, 2020));
		
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DAY_OF_MONTH, 07);
		calendar.set(Calendar.MONTH, Calendar.NOVEMBER);
		calendar.set(Calendar.YEAR, 2020);
		PowerMockito.mockStatic(Calendar.class);
		PowerMockito.when(Calendar.getInstance()).thenReturn(calendar);

		Locacao locacao = service.alugarFilme(usuario, filmes);

		assertThat(locacao.getDataRetorno(), new DiaSemanaMatcher(Calendar.MONDAY));
		assertThat(locacao.getDataRetorno(), MatchersProprios.caiEmUmaSegundaFeira());
		
		
		PowerMockito.verifyStatic(Mockito.times(2));
		Calendar.getInstance();
//		PowerMockito.verifyNew(Date.class, Mockito.times(2)).withNoArguments();
	}
	
	@Test
	public void naoDeveAlugarFilmeParaNegativadoSPC() throws Exception {
		
		Usuario usuario = umUsuario().agora();
		List<Filme> filmes = Arrays.asList(umFilme().agora());
		
		Mockito.when(spcService.possuiNegativacao(Mockito.any(Usuario.class))).thenReturn(true);
		
		try {
			service.alugarFilme(usuario, filmes);
			Assert.fail();
		} catch (LocadoraException e) {
			Assert.assertThat(e.getMessage(), is("Usuário Negativado!"));
		} 
		Mockito.verify(spcService).possuiNegativacao(usuario);
	}
	
	@Test
	public void deveEnviarEmailParaLocacoesAtrasadas() throws Exception {
		
		Usuario usuario = umUsuario().agora();
		Usuario usuarioEmDia = umUsuario().comNome("Usuario em dia").agora();
		Usuario usuario3 = umUsuario().comNome("Usuario 3").agora();
	
		List<Locacao> locacoes = Arrays.asList(umaLocacao().comUsuario(usuario)
														   .atrasado()
				                                           .agora(),
				                               umaLocacao().comUsuario(usuarioEmDia)
				                                           .agora(),
				                               umaLocacao().comUsuario(usuario3)
				                               			   .atrasado()
				                                           .agora(),
				                               umaLocacao().comUsuario(usuario3)
				                               			   .atrasado()
				                                           .agora()       
				                                             );
		Mockito.when(locacaoDAO.buscarLocacoesPendentes()).thenReturn(locacoes);
		
		service.notificarAtrasos();
		Mockito.verify(emailService, Mockito.times(3)).notificarUsuario(Mockito.any(Usuario.class));;
		Mockito.verify(emailService).notificarUsuario(usuario);
		Mockito.verify(emailService, Mockito.atLeastOnce()).notificarUsuario(usuario3);
		Mockito.verify(emailService, Mockito.never()).notificarUsuario(usuarioEmDia);
		Mockito.verifyNoMoreInteractions(emailService);
	}
	
	@Test
	public void deveTratarErroSPC() throws Exception {
		
		Usuario usuario = umUsuario().agora();
		List<Filme> filmes = Arrays.asList(umFilme().agora());
		
		Mockito.when(spcService.possuiNegativacao(usuario)).thenThrow(new Exception("Problemas na Comunicação com SPC"));
		
		exception.expect(LocadoraException.class);
		exception.expectMessage("Problemas na Comunicação com SPC");
		service.alugarFilme(usuario, filmes);
		
	}
	
	@Test
	public void deveProrrogarUmaLocacao() throws Exception {
		
		Locacao locacao = umaLocacao().agora();
		
		service.prorrogarLocacao(locacao, 3);
		
		ArgumentCaptor<Locacao> argumentCaptor = ArgumentCaptor.forClass(Locacao.class);
		Mockito.verify(locacaoDAO).salvar(argumentCaptor.capture());
		Locacao locacaoRetorno = argumentCaptor.getValue();
		
		error.checkThat(locacaoRetorno.getValor(), is(12.0));
		error.checkThat(locacaoRetorno.getDataLocacao(), ehHoje());
		error.checkThat(locacaoRetorno.getDataRetorno(), ehHojeComDiferencaDias(3));
		
		
	}
	
	@Test
	public void deveAlugarFilmeSemCalcularValor() throws Exception {
		Usuario usuario = umUsuario().agora();
		List<Filme> filmes = Arrays.asList(umFilme().agora());
		
		PowerMockito.doReturn(1.0).when(service, "calcularValorTotalLocacao", filmes);
		
		Locacao locacao = service.alugarFilme(usuario, filmes);
		
		assertThat(locacao.getValor(), is(1.0));
		PowerMockito.verifyPrivate(service).invoke("calcularValorTotalLocacao", filmes);
	}
}
