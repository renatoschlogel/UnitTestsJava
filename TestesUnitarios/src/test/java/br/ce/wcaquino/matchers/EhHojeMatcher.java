package br.ce.wcaquino.matchers;

import static br.ce.wcaquino.utils.DataUtils.isMesmaData;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

import br.ce.wcaquino.utils.DataUtils;

public class EhHojeMatcher extends TypeSafeMatcher<Date>  {
	
	private Integer quatidadeDias;
	
	public EhHojeMatcher(Integer quatidadeDias) {
		this.quatidadeDias = quatidadeDias;
	}

	public void describeTo(Description desc) {
		Date dataEsperada = DataUtils.obterDataComDiferencaDias(quatidadeDias);
		DateFormat format = new SimpleDateFormat("dd/MM/YYYY");
		desc.appendText(format.format(dataEsperada));
	}

	@Override
	protected boolean matchesSafely(Date data) {
		return isMesmaData(data, DataUtils.obterDataComDiferencaDias(this.quatidadeDias));
	}

}
