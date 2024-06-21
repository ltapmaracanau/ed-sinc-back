package br.gov.ed_sinc.dto.input;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class TurmaInput {
	/*
	@NotBlank
	@Size(max = 100)
	private String nome;
	*/
	
	private String descricao;
	
	private List<EntidadeGenericaIdInput> usuarios;
}