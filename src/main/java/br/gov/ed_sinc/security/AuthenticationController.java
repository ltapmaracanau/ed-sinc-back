package br.gov.ed_sinc.security;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.gov.ed_sinc.model.Usuario;
import br.gov.ed_sinc.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import net.bytebuddy.utility.RandomString;

@CrossOrigin
@AllArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthenticationController {

	private final AuthenticationService authService;

	@Autowired
	private UsuarioService usuarioService;
	
	/*
	@PostMapping("/registerall")
	public ResponseEntity<AuthenticationResponse> registerCategoriaGeral(@Valid @RequestBody RegisterRequest request) {
		return ResponseEntity.ok(authService.register(request));
	}
	*/
	
	@PostMapping("/register")
	public ResponseEntity<AuthenticationResponse> registerCatagoriaUsuarioComum(@Valid @RequestBody RegisterRequest request) {
		return ResponseEntity.ok(authService.registerCategoriaUsuarioComum(request));
	}
	
	@PostMapping("/authenticate")
	public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request) {
		Optional<Usuario> usuario = usuarioService.buscarPorEmail(request.getEmail());
		if (usuario.isPresent()) {
			if (usuario.get().isAccountNonLocked()) {
				if (usuario.get().getFailedAttempt() < UsuarioService.MAX_FAILURE_ATTEMPTS - 1) {
					usuarioService.incrementarFailureAttempt(usuario.get());
				} else {
					usuarioService.bloquearUsuario(usuario.get().getId());
				}
			}
		}
		return ResponseEntity.ok(authService.authenticate(request));
	}
	
    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@RequestBody JwtRequest jwtRequest) {
        if (jwtRequest.getToken() != null) {
        	return authService.refreshToken(jwtRequest.getToken());
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
	
	@GetMapping("/authenticate/{email}")
	public AuthenticationUsuarioReportResponse usuarioReport(@PathVariable String email) {
		return authService.usuarioReport(email);
	}
	
	/*envia ao usuario o link + token de reset*/
	@PostMapping("/reset")
	public ResponseEntity<Void> enviarEmailNovaSenha(@RequestBody @Valid ResetSenhaRequest request) {
		String token = RandomString.make(8);
		return usuarioService.resetarSenha(request, token);
	}
	
	/*edita o usuario com o token de reset*/
	@PutMapping("/reset/{token}")
	public ResponseEntity<Void> cadastrarNovaSenha(@RequestBody @Valid ResetSenhaEditRequest request,
			@PathVariable String token) {
		return usuarioService.editarUsuarioPorToken(request, token);
	}
	/*
	@PostMapping("/contato")
	public ResponseEntity<String> enviarMensagemContato(@RequestBody @Valid MensagemContatoRequest request) {
		return emailService.enviarMensagemContato(request);
	}
	*/

}
