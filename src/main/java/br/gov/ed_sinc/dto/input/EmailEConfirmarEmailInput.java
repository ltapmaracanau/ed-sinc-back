package br.gov.ed_sinc.dto.input;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmailEConfirmarEmailInput {
	@Email
	@NotBlank
	private String email;
	private String confirmarEmail;
}
