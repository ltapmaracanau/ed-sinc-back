package br.gov.ed_sinc.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Status {
	Ativo("Ativo"), 
	Arquivado("Arquivado"),
	Bloqueado("Bloqueado"),
	Inativo("Inativo");

	private String descricao;

}
