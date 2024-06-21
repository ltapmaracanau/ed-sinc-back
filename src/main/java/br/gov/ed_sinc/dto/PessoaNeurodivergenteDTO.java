package br.gov.ed_sinc.dto;

import com.fasterxml.jackson.annotation.JsonView;

import br.gov.ed_sinc.dto.view.PessoaNeurodivergenteView;
import br.gov.ed_sinc.model.enums.Status;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class PessoaNeurodivergenteDTO {
	@JsonView({PessoaNeurodivergenteView.Resumo.class})
	private Long id;
	@JsonView({PessoaNeurodivergenteView.Resumo.class})
	private String nome;
	@JsonView({PessoaNeurodivergenteView.Resumo.class})
	private String descricao;
	@JsonView({PessoaNeurodivergenteView.Resumo.class})
	private Status status;
}