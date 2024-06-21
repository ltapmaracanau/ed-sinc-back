package br.gov.ed_sinc.dto;

import com.fasterxml.jackson.annotation.JsonView;

import br.gov.ed_sinc.dto.view.GrupoSocialView;
import br.gov.ed_sinc.model.enums.Status;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class GrupoSocialDTO {
	@JsonView({GrupoSocialView.Resumo.class})
	private Long id;
	@JsonView({GrupoSocialView.Resumo.class})
	private String nome;
	@JsonView({GrupoSocialView.Resumo.class})
	private String descricao;
	@JsonView({GrupoSocialView.Resumo.class})
	private Status status;
}