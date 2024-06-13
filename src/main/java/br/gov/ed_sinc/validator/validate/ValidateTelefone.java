package br.gov.ed_sinc.validator.validate;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import br.gov.ed_sinc.validator.TelefoneValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = TelefoneValidator.class)
public @interface ValidateTelefone {
    String message() default " é inválido";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}