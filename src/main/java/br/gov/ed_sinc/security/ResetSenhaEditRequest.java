package br.gov.ed_sinc.security;

import br.gov.ed_sinc.validator.validate.ValidateSenha;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResetSenhaEditRequest {

	@NotBlank
	@ValidateSenha
	private String senha;
	private String confirmarSenha;

}
