package br.gov.ed_sinc.dto.input;

import br.gov.ed_sinc.validator.validate.ValidateSenha;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SenhaEConfirmarSenhaInput {
	@ValidateSenha
	private String senha;
	private String confirmarSenha;
}