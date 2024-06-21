package br.gov.ed_sinc.dto;

import com.fasterxml.jackson.annotation.JsonView;

import br.gov.ed_sinc.dto.view.PessoaComDeficienciaView;
import br.gov.ed_sinc.model.enums.Status;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class PessoaComDeficienciaDTO {
	@JsonView({PessoaComDeficienciaView.Resumo.class})
	private Long id;
	@JsonView({PessoaComDeficienciaView.Resumo.class})
	private String nome;
	@JsonView({PessoaComDeficienciaView.Resumo.class})
	private String descricao;
	@JsonView({PessoaComDeficienciaView.Resumo.class})
	private Status status;
}