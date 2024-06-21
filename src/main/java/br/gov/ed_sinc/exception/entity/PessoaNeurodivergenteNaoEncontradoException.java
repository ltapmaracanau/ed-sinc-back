package br.gov.ed_sinc.exception.entity;

import br.gov.ed_sinc.exception.EntidadeNaoEncontradaException;

public class PessoaNeurodivergenteNaoEncontradoException extends EntidadeNaoEncontradaException {
	
	private static final long serialVersionUID = 1L;

	public PessoaNeurodivergenteNaoEncontradoException(String mensagem) {
		super(mensagem);
	}
	
	public PessoaNeurodivergenteNaoEncontradoException(Long id) {
		this(String.format("Não existe um cadastro de Pessoa Neurodivergente com este código %d", id));
	}

}