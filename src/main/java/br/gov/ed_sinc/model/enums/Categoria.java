package br.gov.ed_sinc.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Categoria {
	
	Administrador("Administrador"), 
	Coordenador("Coordenador"),
	Mentor("Mentor"),
	Aluno("Aluno");

	private String descricao;
	
}