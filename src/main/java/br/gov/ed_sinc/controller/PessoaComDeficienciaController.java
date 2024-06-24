package br.gov.ed_sinc.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;

import br.gov.ed_sinc.dto.PessoaComDeficienciaDTO;
import br.gov.ed_sinc.dto.assembler.PessoaComDeficienciaDTOAssembler;
import br.gov.ed_sinc.dto.disassembler.PessoaComDeficienciaInputDisassembler;
import br.gov.ed_sinc.dto.input.PessoaComDeficienciaInput;
import br.gov.ed_sinc.dto.view.PessoaComDeficienciaView;
import br.gov.ed_sinc.filter.PessoaComDeficienciaFilter;
import br.gov.ed_sinc.model.PessoaComDeficiencia;
import br.gov.ed_sinc.projection.pesquisa.PagePessoaComDeficienciaPesquisaProjection;
import br.gov.ed_sinc.service.PessoaComDeficienciaService;
import br.gov.ed_sinc.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping(value = "/pcd", produces = {"application/json"})
@Tag(name = "Pessoa Com Deficiência (PCD)")
public class PessoaComDeficienciaController {

	PessoaComDeficienciaService pessoaComDeficienciaService;
	PessoaComDeficienciaDTOAssembler pessoaComDeficienciaDTOAssembler;
	PessoaComDeficienciaInputDisassembler pessoaComDeficienciaInputDisassembler;
	UsuarioService usuarioService;

	@JsonView(PessoaComDeficienciaView.Resumo.class)
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	@Operation(
			summary = "Cadastro de um novo pessoaComDeficiencia.", 
			description = "Método que faz cadastro de um novo pessoaComDeficiencia no sistema seguindo os atributos do input da classe.", 
			security = {@SecurityRequirement(name = "Bearer Authentication")})
	@RolesAllowed({"Administrador"})
    @ApiResponses(value = {
    		@ApiResponse(responseCode = "201", description = "Sucesso no cadastro."),
            @ApiResponse(responseCode = "400", description = "Parâmetro com formatação incorreta ou Categoria não corresponde à operação."),
            @ApiResponse(responseCode = "401", description = "Token JWT expirado."),
            @ApiResponse(responseCode = "403", description = "Token JWT não inserido no cabeçalho."),
            @ApiResponse(responseCode = "406", description = "Erro no parâmetro da entidade dependente."),
            @ApiResponse(responseCode = "500", description = "Erro no Sistema."),
    })
	public PessoaComDeficienciaDTO adicionarPessoaComDeficiencia(@RequestHeader("Authorization") String bearerToken, @Valid @RequestBody PessoaComDeficienciaInput pessoaComDeficienciaInput) {
		usuarioService.autorizarAdmin(bearerToken);
		PessoaComDeficiencia pessoaComDeficiencia = pessoaComDeficienciaInputDisassembler.toDomainObject(pessoaComDeficienciaInput);
		pessoaComDeficiencia = pessoaComDeficienciaService.adicionarPessoaComDeficiencia(pessoaComDeficiencia);
		return pessoaComDeficienciaDTOAssembler.toModel(pessoaComDeficiencia);
	}

	@JsonView(PessoaComDeficienciaView.Resumo.class)
	@GetMapping("/{id}")
	@Operation(
			summary = "Busca um pessoaComDeficiencia com base no seu id.", 
			description = "Retorna um pessoaComDeficiencia com base no parâmetro id.")
    @ApiResponses(value = {
    		@ApiResponse(responseCode = "200", description = "Sucesso na busca."),
            @ApiResponse(responseCode = "400", description = "Parâmetro com formatação incorreta."),
            @ApiResponse(responseCode = "404", description = "Violação das regras de negócio."),
            @ApiResponse(responseCode = "500", description = "Erro no Sistema."),
    })
	public PessoaComDeficienciaDTO buscarPessoaComDeficiencia(@PathVariable Long id) {
		PessoaComDeficiencia pessoaComDeficiencia = pessoaComDeficienciaService.buscarOuFalhar(id);
		return pessoaComDeficienciaDTOAssembler.toModel(pessoaComDeficiencia);
	}

	@JsonView(PessoaComDeficienciaView.Resumo.class)
	@PutMapping("/{id}")
	@Operation(
			summary = "Edita um pessoaComDeficiencia com base no seu id.", 
			description = "Edita um pessoaComDeficiencia e o retorna com base no parâmetro id.", 
			security = {@SecurityRequirement(name = "Bearer Authentication")})
	@RolesAllowed({"Administrador","Coordenador"})
    @ApiResponses(value = {
    		@ApiResponse(responseCode = "200", description = "Sucesso na edição."),
            @ApiResponse(responseCode = "400", description = "Parâmetro com formatação incorreta ou Categoria não corresponde à operação."),
            @ApiResponse(responseCode = "401", description = "Token JWT expirado."),
            @ApiResponse(responseCode = "403", description = "Token JWT não inserido no cabeçalho."),
            @ApiResponse(responseCode = "404", description = "Violação das regras de negócio."),
            @ApiResponse(responseCode = "406", description = "Erro no parâmetro da entidade dependente."),
            @ApiResponse(responseCode = "500", description = "Erro no Sistema."),
    })
	public PessoaComDeficienciaDTO editarPessoaComDeficiencia(@RequestHeader("Authorization") String bearerToken, @PathVariable Long id, @Valid @RequestBody PessoaComDeficienciaInput pessoaComDeficienciaInput) {
		usuarioService.autorizarAdmin(bearerToken);
		PessoaComDeficiencia pessoaComDeficienciaAtual = pessoaComDeficienciaService.buscarOuFalhar(id);
		pessoaComDeficienciaInputDisassembler.copyToDomainObject(pessoaComDeficienciaInput, pessoaComDeficienciaAtual);
		pessoaComDeficienciaAtual = pessoaComDeficienciaService.adicionarPessoaComDeficiencia(pessoaComDeficienciaAtual);
		return pessoaComDeficienciaDTOAssembler.toModel(pessoaComDeficienciaAtual);
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@Operation(
			summary = "Deleta um pessoaComDeficiencia com base no seu id.", 
			description = "Método que deleta um pessoaComDeficiencia com base no parâmetro id.", 
			security = {@SecurityRequirement(name = "Bearer Authentication")})
	@RolesAllowed({"Administrador","Coordenador"})
    @ApiResponses(value = {
    		@ApiResponse(responseCode = "204", description = "Sucesso na remoção."),
            @ApiResponse(responseCode = "400", description = "Parâmetro com formatação incorreta ou Categoria não corresponde à operação."),
            @ApiResponse(responseCode = "401", description = "Token JWT expirado."),
            @ApiResponse(responseCode = "403", description = "Token JWT não inserido no cabeçalho."),
            @ApiResponse(responseCode = "404", description = "Violação das regras de negócio."),
            @ApiResponse(responseCode = "500", description = "Erro no Sistema."),
    })
	public void deletarPessoaComDeficiencia(@RequestHeader("Authorization") String bearerToken, @PathVariable Long id) {
		usuarioService.autorizarAdmin(bearerToken);
		pessoaComDeficienciaService.buscarOuFalhar(id);
		pessoaComDeficienciaService.deletarPessoaComDeficiencia(id);
	}

	@GetMapping("/consultar")
	@Operation(
			summary = "Retorna todos os pessoaComDeficiencia com base nos filtros de consulta em uma lista.", 
			description = "Método que retorna todos os pessoaComDeficiencia com base nos filtros e pode ser customizado com os parâmetros do Pageable como: sort, size, page. Requer role 'Administrador'.")
    @ApiResponses(value = {
    		@ApiResponse(responseCode = "200", description = "Sucesso na requisição de listagem."),
            @ApiResponse(responseCode = "400", description = "Parâmetro com formatação incorreta."),
            @ApiResponse(responseCode = "404", description = "Violação das regras de negócio."),
            @ApiResponse(responseCode = "500", description = "Erro no Sistema."),
    })
	public PagePessoaComDeficienciaPesquisaProjection consultarUsuarios(Pageable pageable, PessoaComDeficienciaFilter filtro){
		return pessoaComDeficienciaService.listarPessoaComDeficienciaPesquisaProjetado(filtro, pageable);
	}
	

	@GetMapping("/bloquear/{id}")
	@Operation(
			summary = "Bloqueia um pessoaComDeficiencia com base no seu id.", 
			description = "Método que bloqueia(torna o atributo status em Inativo) um pessoaComDeficiencia com base no parâmetro id.", 
			security = {@SecurityRequirement(name = "Bearer Authentication")})
	@RolesAllowed({"Administrador","Coordenador"})
    @ApiResponses(value = {
    		@ApiResponse(responseCode = "200", description = "Sucesso na operação."),
            @ApiResponse(responseCode = "400", description = "Parâmetro com formatação incorreta ou Categoria não corresponde à operação."),
            @ApiResponse(responseCode = "401", description = "Token JWT expirado."),
            @ApiResponse(responseCode = "403", description = "Token JWT não inserido no cabeçalho."),
            @ApiResponse(responseCode = "404", description = "Violação das regras de negócio."),
            @ApiResponse(responseCode = "500", description = "Erro no Sistema."),
    })
	public ResponseEntity<Void> bloquearPessoaComDeficiencia(@RequestHeader("Authorization") String bearerToken,
			@PathVariable Long id) {
		return pessoaComDeficienciaService.bloquearPessoaComDeficienciaId(bearerToken, id);
	}

	@GetMapping("/arquivar/{id}")
	@Operation(
			summary = "Arquiva um pessoaComDeficiencia com base no seu id.", 
			description = "Método que arquiva(torna o atributo status em Arquivado) um pessoaComDeficiencia com base no parâmetro id.", 
			security = {@SecurityRequirement(name = "Bearer Authentication")})
	@RolesAllowed({"Administrador","Coordenador"})
    @ApiResponses(value = {
    		@ApiResponse(responseCode = "200", description = "Sucesso na operação."),
            @ApiResponse(responseCode = "400", description = "Parâmetro com formatação incorreta ou Categoria não corresponde à operação."),
            @ApiResponse(responseCode = "401", description = "Token JWT expirado."),
            @ApiResponse(responseCode = "403", description = "Token JWT não inserido no cabeçalho."),
            @ApiResponse(responseCode = "404", description = "Violação das regras de negócio."),
            @ApiResponse(responseCode = "500", description = "Erro no Sistema."),
    })
	public ResponseEntity<Void> arquivarPessoaComDeficiencia(@RequestHeader("Authorization") String bearerToken,
			@PathVariable Long id) {
		return pessoaComDeficienciaService.arquivarPessoaComDeficienciaId(bearerToken, id);
	}

	@GetMapping("/restaurar/{id}")
	@Operation(
			summary = "Restaura um pessoaComDeficiencia com base no seu id.", 
			description = "Método que restaura(torna o atributo status em Ativo) um pessoaComDeficiencia com base no parâmetro id.", 
			security = {@SecurityRequirement(name = "Bearer Authentication")})
	@RolesAllowed({"Administrador","Coordenador"})
    @ApiResponses(value = {
    		@ApiResponse(responseCode = "200", description = "Sucesso na operação."),
            @ApiResponse(responseCode = "400", description = "Parâmetro com formatação incorreta ou Categoria não corresponde à operação."),
            @ApiResponse(responseCode = "401", description = "Token JWT expirado."),
            @ApiResponse(responseCode = "403", description = "Token JWT não inserido no cabeçalho."),
            @ApiResponse(responseCode = "404", description = "Violação das regras de negócio."),
            @ApiResponse(responseCode = "500", description = "Erro no Sistema."),
    })
	public ResponseEntity<Void> restaurarPessoaComDeficiencia(@RequestHeader("Authorization") String bearerToken,
			@PathVariable Long id) {
		return pessoaComDeficienciaService.restaurarPessoaComDeficienciaId(bearerToken, id);
	}

}
