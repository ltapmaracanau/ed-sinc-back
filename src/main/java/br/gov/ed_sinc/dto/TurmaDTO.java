package br.gov.ed_sinc.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonView;

import br.gov.ed_sinc.dto.view.TurmaView;
import br.gov.ed_sinc.model.enums.Status;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class TurmaDTO {
	@JsonView({TurmaView.Resumo.class})
	private Long id;
	@JsonView({TurmaView.Resumo.class})
	private String nome;
	@JsonView({TurmaView.Resumo.class})
	private String descricao;
	@JsonView({TurmaView.Resumo.class})
	private Status status;
	@JsonView({TurmaView.Resumo.class})
	private Boolean exportado;
	@JsonView({TurmaView.Resumo.class})
	private List<UsuarioDTO> usuarios;
}