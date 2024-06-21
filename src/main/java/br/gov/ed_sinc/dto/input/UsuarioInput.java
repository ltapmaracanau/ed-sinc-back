package br.gov.ed_sinc.dto.input;

import java.time.LocalDate;
import java.util.List;

import org.hibernate.validator.constraints.br.CPF;

import com.fasterxml.jackson.annotation.JsonIgnore;

import br.gov.ed_sinc.model.enums.Categoria;
import br.gov.ed_sinc.model.enums.Status;
import br.gov.ed_sinc.validator.validate.ValidateConfirmarSenha;
import br.gov.ed_sinc.validator.validate.ValidateDate;
import br.gov.ed_sinc.validator.validate.ValidateTelefone;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class UsuarioInput {
	@NotBlank
	@Size(max = 60)
	private String nome;

	@ValidateConfirmarSenha
	@Valid
	private SenhaEConfirmarSenhaInput senhaEConfirmarSenha;
	
	@Email
	@NotBlank
	private String email;

	@CPF
	private String cpf;

	@ValidateTelefone
	private String telefone;
	
	@Past
	@ValidateDate
	private LocalDate dataNascimento;
	
	private List<Categoria> categorias;
	
	@Valid
	private List<EntidadeGenericaIdInput> gruposSociais;
	
	@Valid
	private List<EntidadeGenericaIdInput> pessoasNeurodivergente;
	
	@Valid
	private List<EntidadeGenericaIdInput> pessoasComDeficiencia;
	
	@Valid
	private List<EntidadeGenericaIdInput> polos;
	
	@NotNull
	private Boolean exportado;
	
	@NotNull
	private Status status;
	
	@JsonIgnore
	public String getSenha() {
		return senhaEConfirmarSenha.getSenha();
	}
}