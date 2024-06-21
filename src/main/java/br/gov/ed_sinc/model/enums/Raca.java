package br.gov.ed_sinc.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Raca {
	Branca("Branca"), 
	Parda("Parda"),
	Preta("Preta"),
	Amarela("Amarela"),
	Indigena("Indígena");

	private String descricao;
}
