package br.gov.ed_sinc.exception;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.catalina.connector.ClientAbortException;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.hibernate.PersistentObjectException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.fasterxml.jackson.databind.JsonMappingException.Reference;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.exc.PropertyBindingException;

@ControllerAdvice
public class EdSincExceptionHandler extends ResponseEntityExceptionHandler {

	public static final String MSG_ERRO_GENERICA_USUARIO_FINAL = "Ocorreu um erro interno inesperado no sistema. Tente novamente e se "
			+ "o problema persistir, entre em contato com o administrador do sistema.";

	@Autowired
	private MessageSource messageSource;

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatusCode status, WebRequest request) {

		TipoProblema tipoProblema = TipoProblema.DADOS_INVALIDOS;
		String detalhe = "Um ou mais campos estão inválidos. Faça o preenchimento correto e tente novamente.";

		BindingResult bindingResult = ex.getBindingResult();

		List<Problema.Campo> problemaCampos = bindingResult.getAllErrors().stream().map(campoError -> {
			String message = messageSource.getMessage(campoError, LocaleContextHolder.getLocale());

			String nome = campoError.getObjectName();

			if (campoError instanceof FieldError) {
				nome = ((FieldError) campoError).getField();
			}

			return Problema.Campo.builder().nome(nome).mensagem(message).build();
		}).collect(Collectors.toList());

		Problema problema = createProblemaBuilder(status, tipoProblema, detalhe).mensagem(detalhe)
				.campos(problemaCampos).build();

		return handleExceptionInternal(ex, problema, headers, status, request);
	}
	
	@ExceptionHandler(ClientAbortException.class)
	public ResponseEntity<?> handleClientAbort(ClientAbortException ex, WebRequest request) {

	    HttpStatus status = HttpStatus.ALREADY_REPORTED;
	    TipoProblema tipoProblema = TipoProblema.ERRO_CLIENTE;
	    String detalhe = "A conexão com o cliente foi interrompida.";

	    Problema problema = createProblemaBuilder(status, tipoProblema, detalhe).mensagem(detalhe).build();

	    return handleExceptionInternal(ex, problema, new HttpHeaders(), status, request);
	}
	
	@ExceptionHandler(BadCredentialsException.class)
	public ResponseEntity<?> handleBadCredentials(BadCredentialsException ex, WebRequest request) {

	    HttpStatus status = HttpStatus.NOT_ACCEPTABLE;
	    TipoProblema tipoProblema = TipoProblema.AUTENTICACAO_FALHOU;
	    String detalhe = ex.getMessage();

	    Problema problema = createProblemaBuilder(status, tipoProblema, detalhe).mensagem(detalhe).build();

	    return handleExceptionInternal(ex, problema, new HttpHeaders(), status, request);
	}
	
	@ExceptionHandler(LockedException.class)
	public ResponseEntity<?> handleLockedException(LockedException ex, WebRequest request) {

	    HttpStatus status = HttpStatus.NOT_ACCEPTABLE;
	    TipoProblema tipoProblema = TipoProblema.CONTA_BLOQUEADA;
	    String detalhe = ex.getMessage();

	    Problema problema = createProblemaBuilder(status, tipoProblema, detalhe).mensagem(detalhe).build();

	    return handleExceptionInternal(ex, problema, new HttpHeaders(), status, request);
	}
	
	@ExceptionHandler(InvalidDataAccessApiUsageException.class)
	public ResponseEntity<?> handleInvalidDataAccessApiUsageException(InvalidDataAccessApiUsageException ex, WebRequest request) {

	    HttpStatus status = HttpStatus.NOT_ACCEPTABLE;
	    TipoProblema tipoProblema = TipoProblema.ERRO_NEGOCIO;
	    String detalhe = ex.getMessage();

	    Problema problema = createProblemaBuilder(status, tipoProblema, detalhe).mensagem(detalhe).build();

	    return handleExceptionInternal(ex, problema, new HttpHeaders(), status, request);
	}
	
	@ExceptionHandler(PersistentObjectException.class)
	public ResponseEntity<?> handlePersistentObjectException(PersistentObjectException ex, WebRequest request) {

	    HttpStatus status = HttpStatus.NOT_ACCEPTABLE;
	    TipoProblema tipoProblema = TipoProblema.ERRO_NEGOCIO;
	    String detalhe = ex.getMessage();

	    Problema problema = createProblemaBuilder(status, tipoProblema, detalhe).mensagem(detalhe).build();

	    return handleExceptionInternal(ex, problema, new HttpHeaders(), status, request);
	}
	
	@ExceptionHandler(JpaSystemException.class)
	public ResponseEntity<?> handleJpaSystemException(JpaSystemException ex, WebRequest request) {

	    HttpStatus status = HttpStatus.NOT_ACCEPTABLE;
	    TipoProblema tipoProblema = TipoProblema.ERRO_NEGOCIO;
	    String detalhe = ex.getMessage();

	    Problema problema = createProblemaBuilder(status, tipoProblema, detalhe).mensagem(detalhe).build();

	    return handleExceptionInternal(ex, problema, new HttpHeaders(), status, request);
	}


	@ExceptionHandler(Exception.class)
	public ResponseEntity<Object> handleUncaught(Exception ex, WebRequest request) {
		HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
		TipoProblema tipoProblema = TipoProblema.ERRO_DE_SISTEMA;
		String detalhe = MSG_ERRO_GENERICA_USUARIO_FINAL;

		ex.printStackTrace();

		Problema problema = createProblemaBuilder(status, tipoProblema, detalhe).mensagem(detalhe).build();

		return handleExceptionInternal(ex, problema, new HttpHeaders(), status, request);
	}

	@Override
	protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex, HttpHeaders headers,
			HttpStatusCode status, WebRequest request) {

		TipoProblema tipoProblema = TipoProblema.RECURSO_NAO_ENCONTRADO;
		String detalhe = String.format("O recurso %s, que você tentou acessar, é inexistente.", ex.getRequestURL());

		Problema problema = createProblemaBuilder(status, tipoProblema, detalhe)
				.mensagem(MSG_ERRO_GENERICA_USUARIO_FINAL).build();

		return handleExceptionInternal(ex, problema, headers, status, request);
	}

	@Override
	protected ResponseEntity<Object> handleTypeMismatch(TypeMismatchException ex, HttpHeaders headers,
			HttpStatusCode status, WebRequest request) {

		if (ex instanceof MethodArgumentTypeMismatchException) {
			return handleMethodArgumentTypeMismatch((MethodArgumentTypeMismatchException) ex, headers, status, request);
		}

		return super.handleTypeMismatch(ex, headers, status, request);
	}

	private ResponseEntity<Object> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex,
			HttpHeaders headers, HttpStatusCode status, WebRequest request) {

		TipoProblema tipoProblema = TipoProblema.PARAMETRO_INVALIDO;

		String detalhe = String.format(
				"O parâmetro de URL '%s' recebeu o valor '%s', "
						+ "que é de um tipo inválido. Corrija e informe um valor compatível com o tipo %s.",
				ex.getName(), ex.getValue(), ex.getRequiredType().getSimpleName());

		Problema problema = createProblemaBuilder(status, tipoProblema, detalhe)
				.mensagem(MSG_ERRO_GENERICA_USUARIO_FINAL).build();

		return handleExceptionInternal(ex, problema, headers, status, request);
	}

	@Override
	protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
			HttpHeaders headers, HttpStatusCode status, WebRequest request) {
		Throwable rootCause = ExceptionUtils.getRootCause(ex);

		if (rootCause instanceof InvalidFormatException) {
			return handleInvalidFormat((InvalidFormatException) rootCause, headers, status, request);
		} else if (rootCause instanceof PropertyBindingException) {
			return handlePropertyBinding((PropertyBindingException) rootCause, headers, status, request);
		}

		TipoProblema tipoProblema = TipoProblema.MENSAGEM_INCOMPREENSIVEL;
		String detalhe = "O corpo da requisição está inválido. Verifique erro de sintaxe.";

		Problema problema = createProblemaBuilder(status, tipoProblema, detalhe)
				.mensagem(MSG_ERRO_GENERICA_USUARIO_FINAL).build();

		return handleExceptionInternal(ex, problema, headers, status, request);
	}

	private ResponseEntity<Object> handlePropertyBinding(PropertyBindingException ex, HttpHeaders headers,
			HttpStatusCode status, WebRequest request) {

		String path = joinPath(ex.getPath());

		TipoProblema tipoProblema = TipoProblema.MENSAGEM_INCOMPREENSIVEL;
		String detalhe = String.format(
				"A propriedade '%s' não existe. " + "Corrija ou remova essa propriedade e tente novamente.", path);

		Problema problema = createProblemaBuilder(status, tipoProblema, detalhe)
				.mensagem(MSG_ERRO_GENERICA_USUARIO_FINAL).build();

		return handleExceptionInternal(ex, problema, headers, status, request);
	}

	private ResponseEntity<Object> handleInvalidFormat(InvalidFormatException ex, HttpHeaders headers,
			HttpStatusCode status, WebRequest request) {

		String path = joinPath(ex.getPath());

		TipoProblema tipoProblema = TipoProblema.MENSAGEM_INCOMPREENSIVEL;
		String detalhe = String.format(
				"A propriedade '%s' recebeu o valor '%s', "
						+ "que é de um tipo inválido. Corrija e informe um valor compatível com o tipo %s.",
				path, ex.getValue(), ex.getTargetType().getSimpleName());

		Problema problema = createProblemaBuilder(status, tipoProblema, detalhe)
				.mensagem(MSG_ERRO_GENERICA_USUARIO_FINAL).build();

		return handleExceptionInternal(ex, problema, headers, status, request);
	}

	@ExceptionHandler(EntidadeNaoEncontradaException.class)
	public ResponseEntity<?> handleEntidadeNaoEncontrada(EntidadeNaoEncontradaException ex, WebRequest request) {

		HttpStatus status = HttpStatus.NOT_FOUND;
		TipoProblema tipoProblema = TipoProblema.RECURSO_NAO_ENCONTRADO;
		String detalhe = ex.getMessage();

		Problema problema = createProblemaBuilder(status, tipoProblema, detalhe).mensagem(detalhe).build();

		return handleExceptionInternal(ex, problema, new HttpHeaders(), status, request);
	}

	@ExceptionHandler(EntidadeEmUsoException.class)
	public ResponseEntity<?> handleEntidadeEmUso(EntidadeEmUsoException ex, WebRequest request) {

		HttpStatus status = HttpStatus.CONFLICT;
		TipoProblema tipoProblema = TipoProblema.ENTIDADE_EM_USO;
		String detalhe = ex.getMessage();

		Problema problema = createProblemaBuilder(status, tipoProblema, detalhe).mensagem(detalhe).build();

		return handleExceptionInternal(ex, problema, new HttpHeaders(), status, request);
	}

	@ExceptionHandler(NegocioException.class)
	public ResponseEntity<?> handleNegocio(NegocioException ex, WebRequest request) {

		HttpStatus status = HttpStatus.BAD_REQUEST;
		TipoProblema tipoProblema = TipoProblema.ERRO_NEGOCIO;
		String detalhe = ex.getMessage();

		Problema problema = createProblemaBuilder(status, tipoProblema, detalhe).mensagem(detalhe).build();

		return handleExceptionInternal(ex, problema, new HttpHeaders(), status, request);
	}

	@Override
	protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers,
			HttpStatusCode status, WebRequest request) {

		if (body == null) {
			body = Problema.builder().dataHora(LocalDate.now()).titulo(status.toString()).status(status.value())
					.mensagem(MSG_ERRO_GENERICA_USUARIO_FINAL).build();
		} else if (body instanceof String) {
			body = Problema.builder().dataHora(LocalDate.now()).titulo((String) body).status(status.value())
					.mensagem(MSG_ERRO_GENERICA_USUARIO_FINAL).build();
		}

		return super.handleExceptionInternal(ex, body, headers, status, request);
	}

	private Problema.ProblemaBuilder createProblemaBuilder(HttpStatusCode status, TipoProblema tipoProblema,
			String detalhe) {

		return Problema.builder().dataHora(LocalDate.now()).status(status.value()).tipo(tipoProblema.getUri())
				.titulo(tipoProblema.getTitulo()).detalhe(detalhe);
	}

	private String joinPath(List<Reference> references) {
		return references.stream().map(ref -> ref.getFieldName()).collect(Collectors.joining("."));
	}

}
