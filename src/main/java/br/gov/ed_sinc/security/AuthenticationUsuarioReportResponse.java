package br.gov.ed_sinc.security;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationUsuarioReportResponse {

	@Builder.Default
	private Integer numErros = -1;
	@Builder.Default
	private Boolean usuarioBloqueado = false;
}
