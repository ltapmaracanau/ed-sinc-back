package br.gov.ed_sinc.validator.validate;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import br.gov.ed_sinc.validator.ConfirmarSenhaValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = ConfirmarSenhaValidator.class)

public @interface ValidateConfirmarSenha {

	public String message() default "Senha e Confirmar Senha devem ser iguais";
	
	Class<?>[] groups() default {};
	
	Class<? extends Payload>[] payload() default {};
}