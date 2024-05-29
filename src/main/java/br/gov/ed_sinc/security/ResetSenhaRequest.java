package br.gov.ed_sinc.security;

import br.gov.ed_sinc.validator.validate.ValidateEmailPresente;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResetSenhaRequest {

	@ValidateEmailPresente
	private String email;

}
