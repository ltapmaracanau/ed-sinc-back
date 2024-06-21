package br.gov.ed_sinc.dto.input;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class PoloInput {
	@NotBlank
	@Size(max = 100)
	private String nome;
	
	private String descricao;

}