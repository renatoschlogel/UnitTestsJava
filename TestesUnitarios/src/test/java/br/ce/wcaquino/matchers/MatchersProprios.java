package br.ce.wcaquino.matchers;

import java.util.Calendar;

public class MatchersProprios {

	public static DiaSemanaMatecher caiEm(Integer diaSemana) {
		return new DiaSemanaMatecher(diaSemana);
	}
	
	public static DiaSemanaMatecher caiEmUmaSegundaFeira() {
		return new DiaSemanaMatecher(Calendar.MONDAY);
	}
}
