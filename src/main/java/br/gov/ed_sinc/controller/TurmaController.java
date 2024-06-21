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

import br.gov.ed_sinc.dto.TurmaDTO;
import br.gov.ed_sinc.dto.assembler.TurmaDTOAssembler;
import br.gov.ed_sinc.dto.disassembler.TurmaInputDisassembler;
import br.gov.ed_sinc.dto.input.TurmaInput;
import br.gov.ed_sinc.dto.view.TurmaView;
import br.gov.ed_sinc.filter.TurmaFilter;
import br.gov.ed_sinc.model.Turma;
import br.gov.ed_sinc.projection.pesquisa.PageTurmaPesquisaProjection;
import br.gov.ed_sinc.service.TurmaService;
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
@RequestMapping(value = "/turmas", produces = {"application/json"})
@Tag(name = "Turmas")
public class TurmaController {

	TurmaService turmaService;
	TurmaDTOAssembler turmaDTOAssembler;
	TurmaInputDisassembler turmaInputDisassembler;
	UsuarioService usuarioService;

	@JsonView(TurmaView.Resumo.class)
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	@Operation(
			summary = "Cadastro de um novo turma.", 
			description = "Método que faz cadastro de um novo turma no sistema seguindo os atributos do input da classe.", 
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
	public TurmaDTO adicionarTurma(@RequestHeader("Authorization") String bearerToken, @Valid @RequestBody TurmaInput turmaInput) {
		usuarioService.autorizarAdmin(bearerToken);
		Turma turma = turmaInputDisassembler.toDomainObject(turmaInput);
		turma = turmaService.adicionarTurma(turma);
		return turmaDTOAssembler.toModel(turma);
	}

	@JsonView(TurmaView.Resumo.class)
	@GetMapping("/{id}")
	@Operation(
			summary = "Busca um turma com base no seu id.", 
			description = "Retorna um turma com base no parâmetro id.")
    @ApiResponses(value = {
    		@ApiResponse(responseCode = "200", description = "Sucesso na busca."),
            @ApiResponse(responseCode = "400", description = "Parâmetro com formatação incorreta."),
            @ApiResponse(responseCode = "404", description = "Violação das regras de negócio."),
            @ApiResponse(responseCode = "500", description = "Erro no Sistema."),
    })
	public TurmaDTO buscarTurma(@PathVariable Long id) {
		Turma turma = turmaService.buscarOuFalhar(id);
		return turmaDTOAssembler.toModel(turma);
	}

	@JsonView(TurmaView.Resumo.class)
	@PutMapping("/{id}")
	@Operation(
			summary = "Edita um turma com base no seu id.", 
			description = "Edita um turma e o retorna com base no parâmetro id.", 
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
	public TurmaDTO editarTurma(@RequestHeader("Authorization") String bearerToken, @PathVariable Long id, @Valid @RequestBody TurmaInput turmaInput) {
		usuarioService.autorizarAdmin(bearerToken);
		Turma turmaAtual = turmaService.buscarOuFalhar(id);
		turmaInputDisassembler.copyToDomainObject(turmaInput, turmaAtual);
		turmaAtual = turmaService.adicionarTurma(turmaAtual);
		return turmaDTOAssembler.toModel(turmaAtual);
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@Operation(
			summary = "Deleta um turma com base no seu id.", 
			description = "Método que deleta um turma com base no parâmetro id.", 
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
	public void deletarTurma(@RequestHeader("Authorization") String bearerToken, @PathVariable Long id) {
		usuarioService.autorizarAdmin(bearerToken);
		turmaService.buscarOuFalhar(id);
		turmaService.deletarTurma(id);
	}

	@GetMapping("/consultar")
	@Operation(
			summary = "Retorna todos os turma com base nos filtros de consulta em uma lista.", 
			description = "Método que retorna todos os grupos sociais com base nos filtros e pode ser customizado com os parâmetros do Pageable como: sort, size, page. Requer role 'Administrador'.", 
			security = {@SecurityRequirement(name = "Bearer Authentication")})
	@RolesAllowed({"Administrador"})
    @ApiResponses(value = {
    		@ApiResponse(responseCode = "200", description = "Sucesso na requisição de listagem."),
            @ApiResponse(responseCode = "400", description = "Parâmetro com formatação incorreta ou Categoria não corresponde à operação."),
            @ApiResponse(responseCode = "401", description = "Token JWT expirado."),
            @ApiResponse(responseCode = "403", description = "Token JWT não inserido no cabeçalho."),
            @ApiResponse(responseCode = "404", description = "Violação das regras de negócio."),
            @ApiResponse(responseCode = "500", description = "Erro no Sistema."),
    })
	public PageTurmaPesquisaProjection consultarUsuarios(@RequestHeader("Authorization") String bearerToken, Pageable pageable, TurmaFilter filtro){
		usuarioService.autorizarAdmin(bearerToken);
		return turmaService.listarTurmaPesquisaProjetado(filtro, pageable);
	}
	

	@GetMapping("/bloquear/{id}")
	@Operation(
			summary = "Bloqueia um turma com base no seu id.", 
			description = "Método que bloqueia(torna o atributo status em Inativo) um turma com base no parâmetro id.", 
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
	public ResponseEntity<Void> bloquearTurma(@RequestHeader("Authorization") String bearerToken,
			@PathVariable Long id) {
		return turmaService.bloquearTurmaId(bearerToken, id);
	}

	@GetMapping("/arquivar/{id}")
	@Operation(
			summary = "Arquiva um turma com base no seu id.", 
			description = "Método que arquiva(torna o atributo status em Arquivado) um turma com base no parâmetro id.", 
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
	public ResponseEntity<Void> arquivarTurma(@RequestHeader("Authorization") String bearerToken,
			@PathVariable Long id) {
		return turmaService.arquivarTurmaId(bearerToken, id);
	}

	@GetMapping("/restaurar/{id}")
	@Operation(
			summary = "Restaura um turma com base no seu id.", 
			description = "Método que restaura(torna o atributo status em Ativo) um turma com base no parâmetro id.", 
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
	public ResponseEntity<Void> restaurarTurma(@RequestHeader("Authorization") String bearerToken,
			@PathVariable Long id) {
		return turmaService.restaurarTurmaId(bearerToken, id);
	}

}