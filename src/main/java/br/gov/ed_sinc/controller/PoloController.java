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

import br.gov.ed_sinc.dto.PoloDTO;
import br.gov.ed_sinc.dto.assembler.PoloDTOAssembler;
import br.gov.ed_sinc.dto.disassembler.PoloInputDisassembler;
import br.gov.ed_sinc.dto.input.PoloInput;
import br.gov.ed_sinc.dto.view.PoloView;
import br.gov.ed_sinc.filter.PoloFilter;
import br.gov.ed_sinc.model.Polo;
import br.gov.ed_sinc.projection.pesquisa.PagePoloPesquisaProjection;
import br.gov.ed_sinc.service.PoloService;
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
@RequestMapping(value = "/polos", produces = {"application/json"})
@Tag(name = "Polos")
public class PoloController {

	PoloService poloService;
	PoloDTOAssembler poloDTOAssembler;
	PoloInputDisassembler poloInputDisassembler;
	UsuarioService usuarioService;

	@JsonView(PoloView.Resumo.class)
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	@Operation(
			summary = "Cadastro de um novo polo.", 
			description = "Método que faz cadastro de um novo polo no sistema seguindo os atributos do input da classe.", 
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
	public PoloDTO adicionarPolo(@RequestHeader("Authorization") String bearerToken, @Valid @RequestBody PoloInput poloInput) {
		usuarioService.autorizarAdmin(bearerToken);
		Polo polo = poloInputDisassembler.toDomainObject(poloInput);
		polo = poloService.adicionarPolo(polo);
		return poloDTOAssembler.toModel(polo);
	}

	@JsonView(PoloView.Resumo.class)
	@GetMapping("/{id}")
	@Operation(
			summary = "Busca um polo com base no seu id.", 
			description = "Retorna um polo com base no parâmetro id.")
    @ApiResponses(value = {
    		@ApiResponse(responseCode = "200", description = "Sucesso na busca."),
            @ApiResponse(responseCode = "400", description = "Parâmetro com formatação incorreta."),
            @ApiResponse(responseCode = "404", description = "Violação das regras de negócio."),
            @ApiResponse(responseCode = "500", description = "Erro no Sistema."),
    })
	public PoloDTO buscarPolo(@PathVariable Long id) {
		Polo polo = poloService.buscarOuFalhar(id);
		return poloDTOAssembler.toModel(polo);
	}

	@JsonView(PoloView.Resumo.class)
	@PutMapping("/{id}")
	@Operation(
			summary = "Edita um polo com base no seu id.", 
			description = "Edita um polo e o retorna com base no parâmetro id.", 
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
	public PoloDTO editarPolo(@RequestHeader("Authorization") String bearerToken, @PathVariable Long id, @Valid @RequestBody PoloInput poloInput) {
		usuarioService.autorizarAdmin(bearerToken);
		Polo poloAtual = poloService.buscarOuFalhar(id);
		poloInputDisassembler.copyToDomainObject(poloInput, poloAtual);
		poloAtual = poloService.adicionarPolo(poloAtual);
		return poloDTOAssembler.toModel(poloAtual);
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@Operation(
			summary = "Deleta um polo com base no seu id.", 
			description = "Método que deleta um polo com base no parâmetro id.", 
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
	public void deletarPolo(@RequestHeader("Authorization") String bearerToken, @PathVariable Long id) {
		usuarioService.autorizarAdmin(bearerToken);
		poloService.buscarOuFalhar(id);
		poloService.deletarPolo(id);
	}

	@GetMapping("/consultar")
	@Operation(
			summary = "Retorna todos os polo com base nos filtros de consulta em uma lista.", 
			description = "Método que retorna todos os polos com base nos filtros e pode ser customizado com os parâmetros do Pageable como: sort, size, page. Requer role 'Administrador'.", 
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
	public PagePoloPesquisaProjection consultarUsuarios(@RequestHeader("Authorization") String bearerToken, Pageable pageable, PoloFilter filtro){
		usuarioService.autorizarAdmin(bearerToken);
		return poloService.listarPoloPesquisaProjetado(filtro, pageable);
	}
	

	@GetMapping("/bloquear/{id}")
	@Operation(
			summary = "Bloqueia um polo com base no seu id.", 
			description = "Método que bloqueia(torna o atributo status em Inativo) um polo com base no parâmetro id.", 
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
	public ResponseEntity<Void> bloquearPolo(@RequestHeader("Authorization") String bearerToken,
			@PathVariable Long id) {
		return poloService.bloquearPoloId(bearerToken, id);
	}

	@GetMapping("/arquivar/{id}")
	@Operation(
			summary = "Arquiva um polo com base no seu id.", 
			description = "Método que arquiva(torna o atributo status em Arquivado) um polo com base no parâmetro id.", 
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
	public ResponseEntity<Void> arquivarPolo(@RequestHeader("Authorization") String bearerToken,
			@PathVariable Long id) {
		return poloService.arquivarPoloId(bearerToken, id);
	}

	@GetMapping("/restaurar/{id}")
	@Operation(
			summary = "Restaura um polo com base no seu id.", 
			description = "Método que restaura(torna o atributo status em Ativo) um polo com base no parâmetro id.", 
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
	public ResponseEntity<Void> restaurarPolo(@RequestHeader("Authorization") String bearerToken,
			@PathVariable Long id) {
		return poloService.restaurarPoloId(bearerToken, id);
	}

}