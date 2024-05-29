package br.gov.ed_sinc.exception;

import java.time.LocalDate;
import java.util.List;

import lombok.Builder;
import lombok.Getter;

//@JsonInclude(Include.NON_NULL)
@Getter
@Builder
public class Problema {

	private Integer status;
	private LocalDate dataHora;
	private String titulo;
	private String mensagem;
	private String detalhe;
	private String tipo;
	private List<Campo> campos;
	
	@Getter
	@Builder
	public static class Campo {
		
		private String nome;
		private String mensagem;
		
	}
	
}
