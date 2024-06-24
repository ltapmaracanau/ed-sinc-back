package br.gov.ed_sinc.dto.input;

import java.time.LocalDate;
import java.util.List;

import org.hibernate.validator.constraints.br.CPF;

import com.fasterxml.jackson.annotation.JsonIgnore;

import br.gov.ed_sinc.model.enums.Raca;
import br.gov.ed_sinc.validator.validate.ValidateConfirmarEmail;
import br.gov.ed_sinc.validator.validate.ValidateDate;
import br.gov.ed_sinc.validator.validate.ValidateTelefone;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class PreCadastroInput {
	@NotBlank
	@Size(max = 60)
	private String nome;
	
	@ValidateConfirmarEmail
	@Valid
	private EmailEConfirmarEmailInput emailEConfirmarEmail;
	
	@CPF
	private String cpf;

	@ValidateTelefone
	private String telefone;
	
	@Past
	@ValidateDate
	private LocalDate dataNascimento;
	
	@Valid
	private List<EntidadeGenericaIdInput> gruposSociais;
	
	@Valid
	private List<EntidadeGenericaIdInput> pessoasNeurodivergente;
	
	@Valid
	private List<EntidadeGenericaIdInput> pessoasComDeficiencia;
	
	@Valid
	private List<EntidadeGenericaIdInput> polos;
	
	@NotNull
    private Raca raca;
	
	@JsonIgnore
	public String getEmail() {
		return emailEConfirmarEmail.getEmail();
	}
	
}