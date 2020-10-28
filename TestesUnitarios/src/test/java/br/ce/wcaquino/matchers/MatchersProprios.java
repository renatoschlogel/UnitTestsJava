package br.ce.wcaquino.matchers;

import java.util.Calendar;

public class MatchersProprios {

	public static DiaSemanaMatcher caiEm(Integer diaSemana) {
		return new DiaSemanaMatcher(diaSemana);
	}
	
	public static DiaSemanaMatcher caiEmUmaSegundaFeira() {
		return new DiaSemanaMatcher(Calendar.MONDAY);
	}

	public static EhHojeMatcher ehHojeComDiferencaDias(Integer dias) {
		return new EhHojeMatcher(dias);
	}
	
	public static EhHojeMatcher ehHoje() {
		return new EhHojeMatcher(0);
	}
}
