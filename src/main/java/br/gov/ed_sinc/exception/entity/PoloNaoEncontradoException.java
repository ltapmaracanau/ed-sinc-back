package br.gov.ed_sinc.exception.entity;

import br.gov.ed_sinc.exception.EntidadeNaoEncontradaException;

public class PoloNaoEncontradoException extends EntidadeNaoEncontradaException {
	
	private static final long serialVersionUID = 1L;

	public PoloNaoEncontradoException(String mensagem) {
		super(mensagem);
	}
	
	public PoloNaoEncontradoException(Long id) {
		this(String.format("Não existe um cadastro de Polo com este código %d", id));
	}

}