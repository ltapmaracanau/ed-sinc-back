package br.gov.ed_sinc.model;

import java.io.Serializable;
import java.util.List;

import org.hibernate.annotations.DynamicUpdate;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import br.gov.ed_sinc.model.enums.Status;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tbl_pessoa_com_deficiencia")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@DynamicUpdate
public class PessoaComDeficiencia implements Serializable {

	private static final long serialVersionUID = 1L;

	@EqualsAndHashCode.Include
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "tbl_pessoa_com_deficiencia_id_generator")
	@SequenceGenerator(name = "tbl_pessoa_com_deficiencia_id_generator", initialValue = 1, allocationSize = 1, sequenceName = "tbl_pessoa_com_deficiencia_id_generator_seq")
	private Long id;

	@NotBlank
	@Size(max = 100)
	private String nome;
	
	private String descricao;

	@Builder.Default
	@Enumerated(EnumType.ORDINAL)
	@NotNull
	private Status status = Status.Ativo;
	
	@ManyToMany(mappedBy = "pessoasComDeficiencia")
	private List<Usuario> usuarios;

}

