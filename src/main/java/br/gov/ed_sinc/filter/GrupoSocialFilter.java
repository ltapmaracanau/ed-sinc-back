package br.gov.ed_sinc.filter;

import br.gov.ed_sinc.model.enums.Status;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GrupoSocialFilter {
	private String nome;
	private Status status;
}