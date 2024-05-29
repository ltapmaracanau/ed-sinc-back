package br.gov.ed_sinc.exception.entity;

import br.gov.ed_sinc.exception.EntidadeNaoEncontradaException;

public class UsuarioNaoEncontradoException extends EntidadeNaoEncontradaException {
	
	private static final long serialVersionUID = 1L;

	public UsuarioNaoEncontradoException(String mensagem) {
		super(mensagem);
	}
	
	public UsuarioNaoEncontradoException(Long id) {
		this(String.format("Não existe um cadastro de Usuário com este código %d", id));
	}

}
