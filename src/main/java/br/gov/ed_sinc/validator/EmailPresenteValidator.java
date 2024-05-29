package br.gov.ed_sinc.validator;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import br.gov.ed_sinc.model.Usuario;
import br.gov.ed_sinc.service.UsuarioService;
import br.gov.ed_sinc.validator.validate.ValidateEmailPresente;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class EmailPresenteValidator implements ConstraintValidator<ValidateEmailPresente, String> {
	
	@Autowired
	UsuarioService usuarioService;

	@Override
	public boolean isValid(String email, ConstraintValidatorContext context) {
		Optional<Usuario> usuario = usuarioService.buscarPorEmail(email);
		return usuario.isPresent();
	}

}
