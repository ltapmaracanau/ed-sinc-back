package br.gov.ed_sinc.exception.entity;

import br.gov.ed_sinc.exception.EntidadeNaoEncontradaException;

public class PessoaComDeficienciaNaoEncontradoException extends EntidadeNaoEncontradaException {
	
	private static final long serialVersionUID = 1L;

	public PessoaComDeficienciaNaoEncontradoException(String mensagem) {
		super(mensagem);
	}
	
	public PessoaComDeficienciaNaoEncontradoException(Long id) {
		this(String.format("Não existe um cadastro de PCD com este código %d", id));
	}

}