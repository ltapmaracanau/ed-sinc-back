package br.gov.ed_sinc.dto;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonView;

import br.gov.ed_sinc.dto.view.UsuarioView;
import br.gov.ed_sinc.model.enums.Categoria;
import br.gov.ed_sinc.model.enums.Status;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class UsuarioDTO {
	@JsonView({UsuarioView.Resumo.class, UsuarioView.Retorno.class})
	private Long id;
	@JsonView({UsuarioView.Resumo.class, UsuarioView.Retorno.class})
	private String nome;
	@JsonView({UsuarioView.Resumo.class, UsuarioView.Retorno.class})
	private String email;
	@JsonView(UsuarioView.Resumo.class)
	private String cpf;
	@JsonView({UsuarioView.Resumo.class, UsuarioView.Retorno.class})
	private String telefone;
	@JsonView(UsuarioView.Resumo.class)
	private LocalDate dataNascimento;
	@JsonView({UsuarioView.Resumo.class, UsuarioView.Retorno.class})
	private List<Categoria> categorias;
	@JsonView(UsuarioView.Resumo.class)
	private boolean accountNonLocked;
	@JsonView({UsuarioView.Resumo.class, UsuarioView.Retorno.class})
	private Status status;
}
