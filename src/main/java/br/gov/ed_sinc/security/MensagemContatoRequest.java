package br.gov.ed_sinc.security;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MensagemContatoRequest {

	@NotBlank
	@Size(max = 1000)
	private String mensagem;
	@NotBlank
	@Size(max = 80)
	private String assunto;
	@NotBlank
	@Size(max = 255)
	@Email
	private String email;
	@NotBlank
	@Size(max = 255)
	private String nome;

}
