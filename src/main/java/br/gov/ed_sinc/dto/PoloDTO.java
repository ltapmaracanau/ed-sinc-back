package br.gov.ed_sinc.dto;

import com.fasterxml.jackson.annotation.JsonView;

import br.gov.ed_sinc.dto.view.PoloView;
import br.gov.ed_sinc.model.enums.Status;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class PoloDTO {
	@JsonView({PoloView.Resumo.class})
	private Long id;
	@JsonView({PoloView.Resumo.class})
	private String nome;
	@JsonView({PoloView.Resumo.class})
	private String descricao;
	@JsonView({PoloView.Resumo.class})
	private Status status;
}