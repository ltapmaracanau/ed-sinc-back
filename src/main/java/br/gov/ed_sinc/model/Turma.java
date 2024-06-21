package br.gov.ed_sinc.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;

import br.gov.ed_sinc.model.enums.Status;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tbl_turma")
public class Turma {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "tbl_turma_id_generator")
    @SequenceGenerator(name = "tbl_turma_id_generator", initialValue = 1, allocationSize = 1, sequenceName = "tbl_turma_id_generator_seq")
    private Long id;

    @NotBlank
    @Size(max = 60)
    private String nome;

    @Size(max = 255)
    private String descricao;
    
    @Builder.Default
    private Boolean exportado = false;
    
    @Builder.Default
    @Enumerated(EnumType.ORDINAL)
    @NotNull
    private Status status = Status.Ativo;
    
    @ManyToMany
    @JoinTable(
        name = "tbl_usuario_turmas",
        joinColumns = @JoinColumn(name = "turmas_id"),
        inverseJoinColumns = @JoinColumn(name = "usuarios_id")
    )
    private List<Usuario> usuarios;

}
