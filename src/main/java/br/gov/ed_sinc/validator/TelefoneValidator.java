package br.gov.ed_sinc.validator;

import java.util.regex.Pattern;

import br.gov.ed_sinc.validator.validate.ValidateTelefone;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class TelefoneValidator implements ConstraintValidator<ValidateTelefone, String> {
    private static final Pattern TELEFONE_PATTERN = Pattern.compile("\\(\\d{2}\\)\\d{5}-\\d{4}");

    @Override
    public void initialize(ValidateTelefone constraintAnnotation) {
        // Você pode inicializar o validador aqui, se precisar.
    }

    @Override
    public boolean isValid(String telefone, ConstraintValidatorContext context) {
        // se telefone for null ou vazio, retorne verdadeiro
        if (telefone == null || telefone.trim().isEmpty()) {
            return true;
        }
        return telefone.length()==11;
    }
}