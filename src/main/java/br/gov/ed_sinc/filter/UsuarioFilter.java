package br.gov.ed_sinc.filter;

import br.gov.ed_sinc.model.enums.Categoria;
import br.gov.ed_sinc.model.enums.Status;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UsuarioFilter {
	private String nome;
	private String email;
	private Status status;
	private Categoria categoria;
}