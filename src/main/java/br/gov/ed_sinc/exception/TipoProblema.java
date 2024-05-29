package br.gov.ed_sinc.exception;

import lombok.Getter;

@Getter
public enum TipoProblema {

	DADOS_INVALIDOS("/dados-invalidos", "Dados inválidos"),
	ERRO_CLIENTE("/erro-cliente", "Erro no Cliente"),
	AUTENTICACAO_FALHOU("/autenticacao-falhou", "Autenticação falhou"),
	CONTA_BLOQUEADA("/conta-bloqueada", "Conta bloqueada, contate o administrador do sistema"),
	ERRO_DE_SISTEMA("/erro-de-sistema", "Erro de sistema"),
	PARAMETRO_INVALIDO("/parametro-invalido", "Parâmetro inválido"),
	MENSAGEM_INCOMPREENSIVEL("/mensagem-incompreensivel", "Mensagem incompreensível"),
	RECURSO_NAO_ENCONTRADO("/recurso-nao-encontrado", "Recurso não encontrado"),
	ENTIDADE_EM_USO("/entidade-em-uso", "Entidade em uso"),
	ERRO_NEGOCIO("/erro-negocio", "Violação de regra de negócio");
	
	private String titulo;
	private String uri;
	
	TipoProblema(String path, String titulo) {
		this.uri = "https://url.padrao..br" + path;
		this.titulo = titulo;
	}
	
}