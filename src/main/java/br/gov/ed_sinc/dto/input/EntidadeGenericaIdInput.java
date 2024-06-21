package br.gov.ed_sinc.dto.input;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EntidadeGenericaIdInput {
	@NotNull
	private Long id;
}
