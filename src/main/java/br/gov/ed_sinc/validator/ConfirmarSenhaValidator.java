package br.gov.ed_sinc.validator;

import br.gov.ed_sinc.dto.input.SenhaEConfirmarSenhaInput;
import br.gov.ed_sinc.validator.validate.ValidateConfirmarSenha;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ConfirmarSenhaValidator implements ConstraintValidator<ValidateConfirmarSenha, SenhaEConfirmarSenhaInput> {

	@Override
	public boolean isValid(SenhaEConfirmarSenhaInput obj, ConstraintValidatorContext context) {
		
		if(obj==null) {
			return false;
		}

		return obj.getConfirmarSenha().matches(obj.getSenha()) ? true : false;

	}
}