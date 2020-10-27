package br.ce.wcaquino.servicos;

import static br.ce.wcaquino.utils.DataUtils.adicionarDias;

import java.util.Date;
import java.util.List;

import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.exceptions.FilmeSemEstoqueException;
import br.ce.wcaquino.exceptions.LocadoraException;

public class LocacaoService {
	
	public Locacao alugarFilme(Usuario usuario, List<Filme> filmes) throws LocadoraException, FilmeSemEstoqueException{
		
		if (usuario == null) {
			throw new LocadoraException("Usuário não informado!");
		}
		
		if (filmes == null || filmes.isEmpty()) {
			throw new LocadoraException("Nenhum filme foi informado!");
		}
		
		Double valorLocacao = 0.0;
		
		
		for (int index = 0; index < filmes.size(); index++) {
			
			Filme filme = filmes.get(index);
			
			if (filme.getEstoque() == 0) {
				throw new FilmeSemEstoqueException();
			}
			
			Double precoLocacao = valorLiquidoLocacaoFilme(filme, index + 1);

			valorLocacao+= precoLocacao;
		}
	
		
		Locacao locacao = new Locacao();
		locacao.setFilmes(filmes);
		locacao.setUsuario(usuario);
		locacao.setDataLocacao(new Date());
		locacao.setValor(valorLocacao);

		//Entrega no dia seguinte
		Date dataEntrega = new Date();
		dataEntrega = adicionarDias(dataEntrega, 1);
		locacao.setDataRetorno(dataEntrega);
		
		//Salvando a locacao...	
		//TODO adicionar mÃ©todo para salvar
		
		return locacao;
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

}