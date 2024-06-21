package br.gov.ed_sinc.exception.entity;

import br.gov.ed_sinc.exception.EntidadeNaoEncontradaException;

public class TurmaNaoEncontradoException extends EntidadeNaoEncontradaException {
	
	private static final long serialVersionUID = 1L;

	public TurmaNaoEncontradoException(String mensagem) {
		super(mensagem);
	}
	
	public TurmaNaoEncontradoException(Long id) {
		this(String.format("Não existe um cadastro de Turma com este código %d", id));
	}

}