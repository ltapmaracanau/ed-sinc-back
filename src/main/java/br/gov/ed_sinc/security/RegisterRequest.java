package br.gov.ed_sinc.security;

import java.time.LocalDate;

import org.hibernate.validator.constraints.br.CPF;

import br.gov.ed_sinc.model.enums.Categoria;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

	@NotBlank
	@Size(max = 60)
	private String nome;
	/*
	@CPF
	private String cpf;
	*/
	@Email
	@NotBlank
	private String email;
	
	private String senha;
	
	@NotNull
	@Past
	private LocalDate dataNascimento;
	
	@Builder.Default
	private Categoria categoria = Categoria.Aluno;
	
	private String telefone;
	
	private Integer failedAttempt;
	
	private boolean accountNonLocked;
}
