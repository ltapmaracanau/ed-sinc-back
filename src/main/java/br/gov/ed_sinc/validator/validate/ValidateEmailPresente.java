package br.gov.ed_sinc.validator.validate;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import br.gov.ed_sinc.validator.EmailPresenteValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Target({ElementType.FIELD,ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = EmailPresenteValidator.class)

public @interface ValidateEmailPresente {

	public String message() default "O Email passado não está em nossa base de dados.";
	
	Class<?>[] groups() default {};
	
	Class<? extends Payload>[] payload() default {};
}