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

import br.gov.ed_sinc.dto.GrupoSocialDTO;
import br.gov.ed_sinc.dto.assembler.GrupoSocialDTOAssembler;
import br.gov.ed_sinc.dto.disassembler.GrupoSocialInputDisassembler;
import br.gov.ed_sinc.dto.input.GrupoSocialInput;
import br.gov.ed_sinc.dto.view.GrupoSocialView;
import br.gov.ed_sinc.filter.GrupoSocialFilter;
import br.gov.ed_sinc.model.GrupoSocial;
import br.gov.ed_sinc.projection.pesquisa.PageGrupoSocialPesquisaProjection;
import br.gov.ed_sinc.service.GrupoSocialService;
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
@RequestMapping(value = "/grupossociais", produces = {"application/json"})
@Tag(name = "Grupos Sociais")
public class GrupoSocialController {

	GrupoSocialService grupoSocialService;
	GrupoSocialDTOAssembler grupoSocialDTOAssembler;
	GrupoSocialInputDisassembler grupoSocialInputDisassembler;
	UsuarioService usuarioService;

	@JsonView(GrupoSocialView.Resumo.class)
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	@Operation(
			summary = "Cadastro de um novo grupoSocial.", 
			description = "Método que faz cadastro de um novo grupoSocial no sistema seguindo os atributos do input da classe.", 
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
	public GrupoSocialDTO adicionarGrupoSocial(@RequestHeader("Authorization") String bearerToken, @Valid @RequestBody GrupoSocialInput grupoSocialInput) {
		usuarioService.autorizarAdmin(bearerToken);
		GrupoSocial grupoSocial = grupoSocialInputDisassembler.toDomainObject(grupoSocialInput);
		grupoSocial = grupoSocialService.adicionarGrupoSocial(grupoSocial);
		return grupoSocialDTOAssembler.toModel(grupoSocial);
	}

	@JsonView(GrupoSocialView.Resumo.class)
	@GetMapping("/{id}")
	@Operation(
			summary = "Busca um grupoSocial com base no seu id.", 
			description = "Retorna um grupoSocial com base no parâmetro id.")
    @ApiResponses(value = {
    		@ApiResponse(responseCode = "200", description = "Sucesso na busca."),
            @ApiResponse(responseCode = "400", description = "Parâmetro com formatação incorreta."),
            @ApiResponse(responseCode = "404", description = "Violação das regras de negócio."),
            @ApiResponse(responseCode = "500", description = "Erro no Sistema."),
    })
	public GrupoSocialDTO buscarGrupoSocial(@PathVariable Long id) {
		GrupoSocial grupoSocial = grupoSocialService.buscarOuFalhar(id);
		return grupoSocialDTOAssembler.toModel(grupoSocial);
	}

	@JsonView(GrupoSocialView.Resumo.class)
	@PutMapping("/{id}")
	@Operation(
			summary = "Edita um grupoSocial com base no seu id.", 
			description = "Edita um grupoSocial e o retorna com base no parâmetro id.", 
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
	public GrupoSocialDTO editarGrupoSocial(@RequestHeader("Authorization") String bearerToken, @PathVariable Long id, @Valid @RequestBody GrupoSocialInput grupoSocialInput) {
		usuarioService.autorizarAdmin(bearerToken);
		GrupoSocial grupoSocialAtual = grupoSocialService.buscarOuFalhar(id);
		grupoSocialInputDisassembler.copyToDomainObject(grupoSocialInput, grupoSocialAtual);
		grupoSocialAtual = grupoSocialService.adicionarGrupoSocial(grupoSocialAtual);
		return grupoSocialDTOAssembler.toModel(grupoSocialAtual);
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@Operation(
			summary = "Deleta um grupoSocial com base no seu id.", 
			description = "Método que deleta um grupoSocial com base no parâmetro id.", 
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
	public void deletarGrupoSocial(@RequestHeader("Authorization") String bearerToken, @PathVariable Long id) {
		usuarioService.autorizarAdmin(bearerToken);
		grupoSocialService.buscarOuFalhar(id);
		grupoSocialService.deletarGrupoSocial(id);
	}

	@GetMapping("/consultar")
	@Operation(
			summary = "Retorna todos os grupos sociais com base nos filtros de consulta em uma lista.", 
			description = "Método que retorna todos os grupos sociais com base nos filtros e pode ser customizado com os parâmetros do Pageable como: sort, size, page. Requer role 'Administrador'.")
    @ApiResponses(value = {
    		@ApiResponse(responseCode = "200", description = "Sucesso na requisição de listagem."),
            @ApiResponse(responseCode = "400", description = "Parâmetro com formatação incorreta."),
            @ApiResponse(responseCode = "404", description = "Violação das regras de negócio."),
            @ApiResponse(responseCode = "500", description = "Erro no Sistema."),
    })
	public PageGrupoSocialPesquisaProjection consultarUsuarios(Pageable pageable, GrupoSocialFilter filtro){
		return grupoSocialService.listarGrupoSocialPesquisaProjetado(filtro, pageable);
	}
	

	@GetMapping("/bloquear/{id}")
	@Operation(
			summary = "Bloqueia um grupoSocial com base no seu id.", 
			description = "Método que bloqueia(torna o atributo status em Inativo) um grupoSocial com base no parâmetro id.", 
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
	public ResponseEntity<Void> bloquearGrupoSocial(@RequestHeader("Authorization") String bearerToken,
			@PathVariable Long id) {
		return grupoSocialService.bloquearGrupoSocialId(bearerToken, id);
	}

	@GetMapping("/arquivar/{id}")
	@Operation(
			summary = "Arquiva um grupoSocial com base no seu id.", 
			description = "Método que arquiva(torna o atributo status em Arquivado) um grupoSocial com base no parâmetro id.", 
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
	public ResponseEntity<Void> arquivarGrupoSocial(@RequestHeader("Authorization") String bearerToken,
			@PathVariable Long id) {
		return grupoSocialService.arquivarGrupoSocialId(bearerToken, id);
	}

	@GetMapping("/restaurar/{id}")
	@Operation(
			summary = "Restaura um grupoSocial com base no seu id.", 
			description = "Método que restaura(torna o atributo status em Ativo) um grupoSocial com base no parâmetro id.", 
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
	public ResponseEntity<Void> restaurarGrupoSocial(@RequestHeader("Authorization") String bearerToken,
			@PathVariable Long id) {
		return grupoSocialService.restaurarGrupoSocialId(bearerToken, id);
	}

}