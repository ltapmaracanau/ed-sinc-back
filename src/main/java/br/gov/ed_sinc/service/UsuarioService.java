package br.gov.ed_sinc.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import br.gov.ed_sinc.config.JwtService;
import br.gov.ed_sinc.exception.NegocioException;
import br.gov.ed_sinc.exception.entity.UsuarioNaoEncontradoException;
import br.gov.ed_sinc.filter.UsuarioFilter;
import br.gov.ed_sinc.model.Usuario;
import br.gov.ed_sinc.model.enums.Categoria;
import br.gov.ed_sinc.model.enums.Status;
import br.gov.ed_sinc.projection.pesquisa.PageUsuarioPesquisaProjection;
import br.gov.ed_sinc.projection.pesquisa.UsuarioPesquisaProjection;
import br.gov.ed_sinc.projection.pesquisa.impl.PageUsuarioPesquisaProjectionImpl;
import br.gov.ed_sinc.projection.relatorio.UsuarioRelatorioProjection;
import br.gov.ed_sinc.repository.UsuarioRepository;
import br.gov.ed_sinc.security.ResetSenhaEditRequest;
import br.gov.ed_sinc.security.ResetSenhaRequest;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;


@AllArgsConstructor
@Service
public class UsuarioService {
    public static final Integer MAX_FAILURE_ATTEMPTS = 3;
    private final PasswordEncoder passwordEncoder;
    private final UsuarioRepository usuarioRepository;
    private final EmailService emailService;
    private final JwtService jwtService;

    @Value("${edsinc.frontend-url}")
    private String frontendURL;


	public Usuario buscarOuFalhar(Long id) {
		return usuarioRepository.findById(id).orElseThrow(() -> new UsuarioNaoEncontradoException(id));
	}

	@Transactional
	public Usuario adicionarUsuario(Usuario usuario) {
		usuarioRepository.detach(usuario);
		boolean emailEmUso = usuarioRepository.findByEmail(usuario.getEmail()).stream()
				.anyMatch(usuarioExistente -> !usuarioExistente.equals(usuario));
		if (emailEmUso) {
			throw new NegocioException(
					String.format("Já existe um usuário cadastrado com o e-mail %s", usuario.getEmail()));
		}
		/* checagem da criptografia*/
		if(!usuario.getSenha().startsWith("$2a$10")){
			usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
		}
		
		return usuarioRepository.save(usuario);
	}

	public Page<Usuario> listarUsuarios(Pageable pageable) {
		return usuarioRepository.findAll(pageable);
	}

	public ResponseEntity<Usuario> buscarUsuario(Long id) {
		return usuarioRepository.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
	}

	@Transactional
	public ResponseEntity<Void> deletarUsuario(Long id) {
		if (!usuarioRepository.existsById(id)) {
			return ResponseEntity.notFound().build();
		}
		usuarioRepository.deleteById(id);
		return ResponseEntity.noContent().build();
	}

	@Transactional
	public void incrementarFailureAttempt(Usuario usuairo) {
		Integer novoFailureAttempt = usuairo.getFailedAttempt() + 1;
		usuarioRepository.updateFailedAttempt(novoFailureAttempt, usuairo.getEmail());
	}

	public Optional<Usuario> buscarPorEmail(String email) {
		return usuarioRepository.findByEmail(email);
	}

	@Transactional
	public ResponseEntity<Void> bloquearUsuarioPorId(String bearerToken, Long id) {
		String jwtToken = bearerToken.split(" ")[1].trim();
		String username = jwtService.extractUsername(jwtToken);
		Optional<Usuario> requisitante = usuarioRepository.findByEmail(username);
	    if (requisitante.isPresent() && requisitante.get().getCategorias().contains(Categoria.Administrador)) {
			Optional<Usuario> usuario = usuarioRepository.findById(id);
			if (usuario.isPresent()) {
				usuarioRepository.updateStatus(Status.Bloqueado, usuario.get().getEmail());
				usuarioRepository.updateAccountNonLocked(false, usuario.get().getEmail());
				return ResponseEntity.ok().build();
			}
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.badRequest().build();

	}

	@Transactional
	public ResponseEntity<Void> desbloquearUsuarioId(String bearerToken, Long id) {
		String jwtToken = bearerToken.split(" ")[1].trim();
		String username = jwtService.extractUsername(jwtToken);
		Optional<Usuario> requisitante = usuarioRepository.findByEmail(username);
		if (requisitante.isPresent() && requisitante.get().getCategorias().contains(Categoria.Administrador)) {
			Optional<Usuario> usuarioBloqueado = usuarioRepository.findById(id);
			if (usuarioBloqueado.isPresent()) {
				usuarioRepository.updateAccountNonLocked(true, usuarioBloqueado.get().getEmail());
				usuarioRepository.updateFailedAttempt(0, usuarioBloqueado.get().getEmail());
				usuarioRepository.updateStatus(Status.Ativo, usuarioBloqueado.get().getEmail());
				return ResponseEntity.ok().build();
			}
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.badRequest().build();

	}
	
	@Transactional
	public void bloquearUsuario(Long id) {
		Optional<Usuario> usuario = usuarioRepository.findById(id);
		if (usuario.isPresent()) {
				usuarioRepository.updateStatus(Status.Bloqueado, usuario.get().getEmail());
				usuarioRepository.updateAccountNonLocked(false, usuario.get().getEmail());
			}
		return;
	
	}

	@Transactional
	public void resetarFailedAttempt(String email) {
		usuarioRepository.updateFailedAttempt(0, email);
	}

	@Transactional
	public void resetarAccountNonLocked(String email) {
		usuarioRepository.updateAccountNonLocked(true, email);
	}

	public void atualizarSenha(Usuario usuario, String novaSenha) {
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		String encodedPassword = passwordEncoder.encode(novaSenha);
		usuario.setSenha(encodedPassword);
		usuario.setResetSenhaToken(null);
		usuarioRepository.save(usuario);
	}

	public ResponseEntity<Usuario> buscarUsuarioPorToken(String token) {
		return usuarioRepository.findByResetSenhaToken(token).map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());

	}

	@Transactional
	public void setarSenhaUsuarioComum(String email, String token, String linksetSenha) {
		Optional<Usuario> usuario = usuarioRepository.findByEmail(email);
		if (usuario.isPresent()) {
			usuario.get().setResetSenhaToken(token);
			usuarioRepository.save(usuario.get());
			emailService.enviarEmailConfirmacaoCadastro(email, token, linksetSenha);
		}
		return;

	}
	
	
	@Transactional
	public ResponseEntity<Void> editarUsuarioPorToken(ResetSenhaEditRequest request, String token) {
		Optional<Usuario> usuario = usuarioRepository.findByResetSenhaToken(token);
		if (usuario.isPresent()) {
			if (request.getSenha().matches(request.getConfirmarSenha())) {
				atualizarSenha(usuario.get(), request.getSenha());
				resetarAccountNonLocked(usuario.get().getEmail());
				resetarFailedAttempt(usuario.get().getEmail());
				return ResponseEntity.ok().build();
			}
			return ResponseEntity.badRequest().build();
		}
		return ResponseEntity.notFound().build();
	}
	
	@Transactional
	public ResponseEntity<Void> resetarSenha(ResetSenhaRequest request, String token) {
		String linkResetSenha = frontendURL + "/reset";
		Optional<Usuario> usuario = usuarioRepository.findByEmail(request.getEmail());
		if (usuario.isPresent()) {
			usuario.get().setResetSenhaToken(token);
			usuarioRepository.save(usuario.get());
			emailService.enviarEmailRecuperacaoSenha(request.getEmail(), token, linkResetSenha);
			return ResponseEntity.accepted().build();
		} else {
			return ResponseEntity.notFound().build();
		}

	}
	

	public void autorizarAdmin(String bearerToken) {
		String jwtToken = bearerToken.split(" ")[1].trim();
		String username = jwtService.extractUsername(jwtToken);
		Optional<Usuario> requisitante = usuarioRepository.findByEmail(username);
		if (!requisitante.get().getCategorias().contains(Categoria.Administrador)) {
			throw new NegocioException(String.format("Seu usuário não tem permissão para essa operação "));
		}
	}
	
	public boolean autorizarAdminBoolean(String bearerToken) {
		String jwtToken = bearerToken.split(" ")[1].trim();
		String username = jwtService.extractUsername(jwtToken);
		Optional<Usuario> requisitante = usuarioRepository.findByEmail(username);
		if (requisitante.isPresent() && requisitante.get().getCategorias().contains(Categoria.Administrador)) {
			return true;
		} 
		return false;	
	}
	/*
	public void autorizarAdminOuCoordenador(String bearerToken) {
		String jwtToken = bearerToken.split(" ")[1].trim();
		String username = jwtService.extractUsername(jwtToken);
		Optional<Usuario> requisitante = usuarioRepository.findByEmail(username);
		if (requisitante.get().getCategoria() == Categoria.COM) {
			throw new NegocioException(String.format("Seu usuário não tem permissão para essa operação "));
		}
	}
	*/
	
	public Long usuarioIdPorToken(String bearerToken) {
		String jwtToken = bearerToken.split(" ")[1].trim();
		String username = jwtService.extractUsername(jwtToken);
		Optional<Usuario> requisitante = usuarioRepository.findByEmail(username);
		if (requisitante.isPresent()) {
			return requisitante.get().getId();
		} else {
			return 0L;
		}
	}
	
	@Transactional
	public ResponseEntity<Void> arquivarUsuarioPorId(String bearerToken, Long id) {
		autorizarAdmin(bearerToken);
		Optional<Usuario> usuarioArquivado = usuarioRepository.findById(id);
		if (usuarioArquivado.isPresent()) {
			usuarioRepository.updateStatus(Status.Arquivado, usuarioArquivado.get().getEmail());
			usuarioRepository.updateAccountNonLocked(false, usuarioArquivado.get().getEmail());
			return ResponseEntity.ok().build();
		}
		return ResponseEntity.notFound().build();
	}
	
	/*

	@Override
	public Page<Usuario> consultarUsuarioFiltro(UsuarioFilter filtro, Pageable pageable) {
		return usuarioRepository.consultarUsuarioFiltro(filtro, pageable);
	}
	*/
	public Optional<Usuario> buscarUsuarioPorEmail(String email) {
	    return usuarioRepository.findByEmail(email);
	}

	public List<UsuarioRelatorioProjection> listarUsuariosRelatorioProjetado(){
		return usuarioRepository.listarUsuariosRelatorioProjetado();
	}
	
	public PageUsuarioPesquisaProjection listarUsuariosPesquisaProjetado(UsuarioFilter filtro, Pageable pageable) {
        Page<UsuarioPesquisaProjection> page = usuarioRepository.listarUsuariosPesquisaProjetado(filtro.getNome(), filtro.getEmail(),Status.Ativo ,pageable);
        return new PageUsuarioPesquisaProjectionImpl(page);
	}
	
}

