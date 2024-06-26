package br.gov.ed_sinc.model;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.validator.constraints.br.CPF;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import br.gov.ed_sinc.model.enums.Categoria;
import br.gov.ed_sinc.model.enums.Raca;
import br.gov.ed_sinc.model.enums.Status;
import br.gov.ed_sinc.validator.validate.ValidateSenha;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tbl_usuario")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@DynamicUpdate
public class Usuario implements UserDetails {

    private static final long serialVersionUID = 1L;

    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "tbl_usuario_id_generator")
    @SequenceGenerator(name = "tbl_usuario_id_generator", initialValue = 1, allocationSize = 1, sequenceName = "tbl_usuario_id_generator_seq")
    private Long id;

    @NotBlank
    @Size(max = 60)
    private String nome;

    @CPF
    private String cpf;

    @NotBlank
    @Size(max = 255)
    @Email
    private String email;

    @ValidateSenha
    private String senha;

    @ElementCollection(targetClass = Categoria.class)
    @Enumerated(EnumType.ORDINAL)
    @NotNull
    @CollectionTable(name = "tbl_usuario_categorias", joinColumns = @JoinColumn(name = "usuario_id"))
    @Column(name = "categoria")
    private List<Categoria> categorias;

    @Column(name = "data_nascimento")
    private LocalDate dataNascimento;

    private String resetSenhaToken;

    @Builder.Default
    private Boolean exportado = false;

    @Builder.Default
    private boolean accountNonLocked = true;

    @Builder.Default
    private Integer failedAttempt = 0;

    @ManyToMany
    private List<GrupoSocial> gruposSociais;

    @ManyToMany
    private List<PessoaComDeficiencia> pessoasComDeficiencia;

    @ManyToMany
    private List<PessoaNeurodivergente> pessoasNeurodivergente;

    @ManyToMany
    private List<Polo> polos;
    
    @ManyToMany(mappedBy = "usuarios")
    private List<Turma> turmas;

    @Builder.Default
    @Enumerated(EnumType.ORDINAL)
    @NotNull
    private Status status = Status.Inativo;

    @Builder.Default
    @Enumerated(EnumType.ORDINAL)
    @NotNull
    private Raca raca = Raca.Parda;

    private String telefone;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return categorias.stream()
            .map(cat -> new SimpleGrantedAuthority(cat.getDescricao()))
            .collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return senha;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return accountNonLocked;
    }

    public void setAccountNonLocked(boolean accountNonLocked) {
        this.accountNonLocked = accountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
