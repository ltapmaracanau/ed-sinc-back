package br.gov.ed_sinc.exception.entity;

import br.gov.ed_sinc.exception.EntidadeNaoEncontradaException;

public class GrupoSocialNaoEncontradoException extends EntidadeNaoEncontradaException {
	
	private static final long serialVersionUID = 1L;

	public GrupoSocialNaoEncontradoException(String mensagem) {
		super(mensagem);
	}
	
	public GrupoSocialNaoEncontradoException(Long id) {
		this(String.format("Não existe um cadastro de Grupo Social com este código %d", id));
	}

}
