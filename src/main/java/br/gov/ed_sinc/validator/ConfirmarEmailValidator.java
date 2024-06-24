package br.gov.ed_sinc.validator;

import br.gov.ed_sinc.dto.input.EmailEConfirmarEmailInput;
import br.gov.ed_sinc.validator.validate.ValidateConfirmarEmail;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ConfirmarEmailValidator implements ConstraintValidator<ValidateConfirmarEmail, EmailEConfirmarEmailInput> {

	@Override
	public boolean isValid(EmailEConfirmarEmailInput obj, ConstraintValidatorContext context) {
		
		if(obj==null) {
			return false;
		}

		return obj.getConfirmarEmail().matches(obj.getEmail()) ? true : false;

	}
}