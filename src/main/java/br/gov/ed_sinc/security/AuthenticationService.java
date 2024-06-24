package br.gov.ed_sinc.security;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import br.gov.ed_sinc.config.JwtService;
import br.gov.ed_sinc.dto.disassembler.UsuarioInputDisassembler;
import br.gov.ed_sinc.dto.input.PreCadastroInput;
import br.gov.ed_sinc.exception.NegocioException;
import br.gov.ed_sinc.model.Usuario;
import br.gov.ed_sinc.repository.UsuarioRepository;
import br.gov.ed_sinc.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import net.bytebuddy.utility.RandomString;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
	private final UsuarioRepository repository;
	private final PasswordEncoder passwordEncoder;
	private final JwtService jwtService;
	private final UserDetailsService userDetailsService;
	private final AuthenticationManager authenticationManager;
	private final UsuarioService usuarioService;
	private final UsuarioInputDisassembler usuarioInputDisassembler;

	@Value("${edsinc.frontend-url}")
	private String frontendURL;

	public AuthenticationResponse register(RegisterRequest request) {
		var user = Usuario.builder()
				.nome(request.getNome())
				.cpf(request.getCpf())
				.email(request.getEmail())
				.senha(passwordEncoder.encode(request.getSenha()))
				.telefone(request.getTelefone())
				.categorias(request.getCategorias())
				.accountNonLocked(true)
				.dataNascimento(request.getDataNascimento())
				.failedAttempt(0)
				.build();
		repository.save(user);
		var jwtToken = jwtService.generateToken(user);
		return AuthenticationResponse.builder().token(jwtToken).build();
	}

	public AuthenticationResponse registerCategoriaUsuarioComum(PreCadastroInput request) {
		Usuario usuario = usuarioInputDisassembler.toDomainObjectPreRegistro(request);
		repository.detach(usuario);
		String senha = RandomString.make(20);
		usuario.setSenha(senha);
		boolean emailEmUso = repository.findByEmail(usuario.getEmail()).stream()
				.anyMatch(usuarioExistente -> !usuarioExistente.equals(usuario));
		if (emailEmUso) {
			throw new NegocioException(
					String.format("Já existe um usuário cadastrado com o e-mail %s", usuario.getEmail()));
		}
		repository.save(usuario);
		var jwtToken = jwtService.generateToken(usuario);
		String token = RandomString.make(8);
		String linksetSenha = frontendURL + "/novaSenha";
		usuarioService.setarSenhaUsuarioComum(request.getEmailEConfirmarEmail().getEmail(), token, linksetSenha);
		return AuthenticationResponse.builder().token(jwtToken).build();
	}

	public AuthenticationResponse authenticate(AuthenticationRequest request) {
		authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getSenha()));
		var user = repository.findByEmail(request.getEmail()).orElseThrow();
		usuarioService.resetarFailedAttempt(request.getEmail());
		usuarioService.resetarAccountNonLocked(request.getEmail());
		var jwtToken = jwtService.generateToken(user);
		String nome = user.getNome();
		//int index = nome.indexOf(" ");
		//if (index != -1) {
		//		nome = nome.substring(0, index);
		//	} 
		
		return AuthenticationResponse.builder()
		        .token(jwtToken)
		        .nome(nome)
		        .build();
	}

	public AuthenticationUsuarioReportResponse usuarioReport(String email) {
		AuthenticationUsuarioReportResponse response = new AuthenticationUsuarioReportResponse();
		Optional<Usuario> usuario = repository.findByEmail(email);
		if (usuario.isPresent()) {
			if (usuario.get().isAccountNonLocked()) {
				response.setNumErros(usuario.get().getFailedAttempt());
				return response;
			}
			response.setUsuarioBloqueado(true);
			response.setNumErros(3);
		}
		return response;

	}
	
	public ResponseEntity<?> refreshToken(String jwt) {
        String userEmail = jwtService.extractUsernameWithoutValidation(jwt);
        if (userEmail != null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(userEmail);
            String newJwtToken = jwtService.generateToken(userDetails);
            AuthenticationResponse response = new AuthenticationResponse(newJwtToken);
            return ResponseEntity.ok(response);
        }
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
	}
	
}
