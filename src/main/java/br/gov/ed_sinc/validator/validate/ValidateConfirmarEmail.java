package br.gov.ed_sinc.validator.validate;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import br.gov.ed_sinc.validator.ConfirmarEmailValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = ConfirmarEmailValidator.class)

public @interface ValidateConfirmarEmail {

	public String message() default "Email e Confirmar Email devem ser iguais";
	
	Class<?>[] groups() default {};
	
	Class<? extends Payload>[] payload() default {};
}