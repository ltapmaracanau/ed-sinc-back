package br.gov.ed_sinc.controller;

import java.util.List;

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

import br.gov.ed_sinc.dto.UsuarioDTO;
import br.gov.ed_sinc.dto.assembler.UsuarioDTOAssembler;
import br.gov.ed_sinc.dto.disassembler.UsuarioInputDisassembler;
import br.gov.ed_sinc.dto.input.UsuarioInput;
import br.gov.ed_sinc.dto.view.UsuarioView;
import br.gov.ed_sinc.filter.UsuarioFilter;
import br.gov.ed_sinc.model.Usuario;
import br.gov.ed_sinc.projection.pesquisa.PageUsuarioPesquisaProjection;
import br.gov.ed_sinc.projection.relatorio.UsuarioRelatorioProjection;
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
@RequestMapping(value = "/usuarios", produces = {"application/json"})
@Tag(name = "Usuários")
public class UsuarioController {
	
	UsuarioService usuarioService;
	UsuarioDTOAssembler usuarioDTOAssembler;
	UsuarioInputDisassembler usuarioInputDisassembler;
	
	
	@GetMapping("/relatorio")
	@Operation(
			summary = "Retorna todos os usuários em uma lista organizada para o relatório.", 
			description = "Método que retorna todos os usuários para o relatório de usuários do sistema.", 
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
	public List<UsuarioRelatorioProjection> relatorioUsuarios(@RequestHeader("Authorization") String bearerToken) {
		usuarioService.autorizarAdmin(bearerToken);
		return usuarioService.listarUsuariosRelatorioProjetado();
	}
	
	@GetMapping("/consultar")
	@Operation(
			summary = "Retorna todos os usuários com base nos filtros de consulta em uma lista.", 
			description = "Método que retorna todos os usuários com base nos filtros e pode ser customizado com os parâmetros do Pageable como: sort, size, page. Requer role 'Administrador'.", 
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
	public PageUsuarioPesquisaProjection consultarUsuarios(@RequestHeader("Authorization") String bearerToken, Pageable pageable, UsuarioFilter filtro){
		usuarioService.autorizarAdmin(bearerToken);
		return usuarioService.listarUsuariosPesquisaProjetado(filtro, pageable);
	}
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	@JsonView(UsuarioView.Resumo.class)
	@Operation(
			summary = "Cadastro de um novo usuário.", 
			description = "Método que faz cadastro de um novo usuário no sistema seguindo os atributos do input da classe. Requer role 'Administrador'.", 
			security = {@SecurityRequirement(name = "Bearer Authentication")})
	@RolesAllowed({"Administrador"})
    @ApiResponses(value = {
    		@ApiResponse(responseCode = "201", description = "Sucesso no cadastro."),
            @ApiResponse(responseCode = "400", description = "Parâmetro com formatação incorreta ou Categoria não corresponde à operação."),
            @ApiResponse(responseCode = "401", description = "Token JWT expirado."),
            @ApiResponse(responseCode = "403", description = "Token JWT não inserido no cabeçalho."),
            @ApiResponse(responseCode = "500", description = "Erro no Sistema."),
    })
	public UsuarioDTO adicionarUsuario(@RequestHeader("Authorization") String bearerToken, @Valid @RequestBody UsuarioInput usuarioInput) {
		usuarioService.autorizarAdmin(bearerToken);
		Usuario usuario = usuarioInputDisassembler.toDomainObject(usuarioInput);
		usuario = usuarioService.adicionarUsuario(usuario);
		return usuarioDTOAssembler.toModel(usuario);
	}
	
	@JsonView(UsuarioView.Resumo.class)
	@GetMapping("/{id}")
	@Operation(
			summary = "Busca um usuário com base no seu id.", 
			description = "Retorna um usuário com base no parâmetro id. Requer role 'Administrador'.", 
			security = {@SecurityRequirement(name = "Bearer Authentication")})
	@RolesAllowed({"Administrador"})
    @ApiResponses(value = {
    		@ApiResponse(responseCode = "200", description = "Sucesso na busca."),
            @ApiResponse(responseCode = "400", description = "Parâmetro com formatação incorreta ou Categoria não corresponde à operação."),
            @ApiResponse(responseCode = "401", description = "Token JWT expirado."),
            @ApiResponse(responseCode = "403", description = "Token JWT não inserido no cabeçalho."),
            @ApiResponse(responseCode = "404", description = "Violação das regras de negócio."),
            @ApiResponse(responseCode = "500", description = "Erro no Sistema."),
    })
	public UsuarioDTO buscarUsuario(@RequestHeader("Authorization") String bearerToken, @PathVariable Long id){
		usuarioService.autorizarAdmin(bearerToken);
		Usuario usuario = usuarioService.buscarOuFalhar(id);
		return usuarioDTOAssembler.toModel(usuario);
	}
	
	@JsonView(UsuarioView.Resumo.class)
	@PutMapping("/{id}")
	@Operation(
			summary = "Edita um usuário com base no seu id.", 
			description = "Edita um usuário e o retorna com base no parâmetro id. Requer role 'Administrador'.", 
			security = {@SecurityRequirement(name = "Bearer Authentication")})
	@RolesAllowed({"Administrador"})
    @ApiResponses(value = {
    		@ApiResponse(responseCode = "200", description = "Sucesso na edição."),
            @ApiResponse(responseCode = "400", description = "Parâmetro com formatação incorreta ou Categoria não corresponde à operação."),
            @ApiResponse(responseCode = "401", description = "Token JWT expirado."),
            @ApiResponse(responseCode = "403", description = "Token JWT não inserido no cabeçalho."),
            @ApiResponse(responseCode = "404", description = "Violação das regras de negócio."),
            @ApiResponse(responseCode = "500", description = "Erro no Sistema."),
    })
	public UsuarioDTO editarUsuario(@RequestHeader("Authorization") String bearerToken, @PathVariable Long id, @Valid @RequestBody UsuarioInput usuarioinput){
		usuarioService.autorizarAdmin(bearerToken);
		Usuario usuarioAtual = usuarioService.buscarOuFalhar(id);
		usuarioInputDisassembler.copyToDomainObject(usuarioinput, usuarioAtual);
		usuarioAtual = usuarioService.adicionarUsuario(usuarioAtual);
		return usuarioDTOAssembler.toModel(usuarioAtual);
	}
	
	@DeleteMapping("/{id}")
	@Operation(
			summary = "Deleta um usuário com base no seu id.", 
			description = "Método que deleta um usuário com base no parâmetro id. Requer role 'Administrador'.", 
			security = {@SecurityRequirement(name = "Bearer Authentication")})
	@RolesAllowed({"Administrador"})
    @ApiResponses(value = {
    		@ApiResponse(responseCode = "204", description = "Sucesso na remoção."),
            @ApiResponse(responseCode = "400", description = "Parâmetro com formatação incorreta ou Categoria não corresponde à operação."),
            @ApiResponse(responseCode = "401", description = "Token JWT expirado."),
            @ApiResponse(responseCode = "403", description = "Token JWT não inserido no cabeçalho."),
            @ApiResponse(responseCode = "404", description = "Violação das regras de negócio."),
            @ApiResponse(responseCode = "500", description = "Erro no Sistema."),
    })
	public ResponseEntity<Void> deletarUsuario(@RequestHeader("Authorization") String bearerToken, @PathVariable Long id){
		usuarioService.autorizarAdmin(bearerToken);
		usuarioService.buscarOuFalhar(id);
		return usuarioService.deletarUsuario(id);
	}
	
	@GetMapping("/arquivar/{id}")
	@Operation(
			summary = "Arquiva um usuário com base no seu id.", 
			description = "Método que arquiva(torna o atributo status em Arquivado) um usuário com base no parâmetro id. Requer role 'Administrador'.", 
			security = {@SecurityRequirement(name = "Bearer Authentication")})
	@RolesAllowed({"Administrador"})
    @ApiResponses(value = {
    		@ApiResponse(responseCode = "200", description = "Sucesso na operação."),
            @ApiResponse(responseCode = "400", description = "Parâmetro com formatação incorreta ou Categoria não corresponde à operação."),
            @ApiResponse(responseCode = "401", description = "Token JWT expirado."),
            @ApiResponse(responseCode = "403", description = "Token JWT não inserido no cabeçalho."),
            @ApiResponse(responseCode = "404", description = "Violação das regras de negócio."),
            @ApiResponse(responseCode = "500", description = "Erro no Sistema."),
    })
	public ResponseEntity<Void> arquivarUsuario(@RequestHeader("Authorization") String bearerToken, @PathVariable Long id){
		return usuarioService.arquivarUsuarioPorId(bearerToken, id);
	}
	
	@GetMapping("/restaurar/{id}")
	@Operation(
			summary = "Restaura um usuário com base no seu id.", 
			description = "Método que restaura(torna o atributo status em Ativo) um usuário com base no parâmetro id. Requer role 'Administrador'.", 
			security = {@SecurityRequirement(name = "Bearer Authentication")})
	@RolesAllowed({"Administrador"})
    @ApiResponses(value = {
    		@ApiResponse(responseCode = "200", description = "Sucesso na operação."),
            @ApiResponse(responseCode = "400", description = "Parâmetro com formatação incorreta ou Categoria não corresponde à operação."),
            @ApiResponse(responseCode = "401", description = "Token JWT expirado."),
            @ApiResponse(responseCode = "403", description = "Token JWT não inserido no cabeçalho."),
            @ApiResponse(responseCode = "404", description = "Violação das regras de negócio."),
            @ApiResponse(responseCode = "500", description = "Erro no Sistema."),
    })
	public ResponseEntity<Void> restaurarUsuario(@RequestHeader("Authorization") String bearerToken, @PathVariable Long id){
		return usuarioService.desbloquearUsuarioId(bearerToken, id);
	}
	
	@GetMapping("/bloquear/{id}")
	@Operation(
			summary = "Bloqueia um usuário com base no seu id.", 
			description = "Método que bloqueia(torna o atributo status em Inativo) um usuário com base no parâmetro id. Requer role 'Administrador'.", 
			security = {@SecurityRequirement(name = "Bearer Authentication")})
	@RolesAllowed({"Administrador"})
    @ApiResponses(value = {
    		@ApiResponse(responseCode = "200", description = "Sucesso na operação."),
            @ApiResponse(responseCode = "400", description = "Parâmetro com formatação incorreta ou Categoria não corresponde à operação."),
            @ApiResponse(responseCode = "401", description = "Token JWT expirado."),
            @ApiResponse(responseCode = "403", description = "Token JWT não inserido no cabeçalho."),
            @ApiResponse(responseCode = "404", description = "Violação das regras de negócio."),
            @ApiResponse(responseCode = "500", description = "Erro no Sistema."),
    })
	public ResponseEntity<Void> bloquearUsuario(@RequestHeader("Authorization") String bearerToken, @PathVariable Long id){
		return usuarioService.bloquearUsuarioPorId(bearerToken, id);
	}
	
}