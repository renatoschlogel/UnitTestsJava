package br.ce.wcaquino.servicos;

import static br.ce.wcaquino.utils.DataUtils.adicionarDias;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import br.ce.wcaquino.daos.LocacaoDAO;
import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.exceptions.FilmeSemEstoqueException;
import br.ce.wcaquino.exceptions.LocadoraException;
import br.ce.wcaquino.utils.DataUtils;

public class LocacaoService {
	
	private LocacaoDAO locacaoDAO;
	private SPCService spcService;
	private EmailService emailService;
	
	public LocacaoService() {
	}
	
	public Locacao alugarFilme(Usuario usuario, List<Filme> filmes) throws LocadoraException, FilmeSemEstoqueException{
		
		if (usuario == null) {
			throw new LocadoraException("Usu�rio n�o informado!");
		}
		
		if (filmes == null || filmes.isEmpty()) {
			throw new LocadoraException("Nenhum filme foi informado!");
		}
		
		boolean possuiNegativacao = false;
		try {
			possuiNegativacao = spcService.possuiNegativacao(usuario);
		} catch (Exception e) {
			throw new LocadoraException("Problemas na Comunica��o com SPC");
		}
		
		if (possuiNegativacao) {
			throw new LocadoraException("Usu�rio Negativado!");
		}
		
		Double valorLocacao = calcularValorTotalLocacao(filmes);
		
		Locacao locacao = new Locacao();
		locacao.setFilmes(filmes);
		locacao.setUsuario(usuario);
		locacao.setDataLocacao(Calendar.getInstance().getTime());
		locacao.setValor(valorLocacao);

		//Entrega no dia seguinte
		Date dataEntrega = Calendar.getInstance().getTime();

		do {
			dataEntrega = adicionarDias(dataEntrega, 1);
		} while (DataUtils.verificarDiaSemana(dataEntrega, Calendar.SUNDAY));
		
		locacao.setDataRetorno(dataEntrega);
		
		//Salvando a locacao...	
		//TODO adicionar método para salvar
		locacaoDAO.salvar(locacao);
		
		return locacao;
	}

	private Double calcularValorTotalLocacao(List<Filme> filmes) throws FilmeSemEstoqueException {
		Double valorLocacao = 0.0;
		for (int index = 0; index < filmes.size(); index++) {
			
			Filme filme = filmes.get(index);
			
			if (filme.getEstoque() == 0) {
				throw new FilmeSemEstoqueException();
			}
			
			Double precoLocacao = valorLiquidoLocacaoFilme(filme, index + 1);

			valorLocacao+= precoLocacao;
		}
		return valorLocacao;
	}
	
	public void notificarAtrasos() {
		List<Locacao> locacoes = locacaoDAO.buscarLocacoesPendentes();
		
		for (Locacao locacao : locacoes) {
			if (locacao.isAtrasada()) {
				emailService.notificarUsuario(locacao.getUsuario());				
			}
		}
	}
	
	private Double valorLiquidoLocacaoFilme(Filme filme, int numeroFilme) {
		switch (numeroFilme) {
		case 3:	return filme.getPrecoLocacao() * 0.75;
		case 4: return filme.getPrecoLocacao() * 0.50;
		case 5: return filme.getPrecoLocacao() * 0.25;
		case 6:	return 0.0;
		default: return filme.getPrecoLocacao();
		}
				
	}

	
	public void prorrogarLocacao(Locacao locacao, int dias) {
		Locacao novaLocacao = new Locacao();
		novaLocacao.setUsuario(locacao.getUsuario());
		novaLocacao.setFilmes(locacao.getFilmes());
		novaLocacao.setDataLocacao(new Date());
		novaLocacao.setDataRetorno(DataUtils.obterDataComDiferencaDias(dias));
		novaLocacao.setValor(locacao.getValor() * dias);
		locacaoDAO.salvar(novaLocacao);;
	}
}