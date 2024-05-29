package br.gov.ed_sinc.validator;

import java.util.regex.Pattern;

import br.gov.ed_sinc.validator.validate.ValidateSenha;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class SenhaValidator implements ConstraintValidator<ValidateSenha,String>{

	@Override
	public boolean isValid(String senha, ConstraintValidatorContext context) {
		
	   // Pattern specailCharPatten = Pattern.compile("[^a-z0-9 ]", Pattern.CASE_INSENSITIVE);
	    Pattern UpperCasePatten = Pattern.compile("[A-Z ]");
	    Pattern lowerCasePatten = Pattern.compile("[a-z ]");
	    Pattern digitCasePatten = Pattern.compile("[0-9 ]");

	    boolean flag=true;

	   /* if (!passwordhere.equals(confirmhere)) {
	        flag=false;
	    }
	   */
	    if (senha.length() < 8) {
	        flag=false;
	    }
	   /* if (!specailCharPatten.matcher(senha).find()) {
	        flag=false;
	    }
	   */
	    if (!UpperCasePatten.matcher(senha).find()) {
	        flag=false;
	    }
	    if (!lowerCasePatten.matcher(senha).find()) {
	        flag=false;
	    }
	    if (!digitCasePatten.matcher(senha).find()) {
	        flag=false;
	    }

	    return flag;
	}
}
