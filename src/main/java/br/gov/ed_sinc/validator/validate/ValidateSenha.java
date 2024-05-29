package br.gov.ed_sinc.validator.validate;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import br.gov.ed_sinc.validator.SenhaValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;


@Target({ElementType.FIELD,ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = SenhaValidator.class)

public @interface ValidateSenha {

	public String message() default "inválida, senha deve conter no mínimo 8 caracteres, com pelo menos uma letra maiúscula e um número.";
	
	Class<?>[] groups() default {};
	
	Class<? extends Payload>[] payload() default {};
}
