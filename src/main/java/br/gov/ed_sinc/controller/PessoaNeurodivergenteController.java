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

import br.gov.ed_sinc.dto.PessoaNeurodivergenteDTO;
import br.gov.ed_sinc.dto.assembler.PessoaNeurodivergenteDTOAssembler;
import br.gov.ed_sinc.dto.disassembler.PessoaNeurodivergenteInputDisassembler;
import br.gov.ed_sinc.dto.input.PessoaNeurodivergenteInput;
import br.gov.ed_sinc.dto.view.PessoaNeurodivergenteView;
import br.gov.ed_sinc.filter.PessoaNeurodivergenteFilter;
import br.gov.ed_sinc.model.PessoaNeurodivergente;
import br.gov.ed_sinc.projection.pesquisa.PagePessoaNeurodivergentePesquisaProjection;
import br.gov.ed_sinc.service.PessoaNeurodivergenteService;
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
@RequestMapping(value = "/neurodivergente", produces = {"application/json"})
@Tag(name = "Pessoa Neurodivergente")
public class PessoaNeurodivergenteController {

	PessoaNeurodivergenteService pessoaNeurodivergenteService;
	PessoaNeurodivergenteDTOAssembler pessoaNeurodivergenteDTOAssembler;
	PessoaNeurodivergenteInputDisassembler pessoaNeurodivergenteInputDisassembler;
	UsuarioService usuarioService;

	@JsonView(PessoaNeurodivergenteView.Resumo.class)
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	@Operation(
			summary = "Cadastro de um novo pessoaNeurodivergente.", 
			description = "Método que faz cadastro de um novo pessoaNeurodivergente no sistema seguindo os atributos do input da classe.", 
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
	public PessoaNeurodivergenteDTO adicionarPessoaNeurodivergente(@RequestHeader("Authorization") String bearerToken, @Valid @RequestBody PessoaNeurodivergenteInput pessoaNeurodivergenteInput) {
		usuarioService.autorizarAdmin(bearerToken);
		PessoaNeurodivergente pessoaNeurodivergente = pessoaNeurodivergenteInputDisassembler.toDomainObject(pessoaNeurodivergenteInput);
		pessoaNeurodivergente = pessoaNeurodivergenteService.adicionarPessoaNeurodivergente(pessoaNeurodivergente);
		return pessoaNeurodivergenteDTOAssembler.toModel(pessoaNeurodivergente);
	}

	@JsonView(PessoaNeurodivergenteView.Resumo.class)
	@GetMapping("/{id}")
	@Operation(
			summary = "Busca um pessoaNeurodivergente com base no seu id.", 
			description = "Retorna um pessoaNeurodivergente com base no parâmetro id.")
    @ApiResponses(value = {
    		@ApiResponse(responseCode = "200", description = "Sucesso na busca."),
            @ApiResponse(responseCode = "400", description = "Parâmetro com formatação incorreta."),
            @ApiResponse(responseCode = "404", description = "Violação das regras de negócio."),
            @ApiResponse(responseCode = "500", description = "Erro no Sistema."),
    })
	public PessoaNeurodivergenteDTO buscarPessoaNeurodivergente(@PathVariable Long id) {
		PessoaNeurodivergente pessoaNeurodivergente = pessoaNeurodivergenteService.buscarOuFalhar(id);
		return pessoaNeurodivergenteDTOAssembler.toModel(pessoaNeurodivergente);
	}

	@JsonView(PessoaNeurodivergenteView.Resumo.class)
	@PutMapping("/{id}")
	@Operation(
			summary = "Edita um pessoaNeurodivergente com base no seu id.", 
			description = "Edita um pessoaNeurodivergente e o retorna com base no parâmetro id.", 
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
	public PessoaNeurodivergenteDTO editarPessoaNeurodivergente(@RequestHeader("Authorization") String bearerToken, @PathVariable Long id, @Valid @RequestBody PessoaNeurodivergenteInput pessoaNeurodivergenteInput) {
		usuarioService.autorizarAdmin(bearerToken);
		PessoaNeurodivergente pessoaNeurodivergenteAtual = pessoaNeurodivergenteService.buscarOuFalhar(id);
		pessoaNeurodivergenteInputDisassembler.copyToDomainObject(pessoaNeurodivergenteInput, pessoaNeurodivergenteAtual);
		pessoaNeurodivergenteAtual = pessoaNeurodivergenteService.adicionarPessoaNeurodivergente(pessoaNeurodivergenteAtual);
		return pessoaNeurodivergenteDTOAssembler.toModel(pessoaNeurodivergenteAtual);
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@Operation(
			summary = "Deleta um pessoaNeurodivergente com base no seu id.", 
			description = "Método que deleta um pessoaNeurodivergente com base no parâmetro id.", 
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
	public void deletarPessoaNeurodivergente(@RequestHeader("Authorization") String bearerToken, @PathVariable Long id) {
		usuarioService.autorizarAdmin(bearerToken);
		pessoaNeurodivergenteService.buscarOuFalhar(id);
		pessoaNeurodivergenteService.deletarPessoaNeurodivergente(id);
	}

	@GetMapping("/consultar")
	@Operation(
			summary = "Retorna todos os pessoaNeurodivergente com base nos filtros de consulta em uma lista.", 
			description = "Método que retorna todos os pessoaNeurodivergente com base nos filtros e pode ser customizado com os parâmetros do Pageable como: sort, size, page. Requer role 'Administrador'.", 
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
	public PagePessoaNeurodivergentePesquisaProjection consultarUsuarios(@RequestHeader("Authorization") String bearerToken, Pageable pageable, PessoaNeurodivergenteFilter filtro){
		usuarioService.autorizarAdmin(bearerToken);
		return pessoaNeurodivergenteService.listarPessoaNeurodivergentePesquisaProjetado(filtro, pageable);
	}
	

	@GetMapping("/bloquear/{id}")
	@Operation(
			summary = "Bloqueia um pessoaNeurodivergente com base no seu id.", 
			description = "Método que bloqueia(torna o atributo status em Inativo) um pessoaNeurodivergente com base no parâmetro id.", 
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
	public ResponseEntity<Void> bloquearPessoaNeurodivergente(@RequestHeader("Authorization") String bearerToken,
			@PathVariable Long id) {
		return pessoaNeurodivergenteService.bloquearPessoaNeurodivergenteId(bearerToken, id);
	}

	@GetMapping("/arquivar/{id}")
	@Operation(
			summary = "Arquiva um pessoaNeurodivergente com base no seu id.", 
			description = "Método que arquiva(torna o atributo status em Arquivado) um pessoaNeurodivergente com base no parâmetro id.", 
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
	public ResponseEntity<Void> arquivarPessoaNeurodivergente(@RequestHeader("Authorization") String bearerToken,
			@PathVariable Long id) {
		return pessoaNeurodivergenteService.arquivarPessoaNeurodivergenteId(bearerToken, id);
	}

	@GetMapping("/restaurar/{id}")
	@Operation(
			summary = "Restaura um pessoaNeurodivergente com base no seu id.", 
			description = "Método que restaura(torna o atributo status em Ativo) um pessoaNeurodivergente com base no parâmetro id.", 
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
	public ResponseEntity<Void> restaurarPessoaNeurodivergente(@RequestHeader("Authorization") String bearerToken,
			@PathVariable Long id) {
		return pessoaNeurodivergenteService.restaurarPessoaNeurodivergenteId(bearerToken, id);
	}

}