package br.gov.ed_sinc.validator;

import java.time.LocalDate;

import br.gov.ed_sinc.validator.validate.ValidateDate;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class DateValidator implements ConstraintValidator<ValidateDate, LocalDate> {

    @Override
    public void initialize(ValidateDate constraintAnnotation) {
        // pode ser usado para inicializar o validador, se necessário
    }

    @Override
    public boolean isValid(LocalDate value, ConstraintValidatorContext context) {
    	System.out.println("entrou");
        if (value == null) {
            return true;
        }
        
        // Verifica se o mês é válido (1-12)
        int month = value.getMonthValue();
        if (month < 1 || month > 12) {
            return false;
        }

        // Verifica se o dia é válido para o mês especificado
        int day = value.getDayOfMonth();
        int maxDayOfMonth = value.getMonth().length(value.isLeapYear());
        if (day < 1 || day > maxDayOfMonth) {
            return false;
        }

        return true;
    }
}