package br.ce.wcaquino.builders;

import br.ce.wcaquino.entidades.Filme;

public class FilmeBuilder {

	private Filme filme;
	
	private FilmeBuilder() {
	}
	
	public static FilmeBuilder umFilme() {
		FilmeBuilder filmeBuilder = new FilmeBuilder();
		filmeBuilder.filme = new Filme();
		filmeBuilder.filme.setNome("Filme 1");
		filmeBuilder.filme.setEstoque(2);
		filmeBuilder.filme.setPrecoLocacao(4.0);
		
		return filmeBuilder;
	}
	
	
	public static FilmeBuilder umFilmeSemEstoque() {
		FilmeBuilder filmeBuilder = new FilmeBuilder();
		filmeBuilder.filme = new Filme();
		filmeBuilder.filme.setNome("Filme 1");
		filmeBuilder.filme.setEstoque(0);
		filmeBuilder.filme.setPrecoLocacao(4.0);
		
		return filmeBuilder;
	}
	
	
	public Filme agora() {
		return this.filme;
	}

	public FilmeBuilder filmeSemEstoque() {
		this.filme.setEstoque(0);
		return this;
	}

	public FilmeBuilder comValor(Double precoLocacao) {
		this.filme.setPrecoLocacao(precoLocacao);
		return this;
	}
}
